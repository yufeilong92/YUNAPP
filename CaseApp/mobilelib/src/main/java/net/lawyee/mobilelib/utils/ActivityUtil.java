package net.lawyee.mobilelib.utils;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Activity相关工具类，如：判断是否前端运行、发送Mail
 * @author wuzhu
 * @date 2013-12-12 09:22:00
 * @version $id$
 */
public class ActivityUtil
{

	private static final String TAG = ActivityUtil.class.getSimpleName();

	/**
	 * 断当前应用程序处于前台
	 * @param context
	 * @return
     */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if(appProcesses==null)
			return false;
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断某个activity是否在最上层显示
	 * @param context
	 * @return
	 * @note 添加以下权限:
	 * <uses-permission android:name="android.permission.GET_TASKS" />
     */
	boolean isFront(Activity context){
		try{
			ActivityManager act = (ActivityManager ) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfo = act.getRunningTasks(1);
			if(taskInfo.size()>0){
				return context.getComponentName().getClassName().equals(taskInfo.get(0).topActivity.getClassName());
			}
		}catch(Exception e){
		}
		return false;
	}



	/** 获得系统进程信息-UID */
	public static int getRunningAppProcessUID(Context context) {
		int uid = 0;
		// 获得ActivityManager服务的对象
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packagename = context.getApplicationInfo().packageName;
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			String processName = appProcessInfo.processName;
			if (!StringUtil.isEmpty(processName) && processName.equals(packagename)) {
				uid = appProcessInfo.uid;
				break;
			}
		}
		return uid;
	}
}
