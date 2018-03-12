package com.lawyee.yj.subaoyun.utils;

import android.content.Context;
import android.widget.Toast;

import net.lawyee.mobilelib.utils.StringUtil;

/**
 * Created by yfl on 2016/10/27 18:26.
 *
 * @purpose:
 */

public class isEmpty {
    /**
     * 判断其值是否相同并提示用户
     * @param context
     * @param value  值一
     * @param values  值二
     */
    public static void isEmpty(Context context, String value, String values){
        if (StringUtil.isEmpty(value)&&StringUtil.isEmpty(values)){

        }else if (StringUtil.isEmpty(value)||StringUtil.isEmpty(values)){
//                     T.showShort(context,"请输入密码");
        }else {
            if (value.equals(values)){
            }else {
                Toast.makeText(context,"输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
