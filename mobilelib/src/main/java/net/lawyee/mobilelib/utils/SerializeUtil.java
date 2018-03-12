package net.lawyee.mobilelib.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.util.Base64;


/**
 * 序列化工具类
 * @author wuzhu email:wuzhupc@gmail.com
 * @version 创建时间：2012-12-2 下午8:15:39
 */
public class SerializeUtil
{
	//private static final String TAG = SerializeUtil.class.getSimpleName();
	
	/**
	 * 序列化为字符串
	 * @param obj
	 * @return
	 */
	public static String objectSerialzeTOString(Object obj)
	{
		String objBody = null;
		ByteArrayOutputStream baops = null;
		ObjectOutputStream oos = null;
		try
		{
			baops = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baops);
			oos.writeObject(obj);
			byte[] bytes = baops.toByteArray();
			objBody = new String(Base64.encode(bytes, Base64.NO_WRAP));
			//objBody = new String(bytes);
		} catch (IOException e)
		{
			e.printStackTrace();
			//Log.e(TAG, e.toString());
		} finally
		{
			try
			{
				if (oos != null)
					oos.close();
				if (baops != null)
					baops.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				//Log.e(TAG, e.toString());
			}
		}
		return objBody;
	}
	
	/**
	 * 字符串反序列化为对象
	 * @param objBody
	 * @param clazz
	 * @return
	 */
	public static Object getObjectFromString(String objBody)
	{
		if(StringUtil.isEmpty(objBody))
			return null;
		ObjectInputStream ois = null;
		Object obj = null;
		try
		{
			byte[] bytes=Base64.decode(objBody, Base64.NO_WRAP);
			if(bytes==null||bytes.length==0)
				return null;
			ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			obj = ois.readObject();
		} catch (IOException e)
		{
			e.printStackTrace();
			//Log.e(TAG, e.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
			//Log.e(TAG, e.toString());
		} finally
		{
			try
			{
				if (ois != null)
					ois.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				//Log.e(TAG, e.toString());
			}
		}

		return obj;
	}
	

	
	/**
	 * 存储对象到文件
	 * @param filename
	 * @param delold 是否删除旧的数据（主要是在vo为null时起作用，因为vo不为空时，文件将是覆盖存储)
	 * @return 
	 */
	public static boolean save(String filename,Object vo,Boolean delold)
	{
		if(StringUtil.isEmpty(filename))
			return false;
		if(delold&&FileUtil.isExistFile(filename))
		{
			FileUtil.deleteFile(filename);
		}
		if (vo == null)
			return true;
		if(!FileUtil.isExistFolderFromFile(filename))
				return false;
		String str = SerializeUtil.objectSerialzeTOString(vo);
		if(StringUtil.isEmpty(str))
			return false;
		FileUtil.saveContent(filename, str);
		return true;
	}
	
	/**
	 * 反序列化读取对象
	 * @param filename
	 * @return
	 */
	public static Object load(String filename)
	{
		if (FileUtil.isExistFile(filename))
		{
			String str = FileUtil.readContent(filename);
			if(StringUtil.isEmpty(str))
				return null;
			Object o= SerializeUtil.getObjectFromString(str);
			return o;
		}
		return null;
	}
	
	/**
	 * 从文件读取列表
	 * @param filename
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> loadArraylistFromFile(String filename)
	{
		if(StringUtil.isEmpty(filename)||!FileUtil.isExistFile(filename))
			return null;
		ArrayList<T> result =new ArrayList<T>();
		
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try
		{
			fIn = new FileInputStream(filename);
			isr = new InputStreamReader(fIn,"UTF-8");
			br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null)
			{
				//从数据中读取内容　１行一条记录
				Object o = SerializeUtil.getObjectFromString(temp);
				
				//反序列化读取到的字符串加入到mDataList中
				if(o!=null)
					result.add((T) o);
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
		
		return result;
	}
	
	/**
	 * 存储列表到文件
	 * @param lists
	 * @param filename
	 * @param bdelold
	 * @return
	 */
	public static <T> boolean saveArraylistToFile(ArrayList<T> lists,String filename,boolean bdelold)
	{
		if(StringUtil.isEmpty(filename))
			return false;
		if(bdelold&&FileUtil.isExistFile(filename))
		{
			FileUtil.deleteFile(filename);
		}
		if(lists==null||lists.isEmpty())
			return true;
		if(!FileUtil.isExistFolderFromFile(filename))
			return false;
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		try
		{
			FileUtil.isExistFolderFromFile(filename); // 文件夹存在与否检测，不存在则创建
			File f = new File(filename);
			fOut = new FileOutputStream(f);
			osw = new OutputStreamWriter(fOut,Charset.forName("UTF-8"));
			
			osw.write(SerializeUtil.objectSerialzeTOString(lists.get(0)));
			for(int i=1;i<lists.size();i++)
			{
				osw.write("\r\n");
				osw.write(SerializeUtil.objectSerialzeTOString(lists.get(i)));
			}
			osw.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
			//Log.e(TAG, e.toString());
			return false;
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
				return false;
			}
		}
		return true;
	}
}
