package net.lawyee.mobilelib.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * time工具类
 * @author wuzhu
 * @date 2013-12-17 上午10:03:35
 * @version $id$
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil
{
	public static final int CINT_TIME_SECOND = 1000;
	public static final int CINT_TIME_MINUTE = 60*1000;
	public static final int CINT_TIME_HOUR = 3600*1000;
	public static final int CINT_TIME_DAY = 24*3600*1000;

	/**
	 * 返回yyyy-MM-DD
	 * @param datestr 处理：2015-12-22 08:49:21.0
	 * @return
     */
	public static String getCommonDateStr(String datestr)
	{
		if(StringUtil.isEmpty(datestr)||datestr.length()<=10)
			return datestr;
		String tmpStr =  datestr.substring(0,10);
		return tmpStr;
		/*Date date = strToDate(tmpStr,null);
		if(date==null)
			return tmpStr;
		return getChatTime(date.getTime());*/
	}

	/**
	 * 字符串转日期
	 * 
	 * @param str
	 *            字符串
	 * @param def
	 *            默认时间，如果转换失败则返回默认时间
	 */
	public static Date strToDate(String str, Date def)
	{
		return strToDate(str, "yyyy-MM-dd HH:mm:ss", def);
	}

	/**
	 * 字符串转日期
	 * 
	 * @param str
	 *            字符串
	 * @param def
	 *            默认时间，如果转换失败则返回默认时间
	 */
	public static Date strToDate(String str, String formatstr, Date def)
	{
		if (StringUtil.isEmpty(str))
			return def;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(formatstr);
			return sdf.parse(str);
		} catch (Exception e)
		{
			return def;
		}
	}
	
	/**
	 * 计算当前时间-提供的时间间隔
	 * @param str
	 * @return
	 */
	public static long intervalNow(String str)
	{
		return intervalNow(strToDate(str, null));
	}
	
	/**
	 * 计算当前时间-提供的时间间隔
	 * @param date
	 * @return
	 */
	public static long intervalNow(Date date)
	{
		if(date==null)
			return (new Date()).getTime();
		return (new Date()).getTime() - date.getTime();
	}

	/**
	 * 返回两个时间的间隔(取绝对值)，单位ms
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long interval(Date date1, Date date2)
	{
		if (date1 == null && date2 == null)
			return 0;
		if (date1 == null)
			return date2.getTime();
		if (date2 == null)
			return date1.getTime();
		return Math.abs(date1.getTime() - date2.getTime());
	}

	/**
	 * 日期转为字符串
	 * 
	 * @param date
	 *            如果为空，返回当前时间
	 * @return
	 */
	public static String dateToString(Date date)
	{
		if (date == null)
			date = new Date();
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 日期转为字符串
	 * 
	 * @param date
	 *            如果为空，返回当前时间
	 * @param formatstring
	 *            如果为空，则默认格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateToString(Date date, String formatstring)
	{
		if (formatstring == null || formatstring.equals(""))
			formatstring = "yyyy-MM-dd HH:mm:ss";
		if (date == null)
			date = new Date();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(formatstring);
			return sdf.format(date);
		} catch (Exception e)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		}
	}
	
	public static String longToStr(long date,String formatstr)
	{
		return dateToString(new Date(date), formatstr);
	}


	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
			case 0:
				result =  getHourAndMin(timesamp);
				break;
			case 1:
				result = "昨天 " + getHourAndMin(timesamp);
				break;
			case 2:
				result = "前天 " + getHourAndMin(timesamp);
				break;

			default:
				// result = temp + "天前 ";
				result = getTime(timesamp);
				break;
		}

		return result;
	}
}
