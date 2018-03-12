package com.lawyee.yj.subaoyun.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lawyee.yj.subaoyun.vo.ProgressVo;
import com.lawyee.yj.subaoyun.R;
import com.lawyee.yj.subaoyun.vo.TimeAxisVo;

import java.util.List;

/**
 * Created by yfl on 2016/10/25 14:29.
 * @purpose: 进度适配器
 */
public class TimeaxisAdapter extends BaseAdapter {
private static final String TAG="TimeaxisAdapter";
    private Context mContext;
    private List<TimeAxisVo> mData;
    private final LayoutInflater mInflater;
    private TextView mText;
    private Boolean istrue = false;
    private String number;
    private View progresscircle;
    private View progressline;
    private View progresslines;
    private List<ProgressVo.MsgBean.ListBean> mlist;


    public TimeaxisAdapter(Context context, List<TimeAxisVo> list, String id) {
        this.mContext = context;
        this.mData = list;
        this.number=id;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<ProgressVo.MsgBean.ListBean> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }


    public boolean setColor(Boolean is, String id) {
        this.istrue = is;
        notifyDataSetChanged();
        return istrue;
    }

    @Override
    public int getCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.size() == 0 ? null : mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View converview, ViewGroup viewGroup) {

        if (converview == null) {
            converview = mInflater.inflate(R.layout.list_item_timeaxis, null);
        }
        progressline = converview.findViewById(R.id.sby_progeress_line);
        progresscircle = converview.findViewById(R.id.sby_progress_circle);
        progresslines = converview.findViewById(R.id.sby_progress_lines);
        mText = (TextView) converview.findViewById(R.id.sby_process_tv);

        mText.setText(mData.get(i).getProcess());
        if (i == 0) {
            progressline.setVisibility(View.INVISIBLE);
        } else {
            progressline.setVisibility(View.VISIBLE);
        }
        if (i == 3) {
            progresslines.setVisibility(View.GONE);
        } else {
            progresslines.setVisibility(View.VISIBLE);
        }

        if (number.equals("1")) {
            setOneColor(i);
        }
        if (number.equals("5")) {
            setOneColor(0);
            setTwoColor(i);
        }
        if (number.equals("6")) {
            setOneColor(0);
            setTwoColor(1);
            setThreeColor(i);
        }
        if (number.equals("4")) {
            setFourColor();
        }
        return converview;
    }

    private void setFourColor() {
        progressline.setBackgroundColor(Color.BLUE);
        mText.setTextColor(Color.BLUE);
        progresscircle.setBackgroundResource(R.drawable.time_cycles);
        progresslines.setBackgroundColor(Color.BLUE);
    }

    private void setThreeColor(int id) {
        progressline.setBackgroundColor(id <= 2 ? Color.BLUE : Color.parseColor("#CBCBCB"));
        progresscircle.setBackgroundResource(id<=2?R.drawable.time_cycles:R.drawable.time_cycle);
        progresslines.setBackgroundColor(id < 2 ? Color.BLUE : Color.parseColor("#CBCBCB"));
        mText.setTextColor(id <= 2 ? Color.BLUE : Color.parseColor("#CBCBCB"));

    }

    private void setTwoColor(int id) {
        progressline.setBackgroundColor(id <=1 ? Color.BLUE : Color.parseColor("#CBCBCB"));
        progresscircle.setBackgroundResource(id<=1?R.drawable.time_cycles:R.drawable.time_cycle);
        progresslines.setBackgroundColor(id <1 ? Color.BLUE: Color.parseColor("#CBCBCB"));
        mText.setTextColor(id <= 1 ? Color.BLUE : Color.parseColor("#CBCBCB"));
    }

    private void setOneColor(int id) {
        progresscircle.setBackgroundResource(id==0?R.drawable.time_cycles:R.drawable.time_cycle);
        mText.setTextColor(id == 0 ? Color.BLUE : Color.parseColor("#CBCBCB"));
        progressline.setBackgroundColor(id == 0 ? Color.BLUE : Color.parseColor("#CBCBCB"));
    }

}
