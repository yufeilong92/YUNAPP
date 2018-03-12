package net.lawyee.mobilelib.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lawyee.mobilelib.utils.StringUtil;
import net.lawyee.mobilelib.vo.ResponseVO;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * Json报文解析
 * @author wuzhu
 * @note 
 * 注意：请在使用第一次涉及JSON数据解析时变更此变量的值。
 * 比如：在BaseActivity的OnCreate中 JsonParser.PACKAGE_NAME = "net.lawyee.whzyapp.vo.";
 */
public class JsonParser
{
	/**
	 * 解析的VO类的包名，注意"."结束
	 * 注意：请在使用第一次涉及JSON数据解析时变更此变量的值。
	 * 比如：在BaseActivity的OnCreate中 JsonParser.PACKAGE_NAME = "net.lawyee.whzyapp.vo.";
	 */
	public static String PACKAGE_NAME = "net.lawyee.vo.";
	//private final static String TAG_START="newsreader_response";
	//private final static String TAG_ID = "value";
	private final static String TAG_RET = "ret";
	private final static String TAG_DATA = "data";
	/**
	 * 判断返回的结果
	 * @param jsoncontent
	 * @return
	 */
	public static ResponseVO parseJsonToResponse(String jsoncontent)
	{
		return parseJsonToResponse(StringUtil.StringToInputStream(jsoncontent));
	}
	/**
	 * 判断返回的结果
	 * @param jsoncontent
	 * @return
	 */
	public static ResponseVO parseJsonToResponse(InputStream jsoncontent)
	{
		ResponseVO vo = new ResponseVO();
		JsonReader reader=parseJson(jsoncontent, vo);
		if(reader!=null)
		{
			try
			{
				reader.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return vo;
	}
	
	/**
	 * 读取ret部分
	 * @param vo
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private static void readResponseVO(ResponseVO vo,JsonReader reader) throws IOException
	{
		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if(name.equals("code")&&(JsonToken.NUMBER.equals(reader.peek())||JsonToken.STRING.equals(reader.peek())))
				vo.setCode(reader.nextInt());
			else if(name.equals("msg")&&JsonToken.STRING.compareTo(reader.peek())==0)
				vo.setMsg(reader.nextString());
			else
				reader.skipValue();
		}
		reader.endObject();
	}
	
	/**
	 * 读取Json(ret的内容必须在data内容之前)
	 * @param content 报文内容
	 * @param respVO　返回报文ResponseVO
	 * @return data部分的reader,如果没有返回null
	 */
	public static JsonReader parseJson(InputStream content,ResponseVO respVO)
	{
		if(respVO==null)
			respVO=new ResponseVO();
		if(content==null)
		{
			respVO.setCode(ResponseVO.RESPONSE_CODE_FAIL);
			return null;
		}
		JsonReader reader;
		try
		{
			reader=new JsonReader(new InputStreamReader(content,"UTF-8"));
		} catch (Exception e)
		{
			respVO.setCode(ResponseVO.RESPONSE_CODE_FAIL);
			respVO.setMsg(e.getMessage());
			e.printStackTrace();
			return null;
		}
		Boolean hasret=false; 
		try
		{
//			reader.beginObject();
//			if(reader.hasNext()&&JsonToken.NAME.equals(reader.peek())&&reader.nextName().equals(TAG_START))
//			{
				reader.beginObject();
				while (reader.hasNext())
				{
					if(!JsonToken.NAME.equals(reader.peek()))
					{
						reader.skipValue();
						continue;
					}
					String name = reader.nextName();
					if(name.equals(TAG_RET))
					{
						hasret=true;
						readResponseVO(respVO, reader);
					}
					else
					if(name.equals(TAG_DATA))
					{
						if(!hasret)
						{
							respVO.setCode(ResponseVO.RESPONSE_CODE_FAIL);
							respVO.setMsg("无效的数据!");
						}
						//修改 无论是否成功都返回reader
						//else
						//if(respVO.getCode() != Constants.RESPONSE_CODE_ERROR)
						{
							//如果存在data直接返回
							return reader;
						}
					}
					else
						reader.skipValue();
				}
				reader.endObject();
//			}
//			reader.endObject();
			reader.close();
			
		} catch (IOException e)
		{
			if(!hasret)
			{
				respVO.setCode(ResponseVO.RESPONSE_CODE_FAIL);
				respVO.setMsg(e.getMessage());
			}
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * 读取Json
	 * @param content 报文内容
	 * @param respVO　返回报文ResponseVO
	 * @return data部分的reader
	 */
	public static JsonReader parseJson(String content,ResponseVO respVO)
	{
		return parseJson(StringUtil.StringToInputStream(content), respVO);
	}
	
	/**
	 * 返回data结点下的entity 实例
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @return
	 */
	public static Object parseJsonToEntity(String content,ResponseVO respVO)
	{
		return parseJsonToEntity(StringUtil.StringToInputStream(content), respVO);
	}
	
	public static Object parseJsonToEntity(String content,ResponseVO respVO,String ref)
	{
		return parseJsonToEntity(StringUtil.StringToInputStream(content), respVO,ref);	
	}
	
	/**
	 * 返回data结点下的entity 实例
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @return
	 */
	public static Object parseJsonToEntity(InputStream content,ResponseVO respVO)
	{
		return parseJsonToEntity(content, respVO,null);
	}
	
	/**
	 * 返回data结点下的entity 实例
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @param ref ref内容，如果ref==null或"" 则读取第一个entity开使的内容
	 * @return
	 */
	public static Object parseJsonToEntity(InputStream content,ResponseVO respVO,String ref)
	{
		JsonReader reader=parseJson(content, respVO);
		if(reader==null)
			return null;
		Object result=null;
		try
		{
			reader.beginObject();
			while (reader.hasNext())
			{
				if(!JsonToken.NAME.equals(reader.peek()))
				{
					reader.skipValue();
					continue;
				}
				String name=reader.nextName();
				//if(name.startsWith(TAG_ENTITY+"_")&&JsonToken.BEGIN_OBJECT.equals(reader.peek()))
				if(IsEntity(reader.peek()))
				{
					String classstr=getClassOrFieldStr(name);
					if(StringUtil.isEmpty(ref))
						result=convertToEntity(reader,classstr);
					else if(name.endsWith("_"+ref))
						result=convertToEntity(reader,classstr);
				}
				else
					reader.skipValue();
			}
			reader.endObject();	
			reader.close();
		} catch (Exception e)
		{
			Log.e("JsonParser","parseJsonToEntity:"+ e.getMessage());
			return null;
		}
		return result;
	}
	
	/**
	 * 返回结点下的Prop属性
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @return
	 */
	public static Map<String, String> parseJsonToMap(String content,ResponseVO respVO)
	{
		return parseJsonToMap(StringUtil.StringToInputStream(content), respVO);
	}
	
	/**
	 * 返回结点下的Prop属性
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @return
	 */
	public static Map<String, String> parseJsonToMap(InputStream content,ResponseVO respVO)
	{
		Map<String, String> result = new HashMap<String, String>();
		JsonReader reader=parseJson(content, respVO);
		if(reader==null)
			return result;
		try
		{
			reader.beginObject();
			while (reader.hasNext())
			{
				if(!JsonToken.NAME.equals(reader.peek()))
				{
					reader.skipValue();
					continue;
				}
				String name=reader.nextName();
				//if(name.startsWith(TAG_PROP+"_")&&!JsonToken.NULL.equals(reader.peek()))
				if(IsProp(reader.peek()))
				{
					String propname=name;//getClassOrFieldStr(name);
					result.put(propname, reader.nextString());
				}
				else
					reader.skipValue();
				
			}
			reader.endObject();	
			reader.close();
		}
		catch (Exception e) {

			Log.e("JsonParser","parseJsonToMap:"+e.getMessage());
		}
		return result;
	}
	
	
	public static Boolean IsProp(JsonToken jt)
	{
		return (!JsonToken.NULL.equals(jt))&&
		(JsonToken.BOOLEAN.equals(jt)||
				JsonToken.NUMBER.equals(jt)||
				JsonToken.STRING.equals(jt));
	}


	public static Boolean IsList(JsonToken jt)
	{
		return (!JsonToken.NULL.equals(jt))&&
		JsonToken.BEGIN_ARRAY.equals(jt);
	}



	public static Boolean IsEntity(JsonToken jt)
	{
		return (!JsonToken.NULL.equals(jt))&&
		(JsonToken.BEGIN_OBJECT.equals(jt));
	}
	
	/**
	 * 返回list结点下的entity 实例
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @return
	 */
	public static List<?> parseJsonToList(String content,ResponseVO respVO)
	{
		return parseJsonToList(StringUtil.StringToInputStream(content), respVO, null);
	}

	/**
	 * 返回list结点下的entity 实例
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @param ref ref内容，如果ref==null或"" 则读取第一个list开使的内容
	 * @return
	 */
	public static List<?> parseJsonToList(String content,ResponseVO respVO,String ref)
	{
		return parseJsonToList(StringUtil.StringToInputStream(content), respVO, ref);
	}
	
	/**
	 * 返回list结点下的entity 实例
	 * @param content
	 * @param respVO 存储报文状态信息(需进行实例化)
	 * @param ref ref内容，如果ref==null或"" 则读取第一个list开使的内容
	 * @return
	 */
	public static List<?> parseJsonToList(InputStream content,ResponseVO respVO,String ref)
	{
		JsonReader reader=parseJson(content, respVO);
		List<?> result = null;
		if(reader==null)
			return result;
		try
		{
			reader.beginObject();
			while (reader.hasNext())
			{
				if(!JsonToken.NAME.equals(reader.peek()))
				{
					reader.skipValue();
					continue;
				}
				String name=reader.nextName();
				//if(name.startsWith(TAG_LIST+"_")&&JsonToken.BEGIN_ARRAY.equals(reader.peek()))
				if(JsonToken.BEGIN_ARRAY.equals(reader.peek()))
				{
					if(StringUtil.isEmpty(ref))
						result=convertToList(reader);
					else if(name.endsWith("_"+ref))
						result=convertToList(reader);
				}
				else 
					reader.skipValue();
			}
			reader.endObject();	
			reader.close();
		}catch (Exception e)
		{
			Log.e("JsonParser","parseJsonToList:"+ e.getMessage());
			return result;
		}
		return result;
		
	}
	
	@SuppressLint("DefaultLocale")
	private static Object convertToEntity(JsonReader reader,String classStr)
	{
		Object obj = null;
		try
		{
			String classname=PACKAGE_NAME.concat(classStr);
			Class<?> clazz=Class.forName(classname);
			obj=clazz.newInstance();
			reader.beginObject();
			while(reader.hasNext())
			{
				if(!JsonToken.NAME.equals(reader.peek()))
				{
					reader.skipValue();
					continue;
				}
				String name = reader.nextName();
				String propName = null;	// 属性名
				Object nodeObj = null;
				String clzName = null;
				//if(name.startsWith(TAG_PROP+"_")&&!JsonToken.NULL.equals(reader.peek()))//prop_field
				if(IsProp(reader.peek()))//field
				{
					nodeObj=reader.nextString();
					clzName = "java.lang.String";
					propName=name;//getClassOrFieldStr(name);
				}
				//else if(name.startsWith(TAG_ENTITY+"_")&&JsonToken.BEGIN_OBJECT.equals(reader.peek()))//entity_class_ref
				else if(IsEntity(reader.peek()))//class_ref
				{
					String subclass=getClassOrFieldStr(name);
					nodeObj = convertToEntity(reader,subclass);
					clzName = PACKAGE_NAME.concat(subclass);
					propName=getRefStr(name);
					
//					if(!StringUtils.isEmpty(propName)&&nodeObj!=null)
//					{
//						Field field = clazz.getField(propName);		// set方法无法进行类型转换，所以采用直接赋值
//						field.set(obj, nodeObj);
//					}
//					continue;
				}
				//else if(name.startsWith(TAG_LIST+"_")&&JsonToken.BEGIN_ARRAY.equals(reader.peek()))//list_ref
				else if(IsList(reader.peek()))//list_ref
				{
					nodeObj = convertToList(reader);
					clzName = "java.util.List";
					propName=getClassOrFieldStr(name);
				}
				else
					reader.skipValue();
				
				if (nodeObj == null) 
				{	
					// 空值不进行赋值
					continue;
				}
				Class<?> paraType = null;
				if (clzName != null) {
					paraType = Class.forName(clzName);
				}
				
				try
				{
					Method method = obj.getClass().getMethod("set"+propName.substring(0, 1).toUpperCase()+propName.substring(1),paraType);
					method.invoke(obj, nodeObj);
				}catch (Exception e)
				{
					Log.e("JsonParser","convertToEntity():"+"set"+propName.substring(0, 1).toUpperCase()+propName.substring(1)+"---"+e.toString());
					continue;
				}
			}
			reader.endObject();
			
		} catch (Exception e)
		{
			Log.e("JsonParser","convertToEntity():"+ e.toString());
		}
		return obj;
	}
	/**
	 * 解析json内容到LIST
	 * @param reader
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List convertToList(JsonReader reader)
	{
		List list = new ArrayList();
		try
		{
			reader.beginArray();
			while (reader.hasNext())
			{
				reader.beginObject();
				while (reader.hasNext())
				{
					if(!JsonToken.NAME.equals(reader.peek()))
					{
						reader.skipValue();
						continue;
					}
					Object nodeObj = null;
					String name = reader.nextName();
					//if(name.startsWith(TAG_ENTITY+"_")&&JsonToken.BEGIN_OBJECT.equals(reader.peek()))
					if(IsEntity(reader.peek()))
					{
						nodeObj = convertToEntity(reader,getClassOrFieldStr(name));
					}
					//else if(name.startsWith(TAG_LIST+"_")&&JsonToken.BEGIN_OBJECT.equals(reader.peek()))
					else if(IsList(reader.peek()))
						nodeObj = convertToList(reader);
					//else if(name.startsWith(TAG_PROP+"_")&&!JsonToken.NULL.equals(reader.peek()))
					else if(IsProp(reader.peek()))
						nodeObj=reader.nextString();
					else
						reader.skipValue();	

					if(nodeObj!=null)
						list.add(nodeObj);
				}
				reader.endObject();
			}
			reader.endArray();
		} catch (Exception e)
		{
			Log.e("JsonParser","convertToList:"+ e.getMessage());
		}
		return list;
	}

	
	
	/**
	 * 返回str[_ref]中的str
	 * @param str
	 * @return
	 */
	private static String getClassOrFieldStr(String str)
	{
		if(StringUtil.isEmpty(str))
			return StringUtil.STR_EMPTY;;
		Integer index=str.indexOf("_");
		if(index==-1)
			return str;
		String result=str.substring(0,index);
		return result;
	}
	/**
	 * 返回str[_ref]中的ref
	 * @param str
	 * @return
	 */
	private static String getRefStr(String str)
	{
		if(StringUtil.isEmpty(str))
			return StringUtil.STR_EMPTY;
		Integer index=str.indexOf("_");
		if(index==-1)
			return StringUtil.STR_EMPTY;
		return str.substring(index+1);
	}
}
