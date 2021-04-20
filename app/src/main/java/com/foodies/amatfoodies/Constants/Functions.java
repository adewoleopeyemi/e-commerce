package com.foodies.amatfoodies.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.foodies.amatfoodies.R;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.text.DecimalFormat;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by qboxus on 10/18/2019.
 */

public class Functions {

    public static void Hide_keyboard(Activity activity){
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            UIUtil.hideKeyboard(activity);
        }
    }



    public static Dialog dialog;
    public static void Show_loader(Context context, boolean outside_touch, boolean cancleable) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_progress_dialog_layout);


        if(!outside_touch)
            dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            dialog.setCancelable(false);

        dialog.show();

    }


    public static void cancel_loader(){
        if(dialog!=null){
            dialog.cancel();
        }
    }


    public static Double Roundoff_decimal(Double value){
        return  Double.valueOf(new DecimalFormat("##.##").format(value));

    }


    public static int convertDpToPx(Context context, int dp) {
        return (int) ((int) dp * context.getResources().getDisplayMetrics().density);
    }

    // this is the delete message diloge which will show after long press in chat message
    public static void Show_Options(Context context,CharSequence[] options,final  Callback callback) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                callback.Responce(""+options[item]);

            }

        });

        builder.show();

    }

}
