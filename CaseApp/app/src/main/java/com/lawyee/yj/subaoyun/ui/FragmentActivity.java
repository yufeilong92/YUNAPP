package com.lawyee.yj.subaoyun.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lawyee.yj.subaoyun.Fragment.AboutFragment;
import com.lawyee.yj.subaoyun.Fragment.AboutusFragment;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.loge;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:28:16:28
 * @Purpose :版本界面
 */

public class FragmentActivity extends BaseActivity {

    public static final String TAG = "" + FragmentActivity.class.getName();

    private String version;
    private String versionName;

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fragment);

//        getversion()
        VersionUpData();
        TextView mTextview = (TextView) findViewById(R.id.sby_fragment_title);
        Intent intent = getIntent();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Log.d(TAG, "===========当前版本号=====: "+versionName);
        AboutFragment aboutFragment = new AboutFragment().newInstance("",versionName);
//        aboutFragment.newInstance("",versionName);
        AboutusFragment aboutusFragment = new AboutusFragment();
        Fragment[] fragments = new Fragment[2];
        fragments[0] = aboutFragment;
        fragments[1] = aboutusFragment;


        String content = intent.getStringExtra("content");
        if (content.equals(SetttingActivity.HELPFRAGMNETTAG)) {
            mTextview.setText(this.getResources().getString(R.string.Help_explain));
            fragmentTransaction.add(R.id.fragment_layou, fragments[0]);
            fragmentTransaction.commit();
        } else if (content.equals(SetttingActivity.ABOUTUSFRAGMNETTAG)) {
            mTextview.setText(this.getResources().getString(R.string.About_us));
            fragmentTransaction.add(R.id.fragment_layou, fragments[1]);
            fragmentTransaction.commit();
        }
    }

    private void VersionUpData() {
        try {
            versionName = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (version==null){
            version=versionName;
        }else {
            if (!versionName.equals(version)){
                versionName=version;
            }
        }


    }

    /**
     * 获取版本号
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
                    loge.e(TAG, message);
                    Toast.makeText(FragmentActivity.this, "系统维护中...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

}
