package com.lawyee.yj.subaoyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lawyee.yj.subaoyun.R;

import java.util.ArrayList;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/19 16:43
 * @Purpose :
 */
public class MyPicRecycleAdpter extends RecyclerView.Adapter<MyPicRecycleAdpter.MyViewHolder> implements View.OnClickListener{


    private ArrayList<String> mData;
    private Context mContext;
    private final LayoutInflater inflater;
    private int index;

    public MyPicRecycleAdpter(Context mContext, ArrayList<String> mlist) {

        this.mContext = mContext;
        this.mData = mlist;
        inflater = LayoutInflater.from(mContext);
    }


    /**
     * C创建布局
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_picture, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return myViewHolder;
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mData.get(position))
                .placeholder(R.mipmap.plugin_camera_no_pictures)
                .skipMemoryCache(false)
                .animate(R.anim.item_alpha_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.plugin_camera_no_pictures)
                .into(holder.img);
         holder.itemView.setTag(mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener !=null){
            mOnItemClickListener.onItemClick(view, (String) view.getTag());
        }
    }

    /**
     * 查找控件
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
     private ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.lj_progress_picture);
        }
    }

    public  static interface OnRecylerViewItemClicklistener{
        void onItemClick(View view ,String data);
    }

    private OnRecylerViewItemClicklistener mOnItemClickListener=null;
    public void setmOnItemClickListener(OnRecylerViewItemClicklistener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
