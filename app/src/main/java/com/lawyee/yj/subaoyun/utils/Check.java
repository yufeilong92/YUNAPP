package com.lawyee.yj.subaoyun.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lawyee.yj.subaoyun.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yfl on 2016/11/8 10:17.
 *
 * @purpose:校验问题
 */

public class Check {
    /**
     * 判断手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        String phone = "^1[234578]\\d{9}$";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 校验真是姓名
     * @param user
     * @return
     */
    public static boolean CheckUser(String user){
        boolean isValid = true;
        CharSequence inputStr = user;
        String name="^[u4e00-u9fa5]{0,}$";
        Pattern pattern = Pattern.compile(name);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = false;
        }
        return isValid;
    }

    /**
     *  校验身份证
     * @param user
     * @return
     */
    public static boolean CheckIDCard(String user){
            String regx = "[0-9]{17}x";
            String reg1 = "[0-9]{15}";
            String regex = "[0-9]{18}";
            return user.matches(regx) || user.matches(reg1) || user.matches(regex);
    }

    public ProgressDialog showdialogs(Context context,String title,String msg){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        return dialog;
    }
    public void setDialog(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflate = inflater.inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setTitle("5555555555")
          .setView(inflate)
          .create();
    }
}
