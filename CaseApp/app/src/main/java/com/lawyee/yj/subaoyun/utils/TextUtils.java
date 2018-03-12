package com.lawyee.yj.subaoyun.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lzh on 2016/2/29.
 */
public class TextUtils {
    private static String telRegex = "[1][358]\\d{9}";
    private static Toast toast;
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
   public static boolean isTelNum(String str){
       if(str.matches(telRegex)){
           return true;
       }else{
           return false;
       }
   }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
