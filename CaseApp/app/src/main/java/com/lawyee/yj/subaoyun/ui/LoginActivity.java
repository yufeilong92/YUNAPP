package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.InputUtil;
import com.lawyee.yj.subaoyun.utils.SaveFile.SharedPreUtil;
import com.lawyee.yj.subaoyun.utils.SaveFile.UserEntity;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;
import com.lawyee.yj.subaoyun.utils.T;
import com.lawyee.yj.subaoyun.vo.ActionVO;
import com.lawyee.yj.subaoyun.vo.ErrorJson;
import com.lawyee.yj.subaoyun.vo.LoginData;

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
 * @Time :2016/12/19 16:28:16:28
 * @Purpose :登陆界面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    public static OkHttpClient client = new OkHttpClient();

    private String[] mDatas = new String[10];
    private EditText mloginUserName;
    private EditText mloginUserPass;
    private Button mBtlogin;
    private TextView mTvRegister;
    private TextView mTvForget;
    private ImageView mIvBack;
    private View whole;
    private String username;
    private String pass;
    private Dialog progressDialog;
    private Dialog dialog;
    boolean isave = false;

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity__login);
        //初始化工具
        SharedPreUtil.initSharedPreference(getApplicationContext());
        findviewID();
        initData();
    }

    /**
     * 密码的回调
     */
    private void initData() {
        if (SharedPreUtil.getInstance().getUser() != null) {
            UserEntity user = SharedPreUtil.getInstance().getUser();
            mloginUserName.setText(user.getUserName());
            mloginUserPass.setText(user.getPassword());
        }


    }

    private void findviewID() {

        whole = findViewById(R.id.activity__login);
        mloginUserName = (EditText) findViewById(R.id.sby_login_username);
        mloginUserPass = (EditText) findViewById(R.id.sby_login_password);
        mBtlogin = (Button) findViewById(R.id.sby_login_login_bt);
        mTvRegister = (TextView) findViewById(R.id.sby_login_register);
        mTvForget = (TextView) findViewById(R.id.sby_login_forger);
        Button mline = (Button) findViewById(R.id.sby_login_login_bt);
        LinearLayout mRoot = (LinearLayout) findViewById(R.id.root);
        mBtlogin.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvForget.setOnClickListener(this);
        //隐藏键盘
        new InputUtil().Screen(whole, LoginActivity.this, mloginUserPass);
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equals("HUAWEI")){
            controlKeyboardLayouts(mRoot, mline);

        }else {
            controlKeyboardLayout(mRoot,mline);
        }
    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    Log.d("========", "键盘隐藏布局测试的总高度" + rootInvisibleHeight);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    Log.d("========", "计算root滚动高度: " + srollHeight + "location[1]==" + location[1] + "scrollToView.getHeight())===" + scrollToView.getHeight() + " rect.bottom==" + rect.bottom);

                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     *
     * @param root
     * @param scrollToView
     */
    private void controlKeyboardLayouts(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom-108;
                Log.d("========", "键盘隐藏布局测试的总高度" + rootInvisibleHeight);
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    Log.d("========", "计算root滚动高度: " + srollHeight + "location[1]==" + location[1] + "scrollToView.getHeight())===" + scrollToView.getHeight() + " rect.bottom==" + rect.bottom);
                    root.scrollTo(0, srollHeight);

                } else {
                    //键盘隐藏

                    root.scrollTo(0, 0);

                }
            }
        });
    }

    private void getNetwork() {
        RequestNetWork();
        setDialog();
    }

    private void RequestNetWork() {
        FormBody builder = new FormBody.Builder()
                .add("username", username)
                .add("pass", pass)
                .build();
        Request request = new Request.Builder()
                .url(ActionVO.LOGINSERVICE + "dllist?" + "username=" + username + "&pass=" + pass)
                .build();
        Log.d(TAG, "postJson: " + ActionVO.LOGINSERVICE + "dllist?" + "username=" + username + "&pass=" + pass);
        client.newCall(request).enqueue(callback);
    }

    private void setDialog() {
        progressDialog = new ShowDialogs().createLoadingDialog(LoginActivity.this, "正在登陆......");
        progressDialog.show();
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            setResult(e.getMessage(), false);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            setResult(response.body().string(), true);
        }
    };

    private void setResult(final String msg, final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    Log.d(TAG, "run: 请求成功");
                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    int length = msg.length();
                    Log.e(TAG, "run: " + msg + length);
                    if (length >= 100) {
                        LoginData json = gson.fromJson(msg, LoginData.class);
                        int code = json.getCode();
                        if (code == 0) {
                            startActivitys(1, json);
                        }
                    } else {
                        ErrorJson errorJson = gson.fromJson(msg, ErrorJson.class);
                        T.showShort(LoginActivity.this, errorJson.getMsg());
                    }

                } else {
                    progressDialog.dismiss();
                    Log.e(TAG, "run:请求失败" + msg);
                    Toast.makeText(getApplicationContext(), "服务器繁忙。。。", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void startActivitys(int id, LoginData str) {
        SavaAccount();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Staff_id", str.getStaff_id());
        bundle.putString("login_id", str.getMsg().getLogin_id());
        bundle.putString("mobile", str.getMobile());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 保存账号
     */
    private boolean SavaAccount() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(pass);
        userEntity.setUserName(username);
        SharedPreUtil.getInstance().putUser(userEntity);

        return isave = true;
    }

    private void getAllValue() {
        username = mloginUserName.getText().toString().trim();
        pass = mloginUserPass.getText().toString().trim();
        if (username.equals("")) {
            T.showShort(LoginActivity.this, "请输入手机号");
        } else if (pass.equals("")) {
            T.showShort(LoginActivity.this, "请输入密码");
        } else {
            getNetwork();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sby_login_login_bt:
                getAllValue();
                break;
            case R.id.sby_login_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.sby_login_forger:
                startActivity(new Intent(LoginActivity.this, ReSetActivity.class));
                break;
            default:
                break;
        }
    }
}
