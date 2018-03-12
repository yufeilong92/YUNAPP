
package com.lawyee.yj.subaoyun.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.utils.Bimp;
import com.lawyee.yj.subaoyun.vo.ActionVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:24:16:24
 * @Purpose :删除图片
 */



public class ProviewPhotosAvtivity extends BaseActivity {
    private ViewPager mpager;
    private TextView mBack;
    private TextView mRmove;
    private AlertDialog dialog;
    private ArrayList<String> list;
    private int id;
    private String mSelectImg;
    private SaveImag saveImag;



    @Override
    protected void initContenView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_proview_photos_avtivity);
        mpager = (ViewPager) findViewById(R.id.viewpager);
        mBack = (TextView) findViewById(R.id.back);
        mRmove = (TextView) findViewById(R.id.remove);
        list = getIntent().getStringArrayListExtra("content");
        saveImag = new SaveImag();
        saveImag.setStr(list);
        SelectImg(saveImag.getStr());
        mRmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setdialog();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });
    }

    private void SelectImg(final ArrayList<String> mlist) {
        if (mlist.equals(""))return ;
        if(mlist.size()==0){
            finish();

        }
        List<View> views = new ArrayList<>();
            for (int i = 0; i < mlist.size(); i++) {
                ImageView imageView = new ImageView(ProviewPhotosAvtivity.this);
                try {
                    imageView.setImageBitmap(Bimp.revitionImageSize(mlist.get(i)));
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    views.add(imageView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        myviewpager adapter = new myviewpager(views);
        mpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        id = getIntent().getIntExtra("ID", 0);
        mpager.setCurrentItem(id);
        mpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mSelectImg = mlist.get(position);
                Log.d("测试代码", "onPageScrolled滑动中" + position + mSelectImg);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("测试代码", "onPageSelected选中了" + position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //正在滑动   pager处于正在拖拽中

                    Log.d("测试代码", "onPageScrollStateChanged=======正在滑动" + "SCROLL_STATE_DRAGGING");

                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                    Log.d("测试代码", "onPageScrollStateChanged=======自动沉降" + "SCROLL_STATE_SETTLING");

                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //空闲状态  pager处于空闲状态
                    Log.d("测试代码", "onPageScrollStateChanged=======空闲状态" + "SCROLL_STATE_IDLE");
                }

            }
        });
    }

    class myviewpager extends PagerAdapter {
        private List<View> mdata;

        public myviewpager(List<View> mlist) {
            this.mdata = mlist;
        }

        @Override
        public int getCount() {
            return mdata == null ? 0 : mdata.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mdata.get(position));
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mdata.get(position));
            return mdata.get(position);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void setdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("要删除这张照片吗？");
        builder.setTitle("提示");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mRemoveImager();
            }


        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.show();
    }

    private void mRemoveImager() {
        if (mSelectImg.equals("")) return;
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (mSelectImg.equals(list.get(i))) {
                list.remove(mSelectImg);

            } else {
                strings.add(list.get(i));
            }
        }
        saveImag.setStr(strings);
        Intent intent = new Intent();
        intent.putStringArrayListExtra("Images",saveImag.getStr());
        intent.setAction(ActionVO.PREVIEWPHOTO);
        sendBroadcast(intent);

        SelectImg(saveImag.getStr());
    }

    class SaveImag {
        private ArrayList<String> str;

        public ArrayList<String> getStr() {
            return str;
        }

        public void setStr(ArrayList<String> str) {
            this.str = str;
        }

    }
}

