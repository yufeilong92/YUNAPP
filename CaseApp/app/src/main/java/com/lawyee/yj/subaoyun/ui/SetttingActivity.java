package com.lawyee.yj.subaoyun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lawyee.yj.subaoyun.Fragment.AboutusFragment;
import com.lawyee.yj.subaoyun.R;
/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:25:16:25
 * @Purpose :设置界面
 */




public class SetttingActivity extends BaseActivity implements View.OnClickListener {


    private TextView mTitleTV;
    private FragmentTransaction transaction;
    private AboutusFragment mHelpfragment;
    public static final String HELPFRAGMNETTAG="1";
    public static final String ABOUTUSFRAGMNETTAG="2";
    private AboutusFragment mAboutusFragment;
    private RelativeLayout mfinish;

    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settting);

        findviewid();
        initview();

    }

    private void initview() {

    }

    private void findviewid() {
        mfinish = (RelativeLayout) findViewById(R.id.sby_setting_finsh);
        mfinish.setOnClickListener(this);
        mTitleTV = (TextView) findViewById(R.id.sby_setpage_setting);
        View view = findViewById(R.id.content_view);
    }



    protected void helpexplain(View view){
       startActivity(new Intent(SetttingActivity.this,FragmentActivity.class).putExtra("content",HELPFRAGMNETTAG));
    }
    protected void aboutus(View view){
       startActivity(new Intent(SetttingActivity.this,FragmentActivity.class).putExtra("content",ABOUTUSFRAGMNETTAG));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        { case R.id.sby_setting_finsh :
            startActivity(new Intent(SetttingActivity.this,PilotActivity.class));
              break;
          default:
              break;
        }
    }
}
