/**
* Project:newsreader2
* File:FavoriteUtil.java
* Copyright 2013 WUZHUPC Co., Ltd. All rights reserved.
*/
package net.lawyee.mobilelib.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import net.lawyee.mobilelib.Constants;
import net.lawyee.mobilelib.vo.BaseVO;

/**
 * 收藏夹工具类
 * @author wuzhu email:wuzhupc@gmail.com
 * @version 创建时间：2012-12-2 下午8:22:29
 */
@SuppressWarnings("rawtypes")
public class FavoriteUtil
{
	protected static final String TAG = FavoriteUtil.class.getSimpleName();
	
	/**
	 * 收藏夹文件
	 */
	private static String CSTR_FAVFILE = "/favinfo.dat";
	
	/**
	 * 
	 */
	private static FavoriteUtil favoriteUtil;

	private Context mContext;
	
	/**
	 * 数据列表
	 */
	private ArrayList mDataList;

	public ArrayList getDataList()
	{
		return mDataList;
	}
	
	private  FavoriteUtil(Context c)
	{
		mContext = c;
		initFavDataList();
	}
	
	/**
	 * 初始化收藏数据
	 */
	@SuppressWarnings("unchecked")
	private void initFavDataList()
	{
		mDataList = new ArrayList();
		File favfile = new File(Constants.getDataStoreDir(mContext)+CSTR_FAVFILE);
		if(!favfile.exists()) return;
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try 
		{
			fIn = new FileInputStream(favfile);
			isr = new InputStreamReader(fIn,"UTF-8");
			br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null)
			{ 
				//从数据中读取内容　１行一条记录
				Object o = SerializeUtil.getObjectFromString(temp);
				//反序列化读取到的字符串加入到mDataList中
				if(o!=null&&o instanceof BaseVO)
					mDataList.add(o);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			//Log.e(TAG, e.getMessage());
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				//Log.e(TAG, e.getMessage());
			}
		}		
	}
	
	/**
	 * 移除指定索引的收藏数据
	 * @param index
	 */
	public boolean removeFavData(int index)
	{
		if(index<0||mDataList==null||mDataList.isEmpty()||index>=mDataList.size())
			return false;
		mDataList.remove(index);
		saveFavDataList();
		return true;
	}
	
	/**
	 * 移除指定收藏数据
	 * @param obj
	 */
	public boolean removeFavData(BaseVO obj)
	{
		return removeFavData(hasFavData(obj));
	}
	
	/**
	 * 返回是收藏索引值，如果没有，返回-1
	 * @param obj
	 * @return
	 */
	public int hasFavData(BaseVO obj)
	{
		if(obj==null||mDataList==null||mDataList.isEmpty())
			return -1;
		for(int i=0;i<mDataList.size();i++)
		{
			if(obj.equals(mDataList.get(i)))
				return i;
		}
		return -1;
	}
	
	/**
	 * 增加收藏数据
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean addFavData(BaseVO obj)
	{
		if(obj==null||mDataList==null)
			return false;
		int index = hasFavData(obj);
		if(index!=-1)
			return true;
		//新增加的收藏记录排在第一位
		mDataList.add(0, obj);
		saveFavDataList();
		return true;
	}
	
	/**
	 * 存储
	 */
	private void saveFavDataList()
	{
		String filepath = Constants.getDataStoreDir(mContext)+CSTR_FAVFILE;
		File favfile = new File(filepath);
		if(mDataList==null||mDataList.isEmpty())
		{
			//删除收藏文件
			if(!favfile.exists()) return;
			favfile.delete();
			return;
		}
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		//BufferedWriter bw=null;
		try
		{
			FileUtil.isExistFolderFromFile(filepath); // 文件夹存在与否检测，不存在则创建
			File f = new File(filepath);
			fOut = new FileOutputStream(f);
			osw = new OutputStreamWriter(fOut,Charset.forName("UTF-8"));
			
			osw.write(SerializeUtil.objectSerialzeTOString(mDataList.get(0)));
			for(int i=1;i<mDataList.size();i++)
			{
				osw.write("\r\n");
				osw.write(SerializeUtil.objectSerialzeTOString(mDataList.get(i)));
			}
			osw.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
			//Log.e(TAG, e.toString());
		} finally
		{
			try
			{
				if (osw != null)
					osw.close();
				if (fOut != null)
					fOut.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				//Log.e(TAG, e.getMessage());
			}
		}
		
	}

	public static FavoriteUtil getInstance(Context c)
	{
		if(favoriteUtil==null)
			favoriteUtil=new FavoriteUtil(c);
		return favoriteUtil;
	}
}
