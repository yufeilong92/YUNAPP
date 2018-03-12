/**
* Project:newsreader2
* File:ShortcutUtil.java
* Copyright 2013 WUZHUPC Co., Ltd. All rights reserved.
*/
package net.lawyee.mobilelib.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

/**
 * 桌面快捷图标工具类
 * @author wuzhu
 * @date 2013-6-21 下午3:19:48
 * @version $id$
 */
public class ShortcutUtil
{
	/**
	 * 
	 * @param act 运行程序入口
	 * @param iconResId 图标
	 * @param appnameResId 应用程序名称
	 * @note 需要增加权限
	 * 	<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
	 *	<uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
	 */
	public static void createShortCut(Activity act, int iconResId,  
            int appnameResId) {  
  
        // com.android.launcher.permission.INSTALL_SHORTCUT  
  
        Intent shortcutintent = new Intent(  
                "com.android.launcher.action.INSTALL_SHORTCUT");  
        // 不允许重复创建  
        shortcutintent.putExtra("duplicate", false);  
        // 需要现实的名称  
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,  
                act.getString(appnameResId));  
        // 快捷图片  
        Parcelable icon = Intent.ShortcutIconResource.fromContext(  
                act.getApplicationContext(), iconResId);  
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);  
        // 点击快捷图片，运行的程序主入口  
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,  
                new Intent(act.getApplicationContext(), act.getClass()));  
        // 发送广播  
        act.sendBroadcast(shortcutintent);  
    } 
}
