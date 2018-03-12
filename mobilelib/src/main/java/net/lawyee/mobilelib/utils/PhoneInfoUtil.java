package net.lawyee.mobilelib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 手机相关信息
 * @author wuzhu
 * @date 2013-4-24 下午2:48:41
 * @version $id$
 */
public class PhoneInfoUtil
{
	/**
	 * 获取验证号
	 * 
	 * @param c
	 * @param bsim
	 *            true时：sim卡有的问优先，先取手机号没有则取IMSI，再取不到返回IMEI号。false时直接返回IMEI号
	 * @return
	 */
	public static String GetValidateID(Context c, Boolean bsim)
	{
		if (!bsim)
			return GetIMEI(c);
		String result = GetPhoneNumber(c);
		if (result == null || result.equals(""))
			result = GetIMSI(c);
		if (result == null || result.equals(""))
			result = GetIMEI(c);
		return result;
	}

	/**
	 * 获取手机IMEI信息
	 */
	public static String GetIMEI(Context c)
	{
		TelephonyManager telephonyManager = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		if(StringUtil.isEmpty(imei))
			imei= getLocalMacAddress(c);
		return imei;
	}
	
	/**
	 * 获取MAC地址
	 * @param c
	 * @return
	 */
	public static String getLocalMacAddress(Context c) {
		WifiManager wifi = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取手机号码
	 */
	public static String GetPhoneNumber(Context c)
	{
		TelephonyManager telephonyManager = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}

	/**
	 * 获取IMSI号
	 * 
	 * @param c
	 * @return
	 */
	public static String GetIMSI(Context c)
	{
		TelephonyManager telephonyManager = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSubscriberId();
	}
	

	
	/**
	 * 验证是否是手机号码
	 * @param phone 要验证的手机号码
	 * @return　符合条件的返回true，否则返回false
	 * @note 
	 * 移动：134、135、136、137、138、139、147、150、151、157(TD)、158、159、182、187、188
	 * 联通：130、131、132、152、155、156、185、186
	 * 电信：133、153、180、189、（1349卫通） 
	 * "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
	 */
	public  static Boolean validateMoblie(String phone)
	{
		if(StringUtil.isEmpty(phone)||phone.length()!=11)
			return false;
		String reg="^(1[3-5,8])\\d{9}$";
		return phone.matches(reg);
	}

	
	/**
     * 运营商,1 移动 2 联通 3 电信 4 其他
     */
	public static int getPeratorType(Context c) {

		TelephonyManager tm = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
		// imsi 国际移动用户识别码
		String imsi = tm.getSubscriberId();
		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")
					|| imsi.startsWith("46007")) {
				// 中国移动
				return 1;
			} else if (imsi.startsWith("46001")) {
				// 中国联通
				return 2;
			} else if (imsi.startsWith("46003")) {
				// 中国电信
				return 3;
			} else {
				// 无法判断
				Log.v("运营商", "无法判断运营商信息" + " " + imsi);
			}

		} else {
			// imsi为空
		}
		return 4;
	}
	
	/**
	 * 获取当前系统的android版本号
	 */
	public static String getVersion(){
         return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * 获取手机型号
	 */
	public static String getModel(){
		return android.os.Build.MODEL;
	}
}
