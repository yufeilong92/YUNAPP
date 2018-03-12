package com.lawyee.yj.subaoyun.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfl on 2016/10/25 14:30.
 *
 * @purpose:  保单流程
 */

public class TimeAxisVo {
    private int id;
    private String process;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public List<TimeAxisVo> add() {
        ArrayList<TimeAxisVo> arrayList = new ArrayList<>();
        TimeAxisVo axisVo = new TimeAxisVo();
        axisVo.setProcess("确定保险费率");
        arrayList.add(axisVo);
        TimeAxisVo axisVo1 = new TimeAxisVo();
        axisVo1.setProcess("出具保单保函");
        arrayList.add(axisVo1);
        TimeAxisVo axisVo2 = new TimeAxisVo();
        axisVo2.setProcess("上传法院裁定");
        arrayList.add(axisVo2);
        TimeAxisVo axisVo3 = new TimeAxisVo();
        axisVo3.setProcess("结束");
        arrayList.add(axisVo3);
        return arrayList;
    }
}
