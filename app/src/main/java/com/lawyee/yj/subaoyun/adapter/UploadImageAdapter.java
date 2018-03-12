package com.lawyee.yj.subaoyun.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.lawyee.yj.subaoyun.R;

import java.io.File;
import java.util.List;


/**
 * Created by yfl on 2016/10/13.
 * email: yufeilong92@163.com
 * @purpose: 上传适配器
 */


public class UploadImageAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private Context mContext;
    private List<String> mList;
    private Boolean isdispay = true;
    private static ImageLoaderConfiguration config;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public UploadImageAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        setDisplayImageOptions();
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSizePercentage(40)
                .build();
        imageLoader.getInstance().init(config);

    }

    public UploadImageAdapter(Context context, List<String> list, boolean IsDispay) {
        this.mContext = context;
        this.mList = list;
        this.isdispay = IsDispay;
        setDisplayImageOptions();
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSizePercentage(40)
                .build();
        imageLoader.getInstance().init(config);

    }

    public void setDisplayImageOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.plugin_camera_no_pictures)
                .showImageForEmptyUri(R.mipmap.plugin_camera_no_pictures)
                .showImageOnFail(R.mipmap.plugin_camera_no_pictures)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

    }

    @Override
    public int getCount() {
        if (mList.size() == 8) return 8;
        return (mList.size() + 1);
    }

    public void setData(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_published_grida, parent, false);
        ImageView image = (ImageView) convertView.findViewById(R.id.item_grida_image);


        if (position == mList.size()) {

            if (isdispay) {
                image.setImageBitmap(BitmapFactory.decodeResource(
                        mContext.getResources(), R.mipmap.photo));
            }
            if (position == 8) {
                image.setVisibility(View.GONE);
            }
        } else {

            String string = Environment.getExternalStorageDirectory().toString() + File.separator;

            //imgLoader.displayImage("file:///"+Environment .getExternalStorageDirectory().toString() + File.separator + Const.TEMP_FILE, choosen_image);
            Log.e(">>>", "getView: file://" + mList.get(position));

            imageLoader.displayImage("file:///" + mList.get(position), image, options);
//            viewHolder.image.setImageBitmap(BitmapFactory.decodeResource(
//                    mContext.getResources(), R.mipmap.photo));
            //viewHolder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
        }
        return convertView;
    }


}