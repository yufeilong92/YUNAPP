package net.lawyee.mobilelib.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 * @author wuzhu
 * @date 2013-12-17 上午10:01:27
 * @version $id$
 */
public class MD5
{
	/**
	 * 生成md5
	 * 
	 * @param string
	 * @return
	 */
	public static String encode(String string)
	{
		if (StringUtil.isEmpty(string))
			return string;
		try
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return bytesToHexString(digest.digest(string.getBytes()));
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static String bytesToHexString(byte[] bytes)
	{
		if (bytes == null || bytes.length == 0)
			return "";

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			int val = ((int) bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
