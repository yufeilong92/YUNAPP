package net.lawyee.mobilelib.utils;

/**
 * java基本数据类型辅助类
 * @author wuzhu
 * @date 2013-12-17 上午10:00:29
 * @version $id$
 */
public class JavaLangUtil
{
	/**
	 * 
	 * @param str
	 * @param defvalue
	 * @return
	 */
	public static int StrToInteger(String str,int defvalue)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch (Exception e) {
			return defvalue;
		}
	}
	
	/**
	 * 
	 * @param str
	 * @param defvalue
	 * @return
	 */
	public static Long StrToLong(String str,Long defvalue)
	{
		try
		{
			return Long.parseLong(str);
		}
		catch (Exception e) {
			return defvalue;
		}
	}
	
	/**
	 * 
	 * @param str 当值得为true或1时返回true
	 * @param defvalue
	 * @return
	 */
	public static Boolean StrToBool(String str,Boolean defvalue)
	{
		if(StringUtil.isEmpty(str))
			return defvalue;
		return str.equalsIgnoreCase("true")||str.equals("1");
	}

	public static  float StrToFloat(String str,float defvalue)
	{
		try
		{
			return Float.parseFloat(str);
		}
		catch (Exception e) {
			return defvalue;
		}
	}

	public static  double StrToDouble(String str,double defvalue)
	{
		try
		{
			return Double.parseDouble(str);
		}
		catch (Exception e) {
			return defvalue;
		}
	}
}
