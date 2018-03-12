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
 * 记录已经操作过的信息类
 * @author wuzhu email:wuzhupc@gmail.com
 */
@SuppressWarnings("rawtypes")
public class MobileInfoUtil
{
	protected static final String TAG = MobileInfoUtil.class.getSimpleName();
	
	/**
	 * 操作记录文件夹
	 */
	private static String CSTR_OPERRECORDFILE = "/mobileInfo.dat";
	
	/**
	 * 
	 */
	private static MobileInfoUtil mobileInfoUtil;
	
	/**
	 * 数据列表
	 */
	private ArrayList mDataList;
	private Context mContext;

	public ArrayList getDataList()
	{
		return mDataList;
	}
	
	private  MobileInfoUtil(Context c)
	{
		mContext = c;
		initOperRecordDataList();
	}
	
	/**
	 * 初始化操作记录数据
	 */
	@SuppressWarnings("unchecked")
	private void initOperRecordDataList()
	{
		mDataList = new ArrayList();
		File favfile = new File(Constants.getDataStoreDir(mContext)+CSTR_OPERRECORDFILE);
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
	 * 移除指定索引的操作记录数据
	 * @param index
	 */
	public boolean removeOperRecordData(int index)
	{
		if(index<0||mDataList==null||mDataList.isEmpty()||index>=mDataList.size())
			return false;
		mDataList.remove(index);
		saveOperRecordDataList();
		return true;
	}
	
	/**
	 * 移除指定操作记录数据
	 * @param obj
	 */
	public boolean removeOperRecordData(BaseVO obj)
	{
		return removeOperRecordData(hasOperRecordData(obj));
	}
	
	/**
	 * 返回是操作记录索引值，如果没有，返回-1
	 * @param obj
	 * @return
	 */
	public int hasOperRecordData(BaseVO obj)
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
	 * 增加操作记录数据
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean addOperRecordData(BaseVO obj)
	{
		if(obj==null||mDataList==null)
			return false;
		int index = hasOperRecordData(obj);
		if(index!=-1)
			return true;
		//新增加的操作记录排在第一位
		mDataList.add(0, obj);
		saveOperRecordDataList();
		return true;
	}
	
	/**
	 * 存储
	 */
	private void saveOperRecordDataList()
	{
		File favfile = new File(Constants.getDataStoreDir(mContext)+CSTR_OPERRECORDFILE);
		if(mDataList==null||mDataList.isEmpty())
		{
			//删除操作记录文件
			if(!favfile.exists()) return;
			favfile.delete();
			return;
		}
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		//BufferedWriter bw=null;
		try
		{
			FileUtil.isExistFolderFromFile(Constants.getDataStoreDir(mContext)+CSTR_OPERRECORDFILE); // 文件夹存在与否检测，不存在则创建
			File f = new File(Constants.getDataStoreDir(mContext)+CSTR_OPERRECORDFILE);
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

	public static MobileInfoUtil getInstance(Context c)
	{
		if(mobileInfoUtil==null)
			mobileInfoUtil=new MobileInfoUtil(c);
		return mobileInfoUtil;
	}
}
