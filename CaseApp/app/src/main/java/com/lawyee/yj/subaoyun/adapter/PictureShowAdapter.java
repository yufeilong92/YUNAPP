package com.lawyee.yj.subaoyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lawyee.yj.subaoyun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfl on 2016/11/23 14:47.
 *
 * @purpose: 图片适配器
 */

public class PictureShowAdapter extends BaseAdapter {

    private Context context;
    private List<String> mList;
    private final LayoutInflater inflater;
    private ImageView imger;
    private int selectid;

    public PictureShowAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;

        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setShowPicture(ArrayList<String> mList, int id) {
        this.mList = mList;
        this.selectid = id;

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converview, ViewGroup viewGroup) {

        if (converview == null) {
            converview = inflater.inflate(R.layout.list_item_picture, viewGroup, false);
        }
        imger = (ImageView) converview.findViewById(R.id.lj_progress_picture);
            Glide.with(context)
                    .load(mList.get(i))
                    .override(500,500)
                    .placeholder(R.mipmap.plugin_camera_no_pictures)
                    .skipMemoryCache(false)
                    .animate(R.anim.item_alpha_in)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.plugin_camera_no_pictures)
                    .into(imger);
        return converview;
    }
}
