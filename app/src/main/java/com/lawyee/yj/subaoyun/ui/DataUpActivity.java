package com.lawyee.yj.subaoyun.ui;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.adapter.UploadImageAdapter;
import com.lawyee.yj.subaoyun.utils.LGImgCompressor;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;
import com.lawyee.yj.subaoyun.utils.T;
import com.lawyee.yj.subaoyun.vo.ActionVO;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yfl
 * email: yufeilong92@163.com
 *
 * @purpose:图片上传界面
 */

public class DataUpActivity extends BaseActivity {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private UploadImageAdapter mUploadImageAdapter;
    private static final int REQUEST_CODE = 732;
    public static final int NODATA = 111;
    private GridView noScrollgridview;
    private ArrayList<String> mResults;
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<String> mData;
    private Dialog dialog;
    private String type;
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    public static OkHttpClient client =
            new OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .build();
    private Bundle parameter;
    private String mobile;
    private String login_id;
    private String staff_id;
    ArrayList<String> mList;


    //permission code
    private final int RC_STORAGE = 1; //相机权限申请

    //request code
    private final int REQUEST_APPSET = 1;       //跳转appset的返回

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_data_up);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        parameter = intent.getExtras();
        mobile = parameter.getString("mobile");
        login_id = parameter.getString("login_id");
        staff_id = parameter.getString("Staff_id");

        Log.e(TAG, "initContenView: " + type + "sssss" + mobile + "|||" + login_id + ">>" + staff_id);
        initView();
        clearList();
        controlView();
    }


    public void initView() {

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    public void clearList() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ActionVO.PREVIEWPHOTO)) {
                    mData = intent.getStringArrayListExtra("Images");
                    mResults.clear();
                    mResults = mData;
                    mUploadImageAdapter.setData(mData);
                    mUploadImageAdapter.notifyDataSetChanged();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionVO.NODATA);
        intentFilter.addAction(ActionVO.PREVIEWPHOTO);
        registerReceiver(broadcastReceiver, intentFilter);

        if (mResults == null) {
            mResults = new ArrayList<>();
            mUploadImageAdapter = new UploadImageAdapter(this, mResults);
            noScrollgridview.setAdapter(mUploadImageAdapter);
        } else {
            mResults.clear();
            mUploadImageAdapter.setData(mResults);
        }
    }

    public void controlView() {
        noScrollgridview.setOnItemClickListener(getItemClickListener());

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener getItemClickListener() {
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mResults.size()) {
///调用相机权限
                    doReadFilePermisssion();

                } else {
                    Intent intent = new Intent(DataUpActivity.this, ProviewPhotosAvtivity.class);
                    if (mResults == null) return;
                    intent.putStringArrayListExtra("content", mResults);
                    intent.putExtra("ID", position);
                    startActivity(intent);

                }
            }
        };
        return onItemClickListener;
    }

    /**
     * 调用调用相机
     */
    private void CallCamera() {
        // start multiple photos selector
        Intent intent = new Intent(DataUpActivity.this, ImagesSelectorActivity.class);
        // max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 100);
        // min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
        // show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
        // pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
        // start the selector
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
                mUploadImageAdapter.setData(mResults);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_APPSET) {         //如果需要跳转系统设置页后返回自动再次检查和执行业务 如果不需要则不需要重写onActivityResult
            doReadFilePermisssion();
        }
    }

    public void onSubmit(View view) {
        if (mResults != null) {
            if (mResults.size() <= 0) {
                T.showShort(DataUpActivity.this, "请提交资料");
            } else {
                SmallImage(mResults);
                sleepThread();
            }
        }
    }

    /**
     * 由于图片压缩需要时间，上传线程延时一秒
     */
    private void sleepThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mList.size()<0){
                    sleepThread();
                }else {
                    submitdatas(mList);
                }
            }
        },1000);
    }

    private void showDialog() {
        dialog = new ShowDialogs().createLoadingDialog(DataUpActivity.this, "上传中....");
        dialog.show();
    }

    private void submitdatas(final ArrayList<String> mImgUrls) {
        //修改把mImgUrls没有压缩的经行压缩保存mImgUrlss里面
        if (mImgUrls.size()<0)
            return;
        for (int i = 0; i < mList.size(); i++) {
            Log.e(TAG, "submitdatas: " + mImgUrls.get(i).toString());
        }
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.toString());
                Toast.makeText(getApplicationContext(), "服务器繁忙...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext: =============");
                new ShowDialogs().setDialogs(DataUpActivity.this, "提交成功");
                dialog.dismiss();
                Intent intent = new Intent(DataUpActivity.this, MainActivity.class);
                intent.putExtras(parameter);
                startActivity(intent.putStringArrayListExtra("content", mImgUrls));
            }
        };

        Observable.just(ActionVO.LOGINSERVICE)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                        for (int i = 0; i < mImgUrls.size(); i++) {
                            File file = new File(mImgUrls.get(i));
                            if (file != null) {
                                builder.addFormDataPart("imgfile" + i, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                            }
                        }
                        RequestBody requestBody = builder.build();
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(s + "iostijiao?bdlx=" + type + "&login_id=" + login_id + "&staff_id=" + staff_id + "&mobile=" + mobile)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                return response.body().string();
                            } else {
                                throw new IOException("UNexception code" + response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new Error(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        showDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * +
     * <p>
     * 压缩图片
     *
     * @param mImgUrls
     */
    private void SmallImage(ArrayList<String> mImgUrls) {
        showDialog();
        mList = new ArrayList<String>();

        for (int i = 0; i < mImgUrls.size(); i++) {
            LGImgCompressor.getInstance(this).withListener(new LGImgCompressor.CompressListener() {

                private String absolutePath;

                @Override
                public void onCompressStart() {

                }

                @Override
                public void onCompressEnd(LGImgCompressor.CompressResult imageOutPath) {
                    if (imageOutPath.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR) {//压缩失败
                        return;
                    }
                    File file = new File(imageOutPath.getOutPath());
                    mList.add(file.getAbsolutePath());
                    absolutePath = file.getAbsolutePath();
                    Log.e(TAG, "onCompressEnd: " + absolutePath + "============" + mList.size());
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                        float imageFileSize = file.length() / 1024f;
                        Log.e("================", "onCompressEnd: " + "image info width:" + bitmap.getWidth() + " \nheight:" + bitmap.getHeight() +
                                " \nsize:" + imageFileSize + "kb" + "\nimagePath:" + file.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).starCompress(mImgUrls.get(i), 600, 800, 100);

        }
    }

    //动态权限申请
    private void doReadFilePermisssion() {
        String[] perms = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};

        performCodeWithPermission(getString(R.string.permission_rc_storage), RC_STORAGE, perms,
                new PermissionCallback() {
                    @Override
                    public void hasPermission() {

                        CallCamera();
                    }

                    @Override
                    public void noPermission(Boolean hasPermanentlyDenied) {
                        if(hasPermanentlyDenied) {
                            //只是提供跳转系统设置的提示 系统返回后不做检查处理
//                            alertAppSetPermission(getString(R.string.permission_storage_deny_again));

                            //如果需要跳转系统设置页后返回自动再次检查和执行业务
                            alertAppSetPermission(getString(R.string.permission_storage_deny_again), REQUEST_APPSET);
                        }
                    }
                });
    }

}
