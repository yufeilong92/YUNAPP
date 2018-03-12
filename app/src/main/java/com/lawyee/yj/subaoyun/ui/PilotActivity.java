package com.lawyee.yj.subaoyun.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.NetworkManager;
import com.lawyee.yj.subaoyun.utils.T;
import com.lawyee.yj.subaoyun.utils.loge;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:26:16:26
 * @Purpose :引导界面（首页）
 */



public class PilotActivity extends FragmentActivity implements View.OnClickListener{

    private TextView mlogin;
    private TextView mRegister;

    private String First ="1";
    @Override
    protected void onStart() {
        super.onStart();

       new NetworkManager().judgeNetwork(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_pilot);
        mlogin = (TextView) findViewById(R.id.home_login);
        mRegister = (TextView) findViewById(R.id.home_register);
        mlogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        { case R.id .home_login:
            startActivity(new Intent(PilotActivity.this,LoginActivity.class));
            break;
            case R.id.home_register:
                startActivity(new Intent(PilotActivity.this,RegisterActivity.class));
                break;
            default:
                break;
        }
    }
    private long firstTime;
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            T.showShort(this, R.string.press_again_backrun);
        }
    }
    /**
     * 获取网络版本号
     */
    private void getversion() {
        String url = "";//版本路径
        Request request = new Request.Builder()
                .url(url)
                .build();
        new LoginActivity().client.newCall(request).enqueue(callback);

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

    private void setResults( final boolean success, final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    // TODO: 2016/12/20 解析数据传递参数

                } else {
                    loge.e("网络错误原因",message);
                    Toast.makeText(PilotActivity.this, "系统维护中...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取本地版本号
     * @return
     * @throws Exception
     */
    private String getVersion() throws Exception{

        PackageManager packageManager =getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String versionName = packageInfo.versionName;
        return versionName;
    }

}
