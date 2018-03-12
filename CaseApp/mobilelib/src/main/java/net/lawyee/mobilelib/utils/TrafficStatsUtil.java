package net.lawyee.mobilelib.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.util.Log;

public class TrafficStatsUtil {

	private static int mConnectType;
	private static Context mContext;
	private static TrafficStatsUtil mUtil;

	private TrafficStatsUtil(Context context) {
		mContext = context;
		mConnectType = NetUtil.getNetWorkType(mContext);
		Log.e("zhuangtai", mConnectType+"");
	}

	public static TrafficStatsUtil getUtil(Context context) {
		if (mUtil == null)
			mUtil = new TrafficStatsUtil(context);
		return mUtil;
	}

	/**
	 * 保存wifi 的SharedPreferences数据
	 * 
	 * @param context
	 */
	@SuppressLint("SimpleDateFormat")
	public static void saveWIFISharedPreferences(Context context,
			long WIFIChanged) {
		SharedPreferences sp = context.getSharedPreferences("wifi",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong("wifi", WIFIChanged);
		editor.commit();
	}

	/**
	 * 清空wifi数据
	 * 
	 * @param context
	 */
	public static void clearWIFISharedPreferences(Context context) {
		SharedPreferences sp = context.getSharedPreferences("wifi",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 读取WIFI的SharedPreferences数据
	 * 
	 * @param context
	 * @return user
	 */
	public static long getWIFISharedPreferences(Context context) {
		SharedPreferences sp = context.getSharedPreferences("wifi",
				Context.MODE_PRIVATE);

		return sp.getLong("wifi", 0);
	}

	/**
	 * 保存2G/3G 的SharedPreferences数据
	 * 
	 * @param context
	 */
	@SuppressLint("SimpleDateFormat")
	public static void saveGPRSSharedPreferences(Context context,
			long GPRSChanged) {
		SharedPreferences sp = context.getSharedPreferences("gprs",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong("gprs", GPRSChanged);
		editor.commit();
	}

	/**
	 * 清空2G/3G数据
	 * 
	 * @param context
	 */
	public static void clearGPRSSharedPreferences(Context context) {
		SharedPreferences sp = context.getSharedPreferences("gprs",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 读取2G/3G的SharedPreferences数据
	 * 
	 * @param context
	 * @return user
	 */
	public static long getGPRSSharedPreferences(Context context) {
		SharedPreferences sp = context.getSharedPreferences("gprs",
				Context.MODE_PRIVATE);

		return sp.getLong("gprs", 0);
	}

	/**
	 * 保存旧的 的SharedPreferences数据
	 * 
	 * @param context
	 */
	@SuppressLint("SimpleDateFormat")
	public static void saveOldSharedPreferences(Context context, long Old) {
		SharedPreferences sp = context.getSharedPreferences("Old",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong("Old", Old);
		editor.commit();
	}

	/**
	 * 清空旧的 数据
	 * 
	 * @param context
	 */

	public static void clearOldSharedPreferences(Context context) {
		SharedPreferences sp = context.getSharedPreferences("Old",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 读取2G/3G的SharedPreferences数据
	 * 
	 * @param context
	 * @return user
	 */
	public static long getOldSharedPreferences(Context context) {
		SharedPreferences sp = context.getSharedPreferences("Old",
				Context.MODE_PRIVATE);

		return sp.getLong("Old", -1);
	}

	/**
	 * 程序退出时的流量判断
	 * 
	 * @param ct
	 * @param uid
	 */
	public static void getTrafficByExit(Context ct, int uid) {

		//
		// ConnectivityManager connectMgr = (ConnectivityManager) ct
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo mobNetInfo = connectMgr
		// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// NetworkInfo wifiNetInfo = connectMgr
		// .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//
		//
		// JudgeGPRSorWIFI(ct, uid, mobNetInfo, wifiNetInfo);

		int newConnectionType = NetUtil.getNetWorkType(ct);


		long oldTraffic;
		long newTraffic;
		long nowTraffic;
		long changeTraffic;
		long WIFITraffic;
		long GPRSTraffic;

		nowTraffic = TrafficStats.getUidRxBytes(uid);
		oldTraffic = getOldSharedPreferences(ct);
		newTraffic = nowTraffic;
		changeTraffic = newTraffic - oldTraffic;

		// 先计算旧的
		if (newConnectionType == NetUtil.CINT_CONNECTTYPE_MOBILE) {
			GPRSTraffic = getGPRSSharedPreferences(ct);
			GPRSTraffic = GPRSTraffic + changeTraffic;
			saveGPRSSharedPreferences(ct, GPRSTraffic);
			saveOldSharedPreferences(ct, newTraffic);
			Log.e("GPRSTraffic ", "" + GPRSTraffic);

		} else if (newConnectionType == NetUtil.CINT_CONNECTTYPE_WIFI) {
			WIFITraffic = getWIFISharedPreferences(ct);
			WIFITraffic = WIFITraffic + changeTraffic;
			saveWIFISharedPreferences(ct, WIFITraffic);
			saveOldSharedPreferences(ct, newTraffic);
			Log.e("WIFITraffic", "" + WIFITraffic);
		}
		
		
		
//		JudgeGPRSorWIFI(ct, uid, newConnectionType, newConnectionType);

	}

	/**
	 * 联网状态变化时流量记录
	 * 
	 * @param ct
	 * @param uid
	 */
	public static void getTrafficByChangeType(Context ct, int uid) {

		// ConnectivityManager connectMgr = (ConnectivityManager) ct
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo mobNetInfo = connectMgr
		// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// NetworkInfo wifiNetInfo = connectMgr
		// .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//
		// Log.e("wifi",wifiNetInfo.isConnected()+"");
		// Log.e("gprs",mobNetInfo.isConnected()+"");
		// JudgeGPRSorWIFI(ct, uid ,wifiNetInfo, mobNetInfo);

		int newConnectionType = NetUtil.getNetWorkType(ct);

		long oldTraffic;
		long newTraffic;
		long nowTraffic;
		long changeTraffic;
		long WIFITraffic;
		long GPRSTraffic;

		nowTraffic = TrafficStats.getUidRxBytes(uid);
		oldTraffic = getOldSharedPreferences(ct);
		newTraffic = nowTraffic;
		changeTraffic = newTraffic - oldTraffic;

		// 先计算旧的
		if (mConnectType == NetUtil.CINT_CONNECTTYPE_MOBILE) {
			GPRSTraffic = getGPRSSharedPreferences(ct);
			GPRSTraffic = GPRSTraffic + changeTraffic;
			saveGPRSSharedPreferences(ct, GPRSTraffic);
			saveOldSharedPreferences(ct, newTraffic);
			Log.e("GPRSTraffic ", "" + GPRSTraffic);

		} else if (mConnectType == NetUtil.CINT_CONNECTTYPE_WIFI) {
			WIFITraffic = getWIFISharedPreferences(ct);
			WIFITraffic = WIFITraffic + changeTraffic;
			saveWIFISharedPreferences(ct, WIFITraffic);
			saveOldSharedPreferences(ct, newTraffic);
			Log.e("WIFITraffic", "" + WIFITraffic);
		}

		mConnectType = newConnectionType;
		
		
		
		
		
//		JudgeGPRSorWIFI(ct, uid, newConnectionType, mConnectType);
	}

	/**
	 * 联网状态变化时判断类型
	 * 
	 * @param ct
	 */
	public static void JudgeGPRSorWIFI(Context ct, int uid,
			int newConnectionType, int oldConnectType) {

		long oldTraffic;
		long newTraffic;
		long nowTraffic;
		long changeTraffic;
		long WIFITraffic;
		long GPRSTraffic;

		nowTraffic = TrafficStats.getUidRxBytes(uid);
		oldTraffic = getOldSharedPreferences(ct);
		newTraffic = nowTraffic;
		changeTraffic = newTraffic - oldTraffic;

		// 先计算旧的
		if (oldConnectType == NetUtil.CINT_CONNECTTYPE_MOBILE) {
			GPRSTraffic = getGPRSSharedPreferences(ct);
			GPRSTraffic = GPRSTraffic + changeTraffic;
			saveGPRSSharedPreferences(ct, GPRSTraffic);
			saveOldSharedPreferences(ct, newTraffic);
			Log.e("GPRSTraffic ", "" + GPRSTraffic);

		} else if (oldConnectType == NetUtil.CINT_CONNECTTYPE_WIFI) {
			WIFITraffic = getWIFISharedPreferences(ct);
			WIFITraffic = WIFITraffic + changeTraffic;
			saveWIFISharedPreferences(ct, WIFITraffic);
			saveOldSharedPreferences(ct, newTraffic);
			Log.e("WIFITraffic", "" + WIFITraffic);
		}

		oldConnectType = newConnectionType;

	}

}
