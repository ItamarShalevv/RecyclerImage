package itamar.com.recyclerimage.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Utils {


    public static void hideStatusBar(AppCompatActivity activity){
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setFullScreen(AppCompatActivity activity){
        hideStatusBar(activity);
        hideActionBar(activity);
    }

    public static void hideActionBar(AppCompatActivity activity) {
        android.app.ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } else {
            ActionBar compactActionBar = activity.getSupportActionBar();
            if (compactActionBar != null) {
                compactActionBar.hide();
            }
        }

    }
    /**
     * @param message  The message you want show
     * @param duration SnackBar duration
     */
    public static void makeAndShowSnackBar(Activity activity, String message, @Snackbar.Duration int duration) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, duration).show();
    }

    /**
     * This snackBar show until you dismiss it
     *
     * @param message The message you want show
     * @return The snackBar you need dismiss
     */
    public static Snackbar makeAndShowDialogProgress(Activity activity, String message) {
        Snackbar snackDialogProgress = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        TextView textViewSnackBar = snackDialogProgress.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        //textViewSnackBar.setTextSize(22);
        ViewGroup contentLay = (ViewGroup) textViewSnackBar.getParent();
        contentLay.setPadding(16, 16, 16, 16);
        ProgressBar item = new ProgressBar(activity);
        contentLay.addView(item);
        snackDialogProgress.show();
        return snackDialogProgress;
    }
    public static boolean isPortraitScreen(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
