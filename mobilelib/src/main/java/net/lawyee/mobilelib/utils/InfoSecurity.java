/**
 * Project:mobilelib
 * File:Security.java
 * Copyright 2015 WUZHUPC Co., Ltd. All rights reserved.
 */
package net.lawyee.mobilelib.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import android.annotation.SuppressLint;
import android.util.Base64;

//import org.apache.commons.codec.binary.Base64;

/**
 * @author wuzhu
 * @date 2015-4-16 下午4:28:35
 * @version $id$ 信息安全相关类
 */
public class InfoSecurity
{
	static Charset CHARSET = Charset.forName("utf-8");

	String mToken;
	String mAppid;

	/**
	 * 
	 * @param token
	 *            每个应用指定的token
	 * @param appid
	 *            每个应用的ID号
	 */
	public InfoSecurity(String token, String appid)
	{
		mToken = token;
		mAppid = appid;
	}

	/**
	 * 生成签名
	 * 
	 * @param
	 * @param timeStamp
	 *            时间戳，对应URL参数的timestamp
	 * @param nonce
	 *            随机串，对应URL参数的nonce
	 * 
	 * @return signature 签名串，对应URL参数的signature，如果未赋值token，则返回空字符串
	 * @note 加密/校验流程如下： 1. 将token、timestamp、nonce三个参数进行连接成一个字符串 2.
	 *       将获得的字符串进行MD5加密 3. 获得加密后的字符串可与signature对比，判断该请求是否合法
	 */
	public String genSignature(String timeStamp, String nonce)
	{
		if (StringUtil.isEmpty(mToken))
			return StringUtil.STR_EMPTY;

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(mToken).append(timeStamp).append(nonce);
		String tmpStr = sBuilder.toString();
		String signature =MD5.encode(tmpStr);
		return signature;
	}

	/**
	 * 生成请求URL
	 * 
	 * @param url
	 * @param appid
	 *            应用id,对应URL参数的appid
	 * @return
	 */
	public String genRequestUrl(String url)
	{
		String timeStamp = getTimeStamp();
		String nonce = getNonce();
		String signature = genSignature(timeStamp, nonce);
		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append(url);
		if (!StringUtil.isEmpty(url))
		{
			if (url.contains("?"))
				sBuilder.append("&");
			else
				sBuilder.append("?");
		}
		boolean haspre = false;
		if (!StringUtil.isEmpty(mAppid))
		{
			sBuilder.append("appid=").append(mAppid);
			haspre = true;
		}
		if (!StringUtil.isEmpty(signature))
		{
			if (haspre)
				sBuilder.append("&");
			sBuilder.append("signature=").append(signature);
			haspre = true;
		}
		if (!StringUtil.isEmpty(timeStamp))
		{
			if (haspre)
				sBuilder.append("&");
			sBuilder.append("timeStamp=").append(timeStamp);
			haspre = true;
		}
		if (!StringUtil.isEmpty(nonce))
		{
			if (haspre)
				sBuilder.append("&");
			sBuilder.append("nonce=").append(nonce);
			haspre = true;
		}
		return sBuilder.toString();
	}

	/**
	 * 生成时间戳
	 * 
	 * @return 返回时间戳，格式为yyyyMMddHHmmss
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStamp()
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 生成6位数字组成的随机字符串
	 * 
	 * @return 生成6位数字组成的随机字符串 （100000~999999）
	 */
	public static String getNonce()
	{
		Random ran = new Random(System.currentTimeMillis());
		return String.valueOf(ran.nextInt(900000) + 100000);
	}

	/**
	 * Encodes a String in AES-256 with a given key
	 * 
	 * @param context
	 * @param password
	 * @param text
	 * @return String Base64 and AES encoded String
	 */
	@SuppressLint("TrulyRandom")
	public static String encode(String keyString, String stringToEncode)
			throws NullPointerException
	{
		if (StringUtil.isEmpty(keyString))
		{
			throw new NullPointerException("Please give Password");
		}

		if (StringUtil.isEmpty(stringToEncode))
		{
			throw new NullPointerException("Please give text");
		}

		try
		{
			SecretKeySpec skeySpec = getKey(keyString);
			byte[] clearText = stringToEncode.getBytes("UTF8");

			// IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
			final byte[] iv = new byte[16];
			Arrays.fill(iv, (byte) 0x00);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

			// Cipher is not thread safe
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

			String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
					//Base64.encodeBase64String(cipher.doFinal(clearText));
			// Log.d("jacek", "Encrypted: " + stringToEncode + " -> " +
			// encrypedValue);
			return encrypedValue;

		} catch (InvalidKeyException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (BadPaddingException e)
		{
			e.printStackTrace();
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		} catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	private static SecretKeySpec getKey(String password)
			throws UnsupportedEncodingException
	{
		int keyLength = 256;
		byte[] keyBytes = new byte[keyLength / 8];
		Arrays.fill(keyBytes, (byte) 0x0);
		byte[] passwordBytes = password.getBytes(CHARSET);
		int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
				: keyBytes.length;
		System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}

	/**
	 * Decodes a String using AES-256 and Base64
	 * 
	 * @param context
	 * @param password
	 * @param text
	 * @return desoded String
	 */
	public static String decode(String password, String text)
			throws NullPointerException
	{
		if (StringUtil.isEmpty(password))
		{
			throw new NullPointerException("Please give Password");
		}

		if (StringUtil.isEmpty(text))
		{
			throw new NullPointerException("Please give text");
		}

		try
		{
			SecretKey key = getKey(password);

			// IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
			final byte[] iv = new byte[16];
			Arrays.fill(iv, (byte) 0x00);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

			byte[] encrypedPwdBytes = Base64.decode(text, Base64.DEFAULT) ;
			//Base64.decodeBase64(text);
			// cipher is not thread safe
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
			byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

			String decrypedValue = new String(decrypedValueBytes);
			return decrypedValue;

		} catch (InvalidKeyException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (BadPaddingException e)
		{
			e.printStackTrace();
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		} catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}