package net.lawyee.mobilelib;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import net.lawyee.mobilelib.utils.StorageUtils;

/**
 * 常量
 * @author wuzhu
 * @note
 * 如果App缓存数据不保存在默认路径下，请在应用程序第一次涉及数据缓存路径（读取或保存）时变更此变量的值。
 * 比如：在BaseActivity的OnCreate中 Constants.DataFileDir = "myaddfiledir";
 */
public class Constants 
{
	/**
	 * 设备类型 1 android 2 iphone 3 ipad 
	 */
	public static final int CINT_DEVTYPE = 1;

	/** 连接超时设置 /s */
	public static final int CONNECT_TIME_OUT = 6;

	/** 读取超时设置 /s */
	public static final int READ_TIME_OUT = 60;

	/** Get请求方式 */
	public static final int METHOD_GET = 0;

	/** Post请求方式 */
	public static final int METHOD_POST = 1;

	/** 每帧读取的数据流量 */
	public static final int READ_DATA_LENGTH = 1024;
	
	/**
	 * 用于设置不同应用的数据缓存路径
	 * 如果App缓存数据不保存在默认路径下，请在应用程序第一次涉及数据缓存路径（读取或保存）时变更此变量的值。
	 * 比如：在BaseActivity的OnCreate中 Constants.DataFileDir = "myaddfiledir";
	 */
	public static String DataFileDir = "lawyee";
	
	/**
	 * 缓存数据存储路径,最后不包含路径符号/
	 */
	public static String getDataStoreDir(Context c)
	{
		File ownfile = StorageUtils.getOwnCacheDirectory(c,DataFileDir);
		return ownfile.getAbsolutePath();
		//return Environment.getExternalStorageDirectory() +File.separator +DataFileDir+File.separator;
	}
	
	/**
	 * 信息详情保存路径,包含路径符号/
	 */
	public static final String CSTR_DETAILDIR = "/detail"+File.separator;
	/**
	 * 缓存保存路径,包含路径符号/
	 */
	public static final String CSTR_IMAGECACHEDIR = "/.images"+File.separator;
	
	/**
	 *头像保存路径,包含路径符号/
	 */
	public static final String CSTR_PHOTOCACHEDIR = "/.photo"+File.separator;
}
