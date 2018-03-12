/**
* Project:newsreader2
* File:SettingUtil.java
* Copyright 2013 WUZHUPC Co., Ltd. All rights reserved.
*/
package net.lawyee.mobilelib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 系统设置工具基类
 * @author wuzhu
 * @date 2013-4-25 下午8:14:04
 * @version $id$
 */
@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
public class SettingUtil
{
	private final static String CSTR_FILENAME_SETTINGSHAREDPREFERENCE="lawyeesetting";
	  
	/**
	 * 获取boolean类型设置数据
	 * @param c
	 * @param key
	 * @param def
	 * @return
	 */
	public static boolean getBooleanSettingValue(Context c,String key,boolean def)
	{
		if(StringUtil.isEmpty(key))
				return def;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE);
		 
		return sp.getBoolean(key, def);
	}
	
	public static void setBooleanSettingValue(Context c,String key,boolean value)
	{
		if(StringUtil.isEmpty(key))
				return;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE); 
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static int getIntSettingValue(Context c,String key,int def)
	{
		if(StringUtil.isEmpty(key))
				return def;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE);
		 
		return sp.getInt(key, def);
	}
	
	public static void setIntSettingValue(Context c,String key,int value)
	{
		if(StringUtil.isEmpty(key))
				return;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE); 
		Editor editor=sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static long getLongSettingValue(Context c,String key,long def)
	{
		if(StringUtil.isEmpty(key))
				return def;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE);
		 
		return sp.getLong(key, def);
	}
	
	public static void setLongSettingValue(Context c,String key,long value)
	{
		if(StringUtil.isEmpty(key))
				return;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE); 
		Editor editor=sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static String getStringSettingValue(Context c,String key,String def)
	{
		if(StringUtil.isEmpty(key))
				return def;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE);
		 
		return sp.getString(key, def);
	}
	
	public static void setStringSettingValue(Context c,String key,String value)
	{
		if(StringUtil.isEmpty(key))
				return;
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE); 
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 获取客户端版本
	 * @param c
	 * @return
	 */
	public static String getClientVersion(Context c)
	{
		PackageManager manager = c.getPackageManager();
		try {
			PackageInfo info=manager.getPackageInfo(c.getPackageName(), 0);	
			return info.versionName;
		} catch (Exception e) {
			return "1.0.0";			
		}
	}
	
	/**
	 * 检测是否是第一次启动
	 * @param c 要检测Activity
	 * @param recordstart true时记录已经启动过 false则不进行记录
	 * @return true 第一次启动 false 非第一次启动
	 */
	@SuppressLint("WorldWriteableFiles")
	public static Boolean isFirstStart(Activity c,Boolean recordstart)
	{
		String key="fs_"+c.getClass().getSimpleName();
		
		SharedPreferences sp=c.getSharedPreferences(CSTR_FILENAME_SETTINGSHAREDPREFERENCE, Context.MODE_PRIVATE);
		
		Boolean firststart=sp.getBoolean(key, true);
		
		if(recordstart&&firststart)
		{
			Editor editor=sp.edit();
			editor.putBoolean(key, false);
			editor.commit();
		}
		
		return firststart;
	}
}
