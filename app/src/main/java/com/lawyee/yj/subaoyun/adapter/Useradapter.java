package com.lawyee.yj.subaoyun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lawyee.yj.subaoyun.vo.UserData;
import com.lawyee.yj.subaoyun.R;
import java.util.List;

/**
 * Created by yfl on 2016/10/21 17:13.
 *
 * @purpose: 用户数据展示
 */

public class Useradapter extends BaseAdapter {

    private List<UserData.MsgBean> mList;
    private Context mcontext;
    private final LayoutInflater mLnflater;


    public Useradapter(List<UserData.MsgBean> list, Context context) {

        this.mList = list;
        this.mcontext = context;
        Log.e("-------", "Useradapter: "+mList.size() );

        mLnflater = LayoutInflater.from(context);
    }

    public void setData(List<UserData.MsgBean> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size() == 0 ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i) == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converview, ViewGroup viewGroup) {
        ViewHolder holder;
        if (converview == null) {
            holder = new ViewHolder();
            converview = mLnflater.inflate(R.layout.list_item_user, null);
            holder.mData = (TextView) converview.findViewById(R.id.sby_userdata_data);
            holder.mRegistetation = (TextView) converview.findViewById(R.id.sby_userdata_registetation);
            holder.mInsurer = (TextView) converview.findViewById(R.id.sby_userdata_insurer);
            converview.setTag(holder);
        } else {
            holder = (ViewHolder) converview.getTag();
        }

        UserData.MsgBean msgBean = mList.get(i);
//        holder.mData.setText(msgBean.getToubao_date());
        holder.mRegistetation.setText(msgBean.getDjh());
        holder.mInsurer.setText(msgBean.getShenqingren_name());
        return converview;
    }

    private static class ViewHolder {
        TextView mRegistetation;
        TextView mInsurer;
        TextView mData;
    }
}
