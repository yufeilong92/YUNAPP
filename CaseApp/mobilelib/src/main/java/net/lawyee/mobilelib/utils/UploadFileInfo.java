/**
* Project:newsreader2
* File:UploadFileInfo.java
* Copyright 2013 WUZHUPC Co., Ltd. All rights reserved.
*/
package net.lawyee.mobilelib.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wuzhu
 * @date 2013-6-9 下午9:24:27
 * @version $id$
 */
public class UploadFileInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3498503100057582831L;

	/**
	 * 本地文件名称（完整路径）
	 */
	private String locFileName;
	
	/**
	 * 上传后记录的访问地址
	 */
	private String accessAddress;
	
	public UploadFileInfo()
	{
		this("","");
	}
	
	public UploadFileInfo(String locfilename,String url)
	{
		setLocFileName(locfilename);
		setAccessAddress(url);
	}

	public String getLocFileName()
	{
		return locFileName;
	}

	public void setLocFileName(String locFileName)
	{
		this.locFileName = locFileName;
	}

	public String getAccessAddress()
	{
		return accessAddress;
	}

	public void setAccessAddress(String accessAddress)
	{
		this.accessAddress = accessAddress;
	}
	
	public void clear()
	{
		setLocFileName("");
		setAccessAddress("");
	}
	
	/**
	 * 返回用;分享的字符串(字符串最后不包括;)
	 * @param listdata
	 * @return
	 * @note UploadFileInfo类型只返回AccessAddress
	 */
	public static String listToString(List<UploadFileInfo> listdata)
	{
		StringBuilder result=new StringBuilder();
		if(listdata!=null&&!listdata.isEmpty())
		{
			for (int i = 0; i < listdata.size(); i++)
			{
				Object o=listdata.get(i);
				if(o instanceof UploadFileInfo)
				{
					result.append(((UploadFileInfo)o).getAccessAddress());
				}
				else
					result.append(o);
				if(i!=listdata.size()-1)
					result.append(";");
			}
		}
		return result.toString();
	}
	
	/**
	 * 将用;分享的字符串转为UploadFileInfo列表
	 * @param str
	 * @return
	 */
	public static List<UploadFileInfo> StringToList(String str)
	{
		if(StringUtil.isEmpty(str))
			return null;
		List<UploadFileInfo> list=new ArrayList<UploadFileInfo>();
		
		String[] strlist=str.split(";");
		for(int i=0;i<strlist.length;i++)
		{
			UploadFileInfo ufi=new UploadFileInfo("", strlist[i]);
			list.add(ufi);
		}
		return list;
	}
}
