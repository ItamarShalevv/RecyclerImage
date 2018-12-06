package itamar.com.recyclerimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import itamar.com.recyclerimage.R;
import itamar.com.recyclerimage.dialog.ImageZoomDialog;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {

    private static final String TAG_DIALOG = "ImageZoomDialog";
    private ImageZoomDialog dialog;
    private AppCompatActivity activity;
    private List<String> pathImagesList;
    private LayoutInflater inflater;
    private View.OnClickListener itemViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            } else {
                DialogFragment dialogInStack = (DialogFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
                boolean hasDialogInStack = dialogInStack != null;
                if (hasDialogInStack) {
                    dialogInStack.dismiss();
                }
            }
            int position = (int) v.getTag();
            String path = pathImagesList.get(position);
            dialog = ImageZoomDialog.createInstance(path);
            dialog.show(activity.getSupportFragmentManager(), TAG_DIALOG);

        }
    };


    public ImageAdapter(AppCompatActivity activity, List<String> pathImagesList) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.pathImagesList = pathImagesList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row_image_only_grid, parent, false);
        itemView.setOnClickListener(itemViewListener);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String path = pathImagesList.get(position);
        holder.itemView.setTag(position);
        Glide.with(activity).load(path).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return pathImagesList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
