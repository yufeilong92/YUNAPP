package com.lawyee.yj.subaoyun.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lawyee.yj.subaoyun.R;


/**
 * Created by yfl on 2016/11/9 08:55.
 *
 * @purpose:
 */

public class ShowDialogs {
    public void setDialogs( Context context,String msg){
        createLoadingDialog(context,msg);
    }
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view1 = inflater.inflate(R.layout.dialog, null);
        LinearLayout layout = (LinearLayout) view1.findViewById(R.id.dialog_view);
        TextView tv = (TextView) view1.findViewById(R.id.tipTextView);
        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        tv.setText(msg);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT
        ));
        return dialog;
    }
}
