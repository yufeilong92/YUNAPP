package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.InputUtil;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;
import com.lawyee.yj.subaoyun.utils.TextUtils;
import com.lawyee.yj.subaoyun.vo.ActionVO;
import com.lawyee.yj.subaoyun.vo.ErrorJson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/16 16:52:16:52
 * @Purpose :提交密码
 */


public class ComplateActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextView2;
    private TextView mSbyResetpageOnnextCompletes;
    private EditText mSbyResetpageOnnextPassword;
    private EditText mSbyResetpageOnnextPasswords;
    private Button mSbyResetpageOnnextSubmit;
    private RelativeLayout mSbyResetpageOnnextRelative;
    private LinearLayout mLinearlayout;
    private LinearLayout mActivityComplate;

    private String password;
    private String passwords;
    private Dialog mDialog;
    private String phone;



    @Override
    protected void initContenView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_complate);
        initView();
        initData();
    }


    private void initView() {

        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView2.setOnClickListener(this);
        mSbyResetpageOnnextCompletes = (TextView) findViewById(R.id.sby_resetpage_onnext_completes);
        mSbyResetpageOnnextCompletes.setOnClickListener(this);
        mSbyResetpageOnnextPassword = (EditText) findViewById(R.id.sby_resetpage_onnext_password);
        mSbyResetpageOnnextPassword.setOnClickListener(this);
        mSbyResetpageOnnextPasswords = (EditText) findViewById(R.id.sby_resetpage_onnext_passwords);
        mSbyResetpageOnnextPasswords.setOnClickListener(this);
        mSbyResetpageOnnextSubmit = (Button) findViewById(R.id.sby_resetpage_onnext_submit);
        mSbyResetpageOnnextSubmit.setOnClickListener(this);
        mSbyResetpageOnnextRelative = (RelativeLayout) findViewById(R.id.sby_resetpage_onnext_relative);
        mSbyResetpageOnnextRelative.setOnClickListener(this);
        mLinearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        mLinearlayout.setOnClickListener(this);
        mActivityComplate = (LinearLayout) findViewById(R.id.activity_complate);
        mActivityComplate.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        if (phone == null) return;
        new InputUtil().Screen(mLinearlayout, ComplateActivity.this, mSbyResetpageOnnextPassword);
        getAllValues();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sby_resetpage_onnext_completes:
                submit();
                break;
            case R.id.sby_resetpage_onnext_submit:
                submit();
                break;
        }
    }

    private void submit() {
        getAllValues();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(passwords)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (password.equals(passwords)) {

                if (password.length() < 6) {
                    Toast.makeText(ComplateActivity.this, "密码不能小于6位", Toast.LENGTH_SHORT).show();
                } else {
                    ChangePassWord();
                }
            } else {
                Toast.makeText(ComplateActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void ChangePassWord() {
            showDialog();
            String url = new ActionVO().LOGINSERVICE + "updatePassword";
            FormBody formBody = new FormBody.Builder()
                    .add("mobile", phone)
                    .add("pass", password)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            new ReSetActivity().client.newCall(request).enqueue(callback);
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            setResults(false, e.getMessage());
        }


        @Override
        public void onResponse(Call call, Response response) throws IOException {
            setResults(true, response.body().string());
        }
    };

    private void setResults(final boolean success, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    Gson gson = new Gson();
                    ErrorJson errorJson = gson.fromJson(message, ErrorJson.class);
                    Toast.makeText(ComplateActivity.this, "" + errorJson.getMsg(), Toast.LENGTH_SHORT).show();
                    int code = errorJson.getCode();
                    switch (code) {
                        case 0:
                            startActivity(new Intent(ComplateActivity.this, LoginActivity.class));
                            break;
                        default:
                            mDialog.dismiss();
                            break;
                    }

                } else {

                    Toast.makeText(ComplateActivity.this, "系统维护中...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog() {
        mDialog = new ShowDialogs().createLoadingDialog(ComplateActivity.this, "数据提交中...");
        mDialog.show();
    }

    public void getAllValues() {
        password = mSbyResetpageOnnextPassword.getText().toString().trim();
        passwords = mSbyResetpageOnnextPasswords.getText().toString().trim();


    }


}
