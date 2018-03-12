package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lawyee.yj.subaoyun.utils.Check;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;
import com.lawyee.yj.subaoyun.vo.ErrorJson;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.InputUtil;
import com.lawyee.yj.subaoyun.utils.T;
import com.lawyee.yj.subaoyun.utils.TimeCounUtil;
import com.lawyee.yj.subaoyun.vo.ActionVO;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Purpose :密码修改
 */


public class ReSetActivity extends BaseActivity implements View.OnClickListener {

    public OkHttpClient client = new OkHttpClient();
    private EditText mGetCode;
    private EditText mUserName;

    private Button mSubmit;
    private TextView mComplete;
    private Button mSendCode;

    private String[] strings = new String[4];
    private String getCode;
    private String username;
    private Dialog mDialog;


    @Override
    protected void initContenView(Bundle savedInstanceState) {

        setContentView(R.layout.activity_re_set);
        findviewid();

    }


    private void findviewid() {
        final View mR = findViewById(R.id.sby_resetpage_relative);
        mComplete = (TextView) findViewById(R.id.sby_resetpage_completes);
        mComplete.setOnClickListener(this);
        mGetCode = (EditText) findViewById(R.id.sby_resetpage_code);
        mUserName = (EditText) findViewById(R.id.sby_resetpage_username);
//        mSubmit = (Button) findViewById(R.id.sby_resetpage_onnext);
//        mSubmit.setOnClickListener(this);
        mSendCode = (Button) findViewById(R.id.sby_resetpage_sendcode);
        mSendCode.setOnClickListener(this);
        new InputUtil().Screen(mR, ReSetActivity.this, mGetCode);
        getAllValues();
    }

    private void getAllValues() {
        getCode = mGetCode.getText().toString().trim();
        username = mUserName.getText().toString().trim();

    }

    private void submit() {
        getAllValues();
        if (username.equals("")) {
            T.showShort(ReSetActivity.this, "请输入手机号码");
            return;
        }
        if (getCode.equals("")) {
            T.showShort(ReSetActivity.this, "请输入验证码");
            return;
        } else {
            CanSendCode();

        }
    }

    private void CanSendCode() {
        mDialog = new ShowDialogs().createLoadingDialog(ReSetActivity.this, "加载中...");
        mDialog.show();
        if(username!=null){
        FormBody formBody = new FormBody.Builder()
                .add("mobile", username)
                .add("verifyCode", getCode)
                .build();
        Log.e(TAG, "CanSendCode: "+getCode+"========"+username );
     String url=new ActionVO().LOGINSERVICE+"forget";
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Log.e(TAG, "CanSendCode: "+url );
        client.newCall(request).enqueue(callbacks);
        }
    }
    private Callback callbacks = new Callback() {
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
                    Log.e(TAG, "run:========== "+message );
                    ErrorJson errorJson = gson.fromJson(message, ErrorJson.class);
                    int code = errorJson.getCode();
                    switch (code) {
                        case 0:
                            mDialog.dismiss();
                            StartActivity();
                            break;
                        case 1:
                             mDialog.dismiss();
                            T.showShort(ReSetActivity.this, errorJson.getMsg());
                            break;
                        default:
                            break;
                    }
                } else {
                    mDialog.dismiss();
                    T.showShort(ReSetActivity.this, "系统维护中....");
                    Log.e(TAG, "run: " + message);
                    return;
                }
            }
        });
    }

    private void StartActivity() {
        Intent intent = new Intent(ReSetActivity.this, ComplateActivity.class);
        intent.putExtra("phone",username);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sby_resetpage_completes:
                submit();
                break;
     /*       case R.id.sby_resetpage_onnext:
                submit();
                break;*/
            case R.id.sby_resetpage_sendcode:
                getAllValues();
                sendphont();
                break;
            default:
                break;
        }
    }

    private void sendphont() {
        String trim = mUserName.getText().toString().trim();
        if (trim.equals("")){
            T.showShort(ReSetActivity.this,getResources().getString(R.string.register_page_phone));
        }
        boolean mobileNO = new Check().isPhoneNumberValid(trim);
        if (mobileNO) {
            mSendCode.setClickable(true);
            new TimeCounUtil(this, 60000, 1000, mSendCode).start();
            sendCode();
            Log.e(TAG, "SendCode:>>>>>>> " + trim);
        } else if (!trim.equals("")) {
            T.showShort(ReSetActivity.this, "请输入正确的手机号码");
        }
    }

    private void sendCode() {
        String url = new ActionVO().LOGINSERVICE + "updatepass?mobile=" + username;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.e(TAG, "sendCode: " + url);
        client.newCall(request).enqueue(callback);


    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            setUsecces(false, e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            setUsecces(true, response.body().string());
        }
    };

    private void setUsecces(final boolean isuseccess, final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isuseccess) {
                    Gson gson = new Gson();
                    Log.e(TAG, "run: " + result);
                    ErrorJson errorJson = gson.fromJson(result, ErrorJson.class);
                    Toast.makeText(ReSetActivity.this, "" + errorJson.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "请求失败..." + result);
                    Toast.makeText(ReSetActivity.this, "系统维修中....", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
