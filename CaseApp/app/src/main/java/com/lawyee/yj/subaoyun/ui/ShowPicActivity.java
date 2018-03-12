package com.lawyee.yj.subaoyun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.lawyee.yj.subaoyun.adapter.MyPicRecycleAdpter;
import com.lawyee.yj.subaoyun.R;

import java.util.ArrayList;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:37
 * @Purpose :
 */
public class ShowPicActivity extends BaseActivity {
    private RecyclerView mRecyclerPic;
    private ArrayList<String> mData;
    private MyPicRecycleAdpter adapter;
    private int id;
    private LinearLayout mLinearlayout;


    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_showprogresspic);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mData = intent.getStringArrayListExtra("content");
        id = intent.getIntExtra("ID", 0);
        adapter = new MyPicRecycleAdpter(this, mData);
        mRecyclerPic.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerPic.setLayoutManager(layoutManager);
        SelectImage(mData);
      mLinearlayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
         if (isNotDoubleClick()){
            finish();
         }
          }
      });
      adapter.setmOnItemClickListener(new MyPicRecycleAdpter.OnRecylerViewItemClicklistener() {
          @Override
          public void onItemClick(View view, String data) {
              if (isNotDoubleClick()){
                  finish();
              }
          }
      });
    }


    private void SelectImage(ArrayList<String> mlist) {
        if (mlist == null) return;
        String url = mlist.get(id);
        mRecyclerPic.smoothScrollToPosition(id);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        mRecyclerPic = (RecyclerView) findViewById(R.id.recycler_pic);
        mLinearlayout= (LinearLayout) findViewById(R.id.linearlayout);

    }
    private static long lastClickTime = 0;
    public static boolean isNotDoubleClick(){
        long time = System.currentTimeMillis();
        long diff = time - lastClickTime;
        lastClickTime = time;
        if( diff>0 && diff<500 ){
            return false;
        }
        return true;
    }

}
