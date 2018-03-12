package com.lawyee.yj.subaoyun.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lawyee.yj.subaoyun.utils.ShowDialogs;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.adapter.Useradapter;
import com.lawyee.yj.subaoyun.serviceImpl.NetUtils;
import com.lawyee.yj.subaoyun.utils.SaveFileService;
import com.lawyee.yj.subaoyun.vo.ActionVO;
import com.lawyee.yj.subaoyun.vo.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:27:16:27
 * @Purpose :主界面
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Useradapter adapter;
    private TextView mMyInsuer;
    private TextView mInsuer;
    private ImageView mInsuerIV, mMyInsuerIV;
    private ImageView mRegister;
    public static final int INSURE = 100001;
    public static final int MYINSURE = 100002;
    private String staff_id, login_id, mobile;
    private Dialog dialog;
    private SwipeRefreshLayout mSwipeRefresh;
    private ListView mListView;

    private List<UserData.MsgBean> mResult = null;


    private int visibleLastIndex;//用来可显示的最后一条数据的索引
    private int visiblecount = 20;//每次加载的数据
    private View footerview;
    private UserData mUserData;
    private Bundle parameter;
    private TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv= (TextView) findViewById(R.id.network);
        //启动时判断网络状态
        boolean netConnect = this.isNetConnect();
        if (netConnect){
            mTv.setVisibility(View.GONE);
        }else {
            mTv.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onNetChange(int netMobile) {
        super.onNetChange(netMobile);
        //网络状态变化时的操作
        if (netMobile== NetUtils.NETWORK_NONE){
            mTv.setVisibility(View.VISIBLE);
        }else {
            mTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        parameter = getIntent().getExtras();
        staff_id = parameter.getString("Staff_id");
        login_id = parameter.getString("login_id");
        mobile = parameter.getString("mobile");
        FindviewByid();
        initView();
        addNewData();
    }

    private void initView() {
        footerview = LayoutInflater.from(this).inflate(R.layout.footer_layout, null);
        footerview.setVisibility(View.GONE);
        mListView.addFooterView(footerview, null, false);
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_red_light, android.R.color.holo_purple);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE) {
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        footerview.setVisibility(View.VISIBLE);
                        // TODO: 2016/11/11 加载新数据
                        addMoreData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;//减去最后一个加载中那条
            }
        });
        mListView.setOnItemClickListener(itemClickListener);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                addMoreData();
            }
        });


    }

    private ListView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            String oid = mResult.get(position).getOid();
            Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
            intent.putExtra("oid",oid);
            startActivity(intent);
        }
    };

    private void addMoreData() {

        clearDataList();
        addMoreRequest();

    }

    private void showdialog() {
        dialog = new ShowDialogs().createLoadingDialog(MainActivity.this, "正在加载....");
        dialog.show();
    }

    private void addMoreRequest() {

        Subscriber<String> mMoresubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mSwipeRefresh.setRefreshing(false);
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefresh.setRefreshing(false);
                Log.e(TAG, "onError: " + e);
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: " + s);
                if (s.length() > 0) {
                    mSwipeRefresh.setRefreshing(false);
                    footerview.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(s, UserData.class);
                    List<UserData.MsgBean> msg = userData.getMsg();
                    if (mResult.equals(msg)) {
                        Toast.makeText(getApplicationContext(), "已经是最新数据", Toast.LENGTH_SHORT).show();
                    } else {
                        addDataList(msg);
                    }
                }
            }
        };
        RequestService(mMoresubscriber);
    }


    private void addNewData() {

        mUserData = new UserData();
        if (staff_id == null) {
            String findid = SaveFileService.findid(this);
            staff_id = findid;
        }
        showdialog();
        newRequestService();
    }

    private void newRequestService() {
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
                    dialog.dismiss();
                    if (mSwipeRefresh.isRefreshing()) {
                        mSwipeRefresh.setRefreshing(false);
                    }

                    Gson gson = new Gson();
                    UserData userData = gson.fromJson(s, UserData.class);

                    mUserData.setmUserData(userData);
                    int code = userData.getCode();
                    List<UserData.MsgBean> msg = userData.getMsg();

                    addDataList(msg);
                }
            }
        };

        RequestService(subscriber);

    }

    private void RequestService(Subscriber<String> subscriber) {
        Observable.just(ActionVO.LOGINSERVICE)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Request request = new Request.Builder()
                                .url(s + "tblist?" + "staff_id=" + staff_id)
                                .build();
                        try {
                            Log.d(">>>", s + "tblist?" + "staff_id=" + staff_id);
                            Response response = LoginActivity.client.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void FindviewByid() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.sby_main_swiperefresh);
        boolean b = SaveFileService.sava_staf_id(this, staff_id);
        if (b) {
            Log.d(TAG, "数据保存成功 " );
        }
        mInsuer = (TextView) findViewById(R.id.sby_mian_wantinsuer);
        mInsuerIV = (ImageView) findViewById(R.id.sby_mian_wantinsuers);
        mMyInsuer = (TextView) findViewById(R.id.sby_mian_myinsurance);
        mMyInsuerIV = (ImageView) findViewById(R.id.sby_mian_myinsurances);
        mRegister = (ImageView) findViewById(R.id.sby_main_setting);
        mInsuer.setOnClickListener(this);
        mMyInsuer.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mInsuerIV.setOnClickListener(this);
        mMyInsuerIV.setOnClickListener(this);
//// TODO: 2016/11/11 修改
        mListView = (ListView) findViewById(R.id.sby_mian_listView);
            mListView.setVerticalScrollBarEnabled(true);


    }

    private void clearDataList() {
        if (mResult == null) {
            mResult = new ArrayList<UserData.MsgBean>();
            adapter = new Useradapter(mResult, this);
            mListView.setAdapter(adapter);
        } else {
            adapter.setData(mResult);
        }
    }

    private void addDataList(List<UserData.MsgBean> list) {
        if (mResult == null)
            clearDataList();
        if (list == null || list.isEmpty())
            return;
        mResult.clear();
        mResult.addAll(list);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sby_main_setting:
                startActivity(new Intent(MainActivity.this, SetttingActivity.class));
                break;

            case R.id.sby_mian_myinsurances:
                startActivity(INSURE);
                break;
            case R.id.sby_mian_myinsurance:
                // TODO: 2016/10/24 把用户信息传过去
                startActivity(INSURE);
                break;
            case R.id.sby_mian_wantinsuers:
                startActivity(MYINSURE);
                break;
            case R.id.sby_mian_wantinsuer:
                startActivity(MYINSURE);
                break;
            default:
                break;
        }
    }

    private void startActivity(int i) {
        if (i == INSURE) {
            addNewData();
        } else if (i == MYINSURE) {
            Intent intent = new Intent(MainActivity.this, InsureActivity.class);
            intent.putExtras(parameter);
            startActivity(intent);

        }
    }

}
