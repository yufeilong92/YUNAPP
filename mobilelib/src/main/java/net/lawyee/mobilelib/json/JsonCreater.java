package net.lawyee.mobilelib.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lawyee.mobilelib.Constants;
import net.lawyee.mobilelib.utils.TimeUtil;

import android.util.Log;

import com.google.gson.stream.JsonWriter;

/**
 * Json报文生成
 * @author wuzhu
 * @date 2013-12-17 上午10:05:36
 * @version $id$
 */
public class JsonCreater
{

	/**
	 * auth集
	 */
	private Map<String, String> mSecuritysMap;	// 
	/**
	 *  参数集
	 */
	private Map<String, String> mParamsMap;	//
	
	private JsonCreater() 
	{
		mSecuritysMap = new HashMap<String, String>();
		mParamsMap = new HashMap<String, String>();
	}
	
	/**
	 * 创建Json生成器
	 */
	public static JsonCreater startJson() {
		return new JsonCreater();
	}
	
	 /**
	 * 创建Json生成器
	 */
	 public static JsonCreater startJson(String devid) {
	 JsonCreater creater = JsonCreater.startJson();
	 creater.setParam("devid", devid);
	 creater.setParam("devtype", Constants.CINT_DEVTYPE);
	 return creater;
	 }
	
	/**
	 * 添加auth
	 * @param name auth名，如果为null，则忽略
	 * @param value auth值，如果为null，则忽略
	 */
	public void setSecurity(String name, String value) {
		if (name == null || value == null) {
			Log.e("JsonCreater", "setSecurity("+name+", "+value+")");
			return;
		}
		mSecuritysMap.put(name, value);
	}
	
	/**
	 * 添加参数
	 * @param name 参数名，如果为null，则忽略
	 * @param value 参数值，如果为null，则忽略
	 */
	public void setParam(String name, String value) {
		if (name == null || value == null) {
			Log.e("JsonCreater", "setParam("+name+", "+value+")");
			return;
		}
		mParamsMap.put(name, value);
	}
	
	public void setParam(String name,Integer value)
	{
		if(value!=null)
			setParam(name,String.valueOf(value));
	}
	
	public void setParam(String name,Long value)
	{
		if(value!=null)
			setParam(name,String.valueOf(value));
	}
	
	public void setParam(String name,Date value)
	{
		setParam(name,value==null?TimeUtil.dateToString(new Date()):TimeUtil.dateToString(value));
	}
	
	/**
	 * 生成json字符串
	 * @param commandname
	 * @return
	 */
	public String createJson(String commandname)
	{
		String id = TimeUtil.dateToString(null, "yyyyMMddHHmmss");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			JsonWriter writer=new JsonWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.setHtmlSafe(true);
			writer.setSerializeNulls(true);
//			writer.beginObject();//1
//			writer.name(TAG_ROOT);
			writer.beginObject();//2
			writer.name(TAG_ID).value(id);
			writer.name(TAG_COMMANDNAME).value(commandname);
			if(!mSecuritysMap.isEmpty())
			{
				writer.name(TAG_SECURITYS);
				writer.beginObject();//3
				addChild(writer,mSecuritysMap);
				writer.endObject();//3
			}
			if(!mParamsMap.isEmpty())
			{
				writer.name(TAG_PARAMS);
				writer.beginObject();//3
				addChild(writer,mParamsMap);
				writer.endObject();//3
			}
			writer.endObject();//2
//			writer.endObject();//1
			writer.close();
			return os.toString("UTF-8");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	private void addChild(JsonWriter writer, Map<String, String> map) throws IOException 
	{
		Iterator<?> it = map.entrySet().iterator();
		while (it.hasNext()) 
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();
			writer.name(name).value(value);
		}
	}

	
	//private static final String NAME_SPACE = "";
	//private static final String TAG_ROOT = "newsreader_request";
	private static final String TAG_ID = "id";
	private static final String TAG_SECURITYS = "securities";
	//private static final String TAG_SECURITY = "security";
	private static final String TAG_COMMANDNAME = "command";
	private static final String TAG_PARAMS = "params";
	//private static final String TAG_PARAM = "param";
}
