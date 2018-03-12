package com.lawyee.yj.subaoyun.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.lawyee.yj.subaoyun.R;

/**
 * Created by yfl on 2016/10/26 11:48.
 *
 * @purpose:
 */
  /*  private void SendCode() {
        String trim = mUserPhone.getText().toString().trim();
        Log.e(TAG, "SendCode:======== " + trim);
        if (trim.equals("")){
            T.showShort(RegisterActivity.this,getResources().getString(R.string.register_page_phone));
        }
        boolean mobileNO = new Check().isPhoneNumberValid(trim);
        if (mobileNO) {
            mBtSendCode.setClickable(true);
            new TimeCounUtil(this, 60000, 1000, mBtSendCode).start();
            sendcodes(trim);
        } else if (!trim.equals("")) {
            T.showShort(RegisterActivity.this, "请输入正确的手机号码");
        }
    }
*/

public class TimeCounUtil extends CountDownTimer {
    private Activity mActivity;
    private Button btn;//按钮
    private Context context;
    public TimeCounUtil(Context context, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.context=context;
    }



    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);
        btn.setTextSize(12);
        btn.setText(" "+"("+millisUntilFinished / 1000+")秒后可重新发送"+" ");
        btn.setTextColor(Color.WHITE);
//        btn.setBackground(context.getResources().getDrawable(R.drawable.showtimes));
        btn.setBackground(context.getResources().getDrawable(R.drawable.code_buttons));
//        btn.setBackgroundColor(Color.GRAY);
        SpannableString span = new SpannableString(btn.getText().toString());
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btn.setTag(span);

    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText(" "+"重新获取验证码"+" ");
        btn.setTextSize(12);
        btn.setClickable(true);
        btn.setTextColor(Color.RED);
//        btn.setBackground(context.getResources().getDrawable(R.drawable.shoutime));
        btn.setBackground(context.getResources().getDrawable(R.drawable.code_button));
//        btn.setBackgroundColor(Color.WHITE);
    }
}
