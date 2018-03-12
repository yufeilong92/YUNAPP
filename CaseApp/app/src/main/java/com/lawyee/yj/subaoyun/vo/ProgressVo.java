package com.lawyee.yj.subaoyun.vo;

import java.util.List;

/**
 * Created by yfl on 2016/11/23 09:30.
 *
 * @purpose:  进度解析
 */

public class ProgressVo {


    /**
     * code : 0
     * msg : {"list":[{"bdlx":"dw","djh":"1611221140041172","dlr_toubaozhuangtai_id":"1","dlr_toubaozhuangtai_name":"确认保险费率","jiaofei_status":"0","oid":"06f7f484c6a84f289fe2bb7508a940f1","shenqingren_id":"61eaaa239cf6478d8ff25554700831a1","shenqingren_iphone":"18317837561","shenqingren_name":"18317837561","toubao_date":"2016-11-22 11:40:04","toubaozhuangtai_id":"1","toubaozhuangtai_name":"确认保险费率","xiaoshou_id":"419706a3505345448adfed8146f529b3,b922d5c7211b42339632372f34b756e7,"}],"path":["http://www.subao120.com/fileUpload/subao.mobile.upload/2016-11-22/06f7f484c6a84f289fe2bb7508a940f1/ca6af79e94a04531938e8fffac588ca9.jpg"]}
     */

    private int code;
    private MsgBean msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private List<ListBean> list;
        private List<String> path;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<String> getPath() {
            return path;
        }

        public void setPath(List<String> path) {
            this.path = path;
        }

        public static class ListBean {
            /**
             * bdlx : dw
             * djh : 1611221140041172
             * dlr_toubaozhuangtai_id : 1
             * dlr_toubaozhuangtai_name : 确认保险费率
             * jiaofei_status : 0
             * oid : 06f7f484c6a84f289fe2bb7508a940f1
             * shenqingren_id : 61eaaa239cf6478d8ff25554700831a1
             * shenqingren_iphone : 18317837561
             * shenqingren_name : 18317837561
             * toubao_date : 2016-11-22 11:40:04
             * toubaozhuangtai_id : 1
             * toubaozhuangtai_name : 确认保险费率
             * xiaoshou_id : 419706a3505345448adfed8146f529b3,b922d5c7211b42339632372f34b756e7,
             */


            private String bdlx;
            private String djh;
            private String dlr_toubaozhuangtai_id;
            private String dlr_toubaozhuangtai_name;
            private String jiaofei_status;
            private String oid;
            private String shenqingren_id;
            private String shenqingren_iphone;
            private String shenqingren_name;
            private String toubao_date;
            private String toubaozhuangtai_id;
            private String toubaozhuangtai_name;
            private String xiaoshou_id;

            public String getBdlx() {
                return bdlx;
            }

            public void setBdlx(String bdlx) {
                this.bdlx = bdlx;
            }

            public String getDjh() {
                return djh;
            }

            public void setDjh(String djh) {
                this.djh = djh;
            }

            public String getDlr_toubaozhuangtai_id() {
                return dlr_toubaozhuangtai_id;
            }

            public void setDlr_toubaozhuangtai_id(String dlr_toubaozhuangtai_id) {
                this.dlr_toubaozhuangtai_id = dlr_toubaozhuangtai_id;
            }

            public String getDlr_toubaozhuangtai_name() {
                return dlr_toubaozhuangtai_name;
            }

            public void setDlr_toubaozhuangtai_name(String dlr_toubaozhuangtai_name) {
                this.dlr_toubaozhuangtai_name = dlr_toubaozhuangtai_name;
            }

            public String getJiaofei_status() {
                return jiaofei_status;
            }

            public void setJiaofei_status(String jiaofei_status) {
                this.jiaofei_status = jiaofei_status;
            }

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getShenqingren_id() {
                return shenqingren_id;
            }

            public void setShenqingren_id(String shenqingren_id) {
                this.shenqingren_id = shenqingren_id;
            }

            public String getShenqingren_iphone() {
                return shenqingren_iphone;
            }

            public void setShenqingren_iphone(String shenqingren_iphone) {
                this.shenqingren_iphone = shenqingren_iphone;
            }

            public String getShenqingren_name() {
                return shenqingren_name;
            }

            public void setShenqingren_name(String shenqingren_name) {
                this.shenqingren_name = shenqingren_name;
            }

            public String getToubao_date() {
                return toubao_date;
            }

            public void setToubao_date(String toubao_date) {
                this.toubao_date = toubao_date;
            }

            public String getToubaozhuangtai_id() {
                return toubaozhuangtai_id;
            }

            public void setToubaozhuangtai_id(String toubaozhuangtai_id) {
                this.toubaozhuangtai_id = toubaozhuangtai_id;
            }

            public String getToubaozhuangtai_name() {
                return toubaozhuangtai_name;
            }

            public void setToubaozhuangtai_name(String toubaozhuangtai_name) {
                this.toubaozhuangtai_name = toubaozhuangtai_name;
            }

            public String getXiaoshou_id() {
                return xiaoshou_id;
            }

            public void setXiaoshou_id(String xiaoshou_id) {
                this.xiaoshou_id = xiaoshou_id;
            }
        }
    }





}
