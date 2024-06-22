package space.app.Helper;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import space.app.R;

public class Dialog {
    public static void openDialogImage(Context context,Uri uri) {
        if (context == null) {
            return;
        }
        android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_show_image_compo);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(0xBF000000));
        WindowManager.LayoutParams windowAttributesribute = window.getAttributes();
        windowAttributesribute.gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        ImageView image = dialog.findViewById(R.id.image);
        if (uri != null) {
            Glide.with(context).load(uri).into(image);
        }
        dialog.show();
        ImageView backImg =  dialog.findViewById(R.id.close);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
