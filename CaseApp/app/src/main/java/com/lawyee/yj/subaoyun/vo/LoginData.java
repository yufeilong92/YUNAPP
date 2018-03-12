package com.lawyee.yj.subaoyun.vo;

/**
 * Created by yfl on 2016/11/7 17:48.
 *
 * @purpose: 登陆数据解析
 */

public class LoginData {

    /**
     * code : 0
     * mobile : 18317837561
     * msg : {"enabled":true,"id":"8183f8212e2a4dc7823e28c607da1c5c","id_number":"410222199208095552","locked":false,"login_id":"18317837561","mobile":"18317837561","orgid":"e290d6a64e1f4fb894c44b1008c60139","password":"494c47d726627d6ac567769c39d68d85","staff_id":"61eaaa239cf6478d8ff25554700831a1","status":"Actived","type":"Normal","user_id":"67e5bcce09104434a990e97aa59f4878","version_":0}
     * staff_id : 61eaaa239cf6478d8ff25554700831a1
     */

    private int code;
    private String mobile;
    /**
     * enabled : true
     * id : 8183f8212e2a4dc7823e28c607da1c5c
     * id_number : 410222199208095552
     * locked : false
     * login_id : 18317837561
     * mobile : 18317837561
     * orgid : e290d6a64e1f4fb894c44b1008c60139
     * password : 494c47d726627d6ac567769c39d68d85
     * staff_id : 61eaaa239cf6478d8ff25554700831a1
     * status : Actived
     * type : Normal
     * user_id : 67e5bcce09104434a990e97aa59f4878
     * version_ : 0
     */

    private MsgBean msg;
    private String staff_id;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public static class MsgBean {
        private boolean enabled;
        private String id;
        private String id_number;
        private boolean locked;
        private String login_id;
        private String mobile;
        private String orgid;
        private String password;
        private String staff_id;
        private String status;
        private String type;
        private String user_id;
        private int version_;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId_number() {
            return id_number;
        }

        public void setId_number(String id_number) {
            this.id_number = id_number;
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public String getLogin_id() {
            return login_id;
        }

        public void setLogin_id(String login_id) {
            this.login_id = login_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOrgid() {
            return orgid;
        }

        public void setOrgid(String orgid) {
            this.orgid = orgid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getVersion_() {
            return version_;
        }

        public void setVersion_(int version_) {
            this.version_ = version_;
        }
    }
}
