package net.lawyee.mobilelib.utils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 * @author wuzhu
 * @date 2013-12-17 上午10:01:45
 * @version $id$
 */
@SuppressLint("DefaultLocale")
public class NetUtil
{
	public static final int CINT_CONNECTTYPE_NOTCONNECT = -1;
	public static final int CINT_CONNECTTYPE_NONE = 0;
	public static final int CINT_CONNECTTYPE_MOBILE = 1;
	public static final int CINT_CONNECTTYPE_WIFI = 2;
	/**
	 * 获得当前联网类型
	 * 
	 * @return -1: 无连接类型; 
	 * 0 其它连接方式;
	 * 2 ConnectivityManager.TYPE_WIFI: wifi;
	 * 1 ConnectivityManager.TYPE_MOBILE:mobile;
	 * 
	 * 判断网络连接状态为时mobile最好增加判断是net还是wap网络
	 */
	public static int getNetWorkType(Context c)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if(connectivityManager==null)
			return CINT_CONNECTTYPE_NOTCONNECT;
		
		NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
		
		if (networkInfo != null&&networkInfo.isAvailable())
		{
			if(networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)
				return CINT_CONNECTTYPE_MOBILE;
			if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI)
				return CINT_CONNECTTYPE_WIFI;
			return CINT_CONNECTTYPE_NONE;
		}
		
		return CINT_CONNECTTYPE_NOTCONNECT;
	}

	public static boolean hasNetWork(Context c)
	{
		int nettype = getNetWorkType(c);
		return nettype == NetUtil.CINT_CONNECTTYPE_MOBILE
				&& nettype == NetUtil.CINT_CONNECTTYPE_WIFI;
	}
	
	/**
	 * 是否是有效的网络url字符串
	 * @param str
	 * @return
	 */
	public static boolean isEffUrlStr(String str)
	{
		if(StringUtil.isEmpty(str))
			return false;
		String tmpstr = str.toLowerCase();
		return tmpstr.startsWith("http://")||tmpstr.startsWith("https://");
	}
	
	/**
	 * 
	 * 方法描述：测试链接是否可以访问
	 * @param url
	 * @return
	 */
	public static boolean isCanAccessUrl(String url) {
		if(StringUtil.isEmpty(url))
			return false;
		URL urlTemp = null;
		HttpURLConnection connt = null;
		try {
			urlTemp = new URL(url);
			connt = (HttpURLConnection) urlTemp.openConnection();
			connt.setRequestMethod("HEAD");
			int returnCode = connt.getResponseCode();
			return returnCode == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connt!=null)
				connt.disconnect();
		}
		return false;
	}
	
	/**
	 * 是否是有效的url格式
	 * @param str
	 * @return
	 * @note 需改进
	 */
	public static boolean isEffectUrl(String str)
	{
		if(StringUtil.isEmpty(str))
			return false;
		if(str.indexOf(":")==-1)
			return false;
		try {
			new URL(str);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}
}
