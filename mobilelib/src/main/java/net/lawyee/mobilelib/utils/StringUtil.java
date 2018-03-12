package net.lawyee.mobilelib.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Base64;

/**
 * 字符串工具类
 * @author wuzhu
 * @date 2013-12-17 上午10:03:29
 * @version $id$
 */
public class StringUtil
{

	/**
	 * 空字符串
	 */
	public static final String STR_EMPTY = "";

	/**
	 * 字符串是否为空，包括空字符串或null
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		return str == null || "".equals(str.trim());
	}

	public static boolean isEmpty(CharSequence str)
	{
		if(str==null)
			return  true;
		return isEmpty(str.toString());
	}

	public static boolean isEqual(String str,String str2)
	{
		if(isEmpty(str)&&!isEmpty(str2))
			return false;
		if(isEmpty(str)&&isEmpty(str2))
			return true;
		return str.equals(str2);
	}

	/**
	 * 字符串转为InputStream
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream StringToInputStream(String str)
	{
		if (StringUtil.isEmpty(str))
			return null;
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}
	public static boolean requiresMb4(String s) {
		if(StringUtil.isEmpty(s))
			return false;
		int len = s.length();
		return len != s.codePointCount(0, len);
	}
	public static boolean checkUtf8Mb4(String text)  {
		byte[] bytes = null;
		try {
			bytes = text.getBytes("utf-8");
		}catch (UnsupportedEncodingException ex)
		{
			return false;
		}
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				i++;
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				i += 2;
				return true;
			}
			else if ((b ^ 0xE0) >> 4 == 0) {
				i += 3;
				return true;
			}
			else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
				return true;
			}
		}
		return false;
	}

	/**
	 * Java过滤掉非UTF-8字符方法
	 * @param text
	 * @return
     */
	public static  String filterOffUtf8Mb4(String text) {
		if(StringUtil.isEmpty(text))
			return text;
		return text.replaceAll("[^\\u0000-\\uD7FF\\uE000-\\uFFFF]", "");
		/*byte[] bytes = null;
		try {
			bytes = text.getBytes("utf-8");
		}catch (UnsupportedEncodingException ex)
		{
			return text;
		}
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			}
			else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			}
			else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}else
			{
				i++;
			}
		}
		buffer.flip();
		try {
			return new String(buffer.array(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return text;
		}*/
	}

	/**
	 * 字符串进行base64编码
	 * 
	 * @param text
	 * @return
	 */
	public static String toBase64fromStr(String text)
	{
		if (StringUtil.isEmpty(text))
			return STR_EMPTY;
		byte[] data = null;
		try
		{
			data = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
			return STR_EMPTY;
		}
		return Base64.encodeToString(data, Base64.DEFAULT);
	}

	/**
	 * 将base64位编码字符串解码
	 * 
	 * @param text
	 * @return
	 */
	public static String fromBase64toString(String text)
	{
		if (StringUtil.isEmpty(text))
			return STR_EMPTY;
		byte[] data = Base64.decode(text, Base64.DEFAULT);
		if (data == null || data.length == 0)
			return STR_EMPTY;
		String text1 = null;
		try
		{
			text1 = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return STR_EMPTY;
		}
		return text1;
	}
	
	/// <summary>
	/// 从适用于URL的Base64编码字符串转换为普通字符串
	/// </summary>
	public static String fromBase64StringForUrl(String base64String)
	{
		String temp = base64String.replace('.', '=').replace('*', '+').replace('-', '/');
		return fromBase64toString(temp);
	}

	/// <summary>
	/// 从普通字符串转换为适用于URL的Base64编码字符串
	/// </summary>
	public static String toBase64StringForUrlParam(String normalString)
	{
	   return toBase64fromStr(normalString).replace('+','*').replace('/', '-').replace('=', '.');
	}
	
	/** 
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符） 
     *  
     * @param c 需要判断的字符 
     * @return 返回true,Ascill字符 
     */  
    public static boolean isLetter(char c) {  
        int k = 0x80;  
        return c / k == 0 ? true : false;  
    }  
  
    /** 
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1 
     *  
     * @param s 需要得到长度的字符串 
     * @return i得到的字符串长度 
     */  
    public static int length(String s) {  
        if (isEmpty(s))  
            return 0;  
        char[] c = s.toCharArray();  
        int len = 0;  
        for (int i = 0; i < c.length; i++) {  
            len++;  
            if (!isLetter(c[i])) {  
                len++;  
            }  
        }  
        return len;  
    }

	
    /**
     * 是否为有效的字符串
     * @param str
     * @param minlen
     * @param maxlen
     * @return
     */
	public static boolean isEffValue(String str,int minlen,int maxlen)
	{
		int len =StringUtil.length(str);
		return len>=minlen&&len<=maxlen;
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
	public  static boolean validateMoblie(String phone)
	{
		if(isEmpty(phone)||phone.length()!=11)
			return false;
		String reg="^(1[3-5,8])\\d{9}$";
		return phone.matches(reg);
	}
	
	/**
	 * 验证身份证号
	 * @return
	 */
	public static boolean validateidCard(String idcard)
	{
		if(isEmpty(idcard)||(idcard.length()!=15&&idcard.length()!=18))
			return false;
		Pattern p = Pattern.compile("(\\d{17}[0-9a-zA-Z]|\\d{14}[0-9a-zA-Z])");
		Matcher matcher =p.matcher(idcard);
		return matcher.matches();
	}
	
	/**
	 * 验证有效的email地址
	 * @param strEmail
	 * @return
	 */
	public static boolean validateEmail(String strEmail) {
		String strPattern ="[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"; 
				//"^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	
	/** 
	 * 验证邮政编码  
	 */
    public static boolean validatePostcode(String post){  
        if(post.matches("[1-9]\\d{5}(?!\\d)")){  
            return true;  
        }else{  
            return false;  
        }  
    }  
}
