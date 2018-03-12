package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.Check;
import com.lawyee.yj.subaoyun.utils.InputUtil;
import com.lawyee.yj.subaoyun.utils.T;
import com.lawyee.yj.subaoyun.utils.TimeCounUtil;
import com.lawyee.yj.subaoyun.utils.isEmpty;
import com.lawyee.yj.subaoyun.vo.ActionVO;
import com.lawyee.yj.subaoyun.vo.ErrorJson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:26:16:26
 * @Purpose :注册界面
 */


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    public static final OkHttpClient client = new OkHttpClient();
    private Button mBtRegister;
    private Button mBtSendCode;
    private TextView mTvLogin;
    private EditText mUserName;
    private EditText mUserIdCard;
    private EditText mUserPassword;
    private EditText mUserPassWords;
    private EditText mUserPhone;
    private EditText mUserCode;
    private String name;
    private String IDNumber;
    private String password;
    private String passwords;
    private String mobile;
    private String musercode;
    private TextView tvService;
    private TextView mRegisterTv;
    private Dialog progressDialog;

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity__register);
        findviewID();
        initView();
    }

    private void initView() {
        String str = this.getResources().getString(R.string.register_Agreement);
        String tvServiec = this.getResources().getString(R.string.register_agreements);
        SpannableString spanstr = new SpannableString(tvServiec);
        spanstr.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
                ds.setUnderlineText(true);

            }

            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, ProtocolActivity.class));

            }
        }, 0, tvServiec.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvService.setHighlightColor(Color.TRANSPARENT);
        tvService.append("\u3000\u3000"+str);
        tvService.append(spanstr);
        tvService.setMovementMethod(LinkMovementMethod.getInstance());


    }

    protected void findviewID() {
        tvService = (TextView) findViewById(R.id.agreement);
        mUserName = (EditText) findViewById(R.id.sby_register_user_name);
        mUserIdCard = (EditText) findViewById(R.id.sby_register_user_IDcard);
        mUserPassword = (EditText) findViewById(R.id.sby_register_user_password);
        mUserPassWords = (EditText) findViewById(R.id.sby_resgister_user_password2);
        mUserPhone = (EditText) findViewById(R.id.sby_resgister_user_phone);
        View mRegister = findViewById(R.id.activity__register);
        mRegisterTv = (TextView) findViewById(R.id.sby_register_register);
        mUserCode = (EditText) findViewById(R.id.sby_register_user_code);

        mBtSendCode = (Button) findViewById(R.id.sby_register_send_code);
        mBtSendCode.setClickable(false);
        mTvLogin = (TextView) findViewById(R.id.sby_register_login);
        new InputUtil().Screen(mRegister, RegisterActivity.this, mUserName);
        mBtSendCode.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mRegisterTv.setOnClickListener(this);
        setListener();


    }

    public void getAllValues() {
        name = mUserName.getText().toString().trim();
        IDNumber = mUserIdCard.getText().toString().trim();
        password = mUserPassword.getText().toString().trim();
        passwords = mUserPassWords.getText().toString().trim();
        mobile = mUserPhone.getText().toString().trim();
        musercode = mUserCode.getText().toString().trim();

    }

    private void setListener() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sby_register_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
            case R.id.sby_register_send_code:
                // TODO: 2016/10/21 做发送验证码
                SendCode();
                break;
            case R.id.sby_register_register:
                Register();
                break;
            default:
                break;
        }

    }

    private void SendCode() {
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

    protected void Register() {

        getAllValues();
        if (name.equals("")) {
            T.showShort(RegisterActivity.this, getResources().getString(R.string.register_page_name));
            return;
        } else {

        }
        if (IDNumber.equals("")) {
            T.showShort(RegisterActivity.this, getResources().getString(R.string.register_page_ID));
            return;
        } else {
            boolean b1 = new Check().CheckIDCard(IDNumber);
            if (b1) {
            } else {
                T.showShort(RegisterActivity.this, "请输入正确的身份证");
            }
        }
        if (passwords.equals("")) {
            T.showShort(RegisterActivity.this, getResources().getString(R.string.register_page_pass));
            return;
        } else {
            new isEmpty().isEmpty(RegisterActivity.this, password, passwords);
        }
        if (mobile.equals("")) {
            T.showShort(RegisterActivity.this, getResources().getString(R.string.register_page_phone));
            return;
        } else {

        }
        if (musercode.equals("")) {
            T.showShort(RegisterActivity.this, getResources().getString(R.string.register_page_agreements));
            return;
        } else {
            Subscriber<String> subscriber = new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    progressDialog.dismiss();
                    Log.d(TAG, "onCompleted: ");
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    Log.e(TAG, "onError: " + e.toString());
                }

                @Override
                public void onNext(String s) {

                    Gson gson = new Gson();
                    ErrorJson errorJson = gson.fromJson(s, ErrorJson.class);
                    if (errorJson.getCode() == 0) {
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        Log.e(TAG, "onNext: " + errorJson.getMsg());
                        T.showShort(RegisterActivity.this, errorJson.getMsg());
                        return;
                    }

                }
            };
            Observable.just(ActionVO.LOGINSERVICE)
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {

                            FormBody formBody = new FormBody.Builder()
                                    .add("name", name)
                                    .add("IDNumber", IDNumber)
                                    .add("password", password)
                                    .add("mobile", mobile)
                                    .add("verifycode", musercode)
                                    .build();

                            Request request = new Request.Builder()
//                                    .url(s + "zclist?" + "name=" + name + "&IDNumber=" + IDNumber + "&password=" + password + "&mobile=" + mobile+"verifycode="+musercode)
                                    .url(s + "zclist")
                                    .post(formBody)
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                if (!response.isSuccessful()) {
                                    throw new IOException("Unexpected code" + response);
                                } else {
                                    return response.body().string();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new Error(e);
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {

                            progressDialog = new ShowDialogs().createLoadingDialog(RegisterActivity.this, "注册中.....");
                            progressDialog.show();
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    private void sendcodes(final String phone) {

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.toString());
            }

            @Override
            public void onNext(String s) {
                Gson gson = new Gson();
                ErrorJson errorJson = gson.fromJson(s, ErrorJson.class);
                T.showShort(RegisterActivity.this, errorJson.getMsg());
            }
        };

        Observable.just(ActionVO.LOGINSERVICE)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Request request = new Request.Builder()
                                .url(s + "fayzm?mobile=" + phone)
                                .build();
                        Log.d(TAG, "call: " + s + "fayzm?mobile=" + phone);
                        try {
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                return response.body().string();
                            } else {
                                throw new IOException("Unexception code" + response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new Error(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
