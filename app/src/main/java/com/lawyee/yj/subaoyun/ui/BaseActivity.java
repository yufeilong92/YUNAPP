package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.serviceImpl.NetBroadcastReceiver;
import com.lawyee.yj.subaoyun.serviceImpl.NetUtils;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by yfl on 2016/10/21 13:40.
 *
 * @purpose:activity基类
 */

public abstract class BaseActivity  extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt,EasyPermissions.PermissionCallbacks{
    protected static final String TAG =BaseActivity.class.getSimpleName().toString();

    public static NetBroadcastReceiver.NetEvevt evevt;

    /**
     * 网络类型
     * @param savedInstanceState
     */
    private  int netMobile;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ApplicationSet.getInstance().addActivity(this);

        evevt = this;
        inspectNet();
        initContenView(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);}
        ActionBar bar = getSupportActionBar();
        bar.hide();


    }


    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtils.getNetWorkState(BaseActivity.this);
        return isNetConnect();

//         if (netMobile == NetUtil.NETWORK_WIFI) {
//         System.out.println("inspectNet：连接wifi");
//         } else if (netMobile == NetUtil.NETWORK_MOBILE) {
//         System.out.println("inspectNet:连接移动数据");
//         } else if (netMobile == NetUtil.NETWORK_NONE) {
//         System.out.println("inspectNet:当前没有网络");
//
//         }
    }
    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
        isNetConnect();

    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == NetUtils.NETWORK_WIFI) {
            return true;
        } else if (netMobile == NetUtils.NETWORK_MOBILE) {
            return true;
        } else if (netMobile == NetUtils.NETWORK_NONE) {
            return false;

        }
        return false;
    }

    public Dialog showDialoges(String msg){
        Dialog dialog = new ShowDialogs().createLoadingDialog(BaseActivity.this, msg);
        return dialog;
    }
    protected abstract void initContenView(Bundle savedInstanceState) ;
    public void onHomeClick(View view){
        finish();
    }


    /**
     * 权限
     */

    private Map<Integer, PermissionCallback> mPermissonCallbacks = new HashMap<>();

    /**
     * 权限回调接口
     */
    protected interface PermissionCallback {
        /**
         * 成功获取权限
         */
        void hasPermission();

        /**
         * 没有权限
         * @param hasPermanentlyDenied 是否点击不再询问，被设置为永久拒绝权限
         */
        void noPermission(Boolean hasPermanentlyDenied);
    }

    /**
     * 请求权限操作
     * @param rationale 请求权限提示语
     * @param permissionRequestCode 权限requestCode
     * @param perms 申请的权限列表
     * @param callback 权限结果回调
     */
    protected void performCodeWithPermission(@NonNull String rationale,
                                             final int permissionRequestCode, @NonNull String[] perms, @NonNull PermissionCallback callback) {
        if (EasyPermissions.hasPermissions(this, perms)) {
            callback.hasPermission();
        } else {
            mPermissonCallbacks.put(permissionRequestCode, callback);
            EasyPermissions.requestPermissions(this, rationale, permissionRequestCode, perms);
        }
    }

    /**
     * 跳转设置弹框 建议在权限被设置为不在询问时弹出 提示用户前往设置页面打开权限
     * @param tips 提示信息
     */
    protected void alertAppSetPermission(String tips) {
        new AppSettingsDialog.Builder(this, tips)
                .setTitle(getString(R.string.permission_deny_again_title))
                .setPositiveButton(getString(R.string.permission_deny_again_positive))
                .setNegativeButton(getString(R.string.permission_deny_again_nagative), null)
                .build()
                .show();
    }

    /**
     * 跳转设置弹框 建议在权限被设置为不在询问时弹出 提示用户前往设置页面打开权限
     * @param tips 提示信息
     * @param requestCode 页面返回时onActivityResult的requestCode
     */
    protected void alertAppSetPermission(String tips, int requestCode) {
        new AppSettingsDialog.Builder(this, tips)
                .setTitle(getString(R.string.permission_deny_again_title))
                .setPositiveButton(getString(R.string.permission_deny_again_positive))
                .setNegativeButton(getString(R.string.permission_deny_again_nagative), null)
                .setRequestCode(requestCode)
                .build()
                .show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        PermissionCallback callback = mPermissonCallbacks.get(requestCode);
        if(callback != null) {
            callback.hasPermission();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        PermissionCallback callback = mPermissonCallbacks.get(requestCode);
        if(callback != null) {
            if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                callback.noPermission(true);
            } else {
                callback.noPermission(false);
            }
        }
    }
}
