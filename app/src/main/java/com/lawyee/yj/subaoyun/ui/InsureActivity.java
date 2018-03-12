package com.lawyee.yj.subaoyun.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;

import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.T;
/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:28:16:28
 * @Purpose :选保界面
 */

public class InsureActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mCompany;
    private LinearLayout mPersional;
    private static final String PERSIONALTYPE = "persional";
    private static final String COMPANYTYPE = "company";
    private Isnext isnext;
    private LinearLayout mLinearlayout;
    private Bundle parameter;


    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_insure);
        parameter = getIntent().getExtras();
        initView();
        mFindviewbyid();
        setClickListener();
    }

    private void initView() {
        isnext = new Isnext();
        isnext.setNext(false);

    }

    private void setClickListener() {
        mCompany.setOnClickListener(this);
        mPersional.setOnClickListener(this);

    }

    private void mFindviewbyid() {
        mCompany = (LinearLayout) findViewById(R.id.sby_typepage_company);
        mPersional = (LinearLayout) findViewById(R.id.sby_typepage_persional);
        mLinearlayout = (LinearLayout) findViewById(R.id.sby_typepage_linear);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sby_typepage_persional:
                isnext.setType(PERSIONALTYPE);
                isnext.setNext(true);
                setIsNext();
                break;
            case R.id.sby_typepage_company:
                isnext.setNext(true);
                isnext.setType(COMPANYTYPE);
                setIsNext();
                break;
            default:
                break;
        }
    }
    private void setIsNext() {
        if (isnext.getNext()) {
            if (isnext.getType().equals(PERSIONALTYPE)) {
                isnext.setType(getResources().getString(R.string.personalMark));
//                Toast.makeText(getApplicationContext(), "个人", Toast.LENGTH_SHORT).show();
                uplaoddata();
            } else if (isnext.getType().equals(COMPANYTYPE)) {
                isnext.setType(getResources().getString(R.string.UnitMark));
//                Toast.makeText(getApplicationContext(), "单位", Toast.LENGTH_SHORT).show();
                uplaoddata();
            }
        } else {
            T.showShort(InsureActivity.this, "请选择投保类型");
        }
    }

    private void uplaoddata() {

        Intent intent = new Intent(InsureActivity.this, DataUpActivity.class);
        intent.putExtra("type",isnext.getType());
        intent.putExtras(parameter);
        startActivity(intent);
    }

    /**
     * 判断类型
     */
    public class Isnext {

        private Boolean next;
        private String type;

        public Boolean getNext() {
            return next;
        }

        public void setNext(Boolean next) {
            this.next = next;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
