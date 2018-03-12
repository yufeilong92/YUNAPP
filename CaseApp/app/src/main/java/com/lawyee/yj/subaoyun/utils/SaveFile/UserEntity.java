package com.lawyee.yj.subaoyun.utils.SaveFile;

import java.io.Serializable;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/28 11:10
 * @Purpose :
 */
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -5683263669918171030L;
    private String userName;
    // 原始密码
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    private String password;
}