package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lawyee.yj.subaoyun.adapter.PictureShowAdapter;
import com.lawyee.yj.subaoyun.adapter.TimeaxisAdapter;
import com.lawyee.yj.subaoyun.vo.ProgressVo;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.vo.ActionVO;
import com.lawyee.yj.subaoyun.vo.TimeAxisVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * @Purpose :进度查询界面
 */



public class ProgressActivity extends BaseActivity {
    private static final String TAG = "ProgressActivity";
    private OkHttpClient client = new OkHttpClient();
    private Dialog dialog;
    private ArrayList<String> mResults = null;
    private GridView mGridView;
    private ListView mListView;
    private PictureShowAdapter mImagerAdapter;
    private static final String PROGRESSBAR = "progressbar";
    private String oid;
    private ArrayList<String> mList;
    private String mid="main";
      //图片缓存
    private HashMap<String ,ArrayList<String>> mPicturePath = new HashMap<String ,ArrayList<String> >();

   private ArrayList<String> mPath=new ArrayList<String>();

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_progress);
        oid = getIntent().getStringExtra("oid");
        if (oid == null) {
            Toast.makeText(getApplicationContext(), "0000", Toast.LENGTH_SHORT).show();
        }

        Log.e(TAG, "initContenView: " + oid);
        findViewid();
        requestservice();
        initView();
    }

    private void findViewid() {
        mGridView = (GridView) findViewById(R.id.sby_progresspage_gridview);
        mListView = (ListView) findViewById(R.id.sby_progeresspage_timeline);
    }

    private void initView() {
        mGridView.setOnItemClickListener(itemClickListener);

    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position != mResults.size()) {
                Intent intent = new Intent(ProgressActivity.this, ShowPicActivity.class);
                if (mResults == null) return;
                intent.putStringArrayListExtra("content", mResults);
                intent.putExtra("ID", position);
                startActivity(intent);
            }
        }
    };

    private void requestservice() {

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                dialog.dismiss();
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                dialog.dismiss();
                Log.e(TAG, "onError: " + e.toString());
            }

            @Override
            public void onNext(String s) {
                if (s.length() > 0) {
                    Gson gson = new Gson();
                    ProgressVo json = gson.fromJson(s, ProgressVo.class);
                    List<ProgressVo.MsgBean.ListBean> list = json.getMsg().getList();
                    for (int i = 0; i < list.size(); i++) {
                        ProgressVo.MsgBean.ListBean listBean = list.get(i);
                        String id = listBean.getDlr_toubaozhuangtai_id();
                        showProgress(id);
                    }
                    List<String> path = json.getMsg().getPath();
                    Log.d(TAG, "path===========: "+path.toString() );
                    addDatalist(path);

                }
            }
        };
        RequestService(subscriber);
    }


    private void clearDataList() {
        if (mResults == null) {
            mResults = new ArrayList<>();
            mImagerAdapter = new PictureShowAdapter(ProgressActivity.this, mResults);
            mGridView.setAdapter(mImagerAdapter);
        } else {
            mResults.clear();
            mImagerAdapter.setData(mResults);
        }
    }

    private void addDatalist(List<String> list) {
        if (mResults == null)
            clearDataList();
        if (list == null || list.isEmpty())
            return;
        mResults.addAll(list);
    }

    private void showProgress(String id) {
        if (id == null) return;
        TimeAxisVo timeAxisVo = new TimeAxisVo();
        List<TimeAxisVo> add = timeAxisVo.add();
        TimeaxisAdapter adapter = new TimeaxisAdapter(this, add, id);
        mListView.setAdapter(adapter);

    }

    private void RequestService(Subscriber<String> subscriber) {
        Observable.just(ActionVO.LOGINSERVICE)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Request request = new Request.Builder()
                                .url(s + "xqshow?oid=" + oid)
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
                        dialog = showDialoges("加载数据中....");
                        dialog.show();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
