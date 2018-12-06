package itamar.com.recyclerimage;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import itamar.com.recyclerimage.adapter.ImageAdapter;
import itamar.com.recyclerimage.utils.Utils;
import itamar.com.recyclerimage.viewmodel.ListPathViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 1;
    private ImageAdapter mImageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListPathViewModel mListPathViewModel;

    private static boolean needRequestRuntimePermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setFullScreen(this);
        setContentView(R.layout.activity_main);
        initObjects();
        initViews();
    }

    private List<String> getPathImagesFormGallery() {
        return getAllShownImagesPath(this);
    }

    public boolean hasPermissionReadFromStorage() {
        if (needRequestRuntimePermissions()) {
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }


    private void getAllShownImagesPath2(Activity context) {
        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

// content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

// Make the query.
        Cursor cur = managedQuery(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Log.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);

                // Do something with the values.
                Log.i("ListingImages", " bucket=" + bucket
                        + "  date_taken=" + date);
            } while (cur.moveToNext());

        }

    }

    private List<String> getAllShownImagesPath(Activity context) {

        //  Uri uriForFile = FileProvider.getUriForFile(context, authority, new File(getCacheDir(), "/imagesCamera"));


        // The list of columns we're interested in:
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // Specify the provider
                columns, // The columns we're interested in m
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = " + "'Camera'", // A WHERE-filter query
                null, // The arguments for the filter-query
                MediaStore.Images.Media.DATE_ADDED + " DESC" // Order the results, newest first
        );
        //

        List<String> result = new ArrayList<String>(cursor.getCount());

        if (cursor.moveToFirst()) {
            final int image_path_col = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                result.add(cursor.getString(image_path_col));
            } while (cursor.moveToNext());
        }
        cursor.close();


        return result;

    }


    private void initObjects() {
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                List<String> pathImagesFormGallery;
                if (hasPermissionReadFromStorage()) {
                    pathImagesFormGallery = getPathImagesFormGallery();
                } else {
                    pathImagesFormGallery = new ArrayList<>();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                }
                return (T) new ListPathViewModel(pathImagesFormGallery);
            }
        };
        mListPathViewModel = ViewModelProviders.of(this, factory).get(ListPathViewModel.class);
        mImageAdapter = new ImageAdapter(this, mListPathViewModel.getPathImagesList());
        int spanCount = Utils.isPortraitScreen(this) ? 2 : 3;
        mLayoutManager = new GridLayoutManager(this, spanCount, RecyclerView.VERTICAL, false);
    }

    private void initViews() {
        RecyclerView recyclerImages = findViewById(R.id.recycler_images);
        recyclerImages.setAdapter(mImageAdapter);
        recyclerImages.setLayoutManager(mLayoutManager);
        recyclerImages.setHasFixedSize(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                List<String> pathImagesFormGallery = getPathImagesFormGallery();
                mListPathViewModel.getPathImagesList().addAll(pathImagesFormGallery);
                mImageAdapter.notifyDataSetChanged();
            } else {
                Utils.makeAndShowSnackBar(this, getString(R.string.no_have_permission), Snackbar.LENGTH_SHORT);
            }
        }
    }
}
