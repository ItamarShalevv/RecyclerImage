package itamar.com.recyclerimage.dialog;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import itamar.com.recyclerimage.R;
import itamar.com.recyclerimage.view.ZoomView;

/**
 * Image zoom dialog for make zoom to image
 * for use call to ImageZoomDialog.createInstance() and give String imageUrl
 */
public class ImageZoomDialog extends DialogFragment {

    private static final String TAG_IMAGE_PATH = "TAG_IMAGE_PATH";
    private String mImagePath;
    private ZoomView zoomView;
    private ImageButton exitImageButton;

    public static ImageZoomDialog createInstance(String imagePath) {
        ImageZoomDialog imageZoomDialog = new ImageZoomDialog();
        Bundle args = new Bundle();
        args.putString(TAG_IMAGE_PATH, imagePath);
        imageZoomDialog.setArguments(args);
        return imageZoomDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_image_zoom,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View parent, @Nullable Bundle savedInstanceState) {
        initVar();
        initViews(parent);
        initListeners();
        Glide.with(getContext()).load(mImagePath).into(zoomView);
    }

    private void initVar() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mImagePath = arguments.getString(TAG_IMAGE_PATH);
        }

    }

    private void initViews(View parent) {
        zoomView = parent.findViewById(R.id.zoom_image_view);
        exitImageButton = parent.findViewById(R.id.exit_image_button);
    }

    private void initListeners() {
        exitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
