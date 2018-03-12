package net.lawyee.mobilelib.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.lawyee.mobilelib.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

/**
 * 处理html内的图片工具基类
 * @author wuzhu
 * @date 2013-12-12 09:22:00
 * @version $id$
 */
@SuppressLint("JavascriptInterface")
public abstract class HtmlParser extends AsyncTask<Void, Void, String>
{
	private static final String TAG = HtmlParser.class.getSimpleName();
	/**
	 * 添加的JS交互名称
	 */
	public static String Js2JavaInterfaceName = "JsUseJava";

	private Context mContext;
	private String mUrl;
	private WebView mWebView;
	private List<String> mImgUrls = new ArrayList<String>();
	private String mHtmltext;
	//private int mImageShowWidth;
	/**
	 * 是否是已经处理过的内容
	 */
	private boolean mIsHandledContent;
	
	private OnCompleteListener mCompleteListener;
	
	private boolean mSucess;
	
	/**
	 * 
	 * @param wevView
	 * @param url
	 * @param context
	 * @param screenwidth
	 */
	public HtmlParser(WebView wevView, String url, Context context,int screenwidth)
	{
		this(wevView,url,context,"",false,screenwidth);
	}
	
	/**
	 * 
	 * @param wevView
	 * @param url
	 * @param context
	 * @param htmltext
	 * @param screenwidth
	 */
	public HtmlParser(WebView wevView, String url, Context context,
			String htmltext,int screenwidth)
	{
		this(wevView,url,context,htmltext,true,screenwidth);
	}
	
	/**
	 * 
	 * @param wevView
	 * @param context
	 * @param htmltext
	 * @param ishandledcontent 是否是已经处理过的内容
	 * @param screenwidth
	 */
	public HtmlParser(WebView wevView, Context context,
			String htmltext,boolean ishandledcontent,int screenwidth)
	{
		this(wevView,null,context,htmltext,ishandledcontent,screenwidth);
	}
	
	/**
	 * 
	 * @param wevView
	 * @param url
	 * @param context
	 * @param htmltext
	 * @param ishandledcontent 是否是已经处理过的内容
	 * @param screenwidth
	 */
	public HtmlParser(WebView wevView, String url, Context context,
			String htmltext,boolean ishandledcontent,int screenwidth)
	{
		this.mWebView = wevView;
		mUrl = url;
		mContext = context;
		mHtmltext = htmltext;
		//mImageShowWidth = screenwidth-10;
		mIsHandledContent = ishandledcontent;
	}

	/**
	 * 数据内容地址，只有当内容为空时使用，用于读取此地址上的内容
	 * @return
	 */
	public String getUrl()
	{
		return mUrl;
	}

	/**
	 * 设置数据内容地址
	 * @param mUrl
	 */
	public void setUrl(String mUrl)
	{
		this.mUrl = mUrl;
	}

	/**
	 * 获取内容解析后得到的图片列表
	 * @return
	 */
	public List<String> getImgUrls()
	{
		return mImgUrls;
	}

	public void setImgUrls(List<String> mImgUrls)
	{
		this.mImgUrls = mImgUrls;
	}

	/**
	 * 数据内容
	 * @return
	 */
	public String getHtmltext()
	{
		return mHtmltext;
	}

	/**
	 * 设置未处理过的内容
	 * @param mHtmltext
	 */
	public void setNoHandledHtmltext(String mHtmltext)
	{
		this.mHtmltext = mHtmltext;
		mIsHandledContent = false;
	}
	
	/**
	 * 设置已经处理过的内容，如果内容为空，则状态还是未处理
	 * @param mHtmltext
	 */
	public void setHandledHtmltext(String mHtmltext)
	{
		this.mHtmltext = mHtmltext;
		
		mIsHandledContent = !StringUtil.isEmpty(mHtmltext);
	}
	
	public void setHtmltext(String mHtmltext)
	{
		this.mHtmltext = mHtmltext;
	}

	@Override
	protected String doInBackground(Void... params)
	{
		Document doc = null;
		mImgUrls.clear();
		//内容为空时自动且地址不为空时读取地址的内容
		if (StringUtil.isEmpty(mHtmltext)&&!StringUtil.isEmpty(mUrl))
		{	
			try
			{
				doc = Jsoup.parse(new URL(mUrl), Constants.READ_TIME_OUT * 1000);
			} catch (MalformedURLException e2)
			{
				e2.printStackTrace();
			} catch (IOException e2)
			{
				e2.printStackTrace();
			}

			if (doc == null)
			{
				Log.e(TAG, "获取" + mUrl + "数据失败");
				return getDocument(null);
			}

			Elements es = doc.select("script");
			if (es != null)
			{
				es.remove();
			}
			Element e = handleDocument(doc);
			handleImageClickEvent(e);
			// removeHyperlinks(doc);
			mHtmltext = getDocument(e);
		} else
		{
			//内容不为空时直接处理
			//已经处理过的直接处理图片
			if(mIsHandledContent)
			{
				//已经处理过的内容，只要重新获取下里面的图片元素，并记录下来就可以
				doc = Jsoup.parse(mHtmltext);
				handleImage(doc);
			}else
			{
				//未处理过的内容，需要重新进行处理
				doc = Jsoup.parse(mHtmltext);
				if (doc == null)
				{
					Log.e(TAG, "获取" + mUrl + "数据失败");
					return getDocument(null);
				}
				Element e = handleDocument(doc);
				handleImageClickEvent(e);
				mIsHandledContent = true;
				mHtmltext = getDocument(e);
			}
			
		}
		return mHtmltext;
	}

	/**
	 * 
	 * @param doc
	 */
	private void handleImage(Document doc)
	{
		Elements es = doc.getElementsByTag("img");

		for (Element e : es)
		{
			String imgUrl = e.attr("ori_link");
			if(StringUtil.isEmpty(imgUrl))
				imgUrl = e.attr("src");
			mImgUrls.add(imgUrl);
		}
	}
	
	private void processImageClickEvent(Elements es)
	{
		if(es==null||es.isEmpty())
			return;
		
		for ( int i=0;i<es.size();i++)
		{
			Element e = es.get(i);
			String imgUrl = e.attr("src");
			mImgUrls.add(imgUrl);
			if (imgUrl.endsWith(".gif"))
			{
				e.remove();
			} else
			{
				// 移除所有原来的属性
				Attributes atts = e.attributes();
				for (Attribute att : atts)
				{
					atts.remove(att.getKey());
				}
				// 重新设置属性
				String filePath = "file://"
						+ ImageUtil.getDefaultImageFileFullPath(mContext,imgUrl);
				e.attr("src", "file:///android_asset/icon_loadingfail.png");
				e.attr("src_link", filePath);
				e.attr("ori_link", imgUrl);
				e.attr("width","98%");
				String str = "window." + Js2JavaInterfaceName + ".showImage('"
						+ imgUrl + "')";
				e.attr("onClick", str);
				//String insertStr = "fixImage(this," + mImageShowWidth + ",0)";
				//e.attr("onload",insertStr);
				//
				//int screenwidth = ViewUtil.getScreenWidth(activity) - 10;
				//String insertStr = " onload=\"fixImage(this," + screenwidth + ",0)\" ";
			}
		}
	}
	
	/**
	 * 增加图片点击处理
	 * @param roote
	 */
	protected void handleImageClickEvent(Element roote)
	{
		if(roote==null)
			return;
		Elements es = roote.getElementsByTag("img");

		processImageClickEvent(es);
	}

	/**
	 * 增加图片点击处理
	 * @param doc
	 */
	protected void handleDocImageClickEvent(Document doc)
	{
		if(doc==null)
			return;
		Elements es = doc.getElementsByTag("img");
		
		processImageClickEvent(es);
	}

//	/**
//	 * 移除href
//	 * 
//	 * @param doc
//	 */
//	protected void removeHyperlinks(Document doc)
//	{
//		Elements hrefs = doc.getElementsByTag("a");
//		for (Element href : hrefs)
//		{
//			href.removeAttr("href");
//		}
//	}

	/**
	 * 内容前期处理抽象方法，需在子类实现，如处理掉第三方链接等
	 * @param doc
	 * @return
	 * 
	 */
	protected abstract Element handleDocument(Document doc);
	/**
	 * 内容最后处理抽象方法，需在子类实现，可在这里做出缓存等操作
	 * @return 要展示的内容
	 */
	protected abstract String getDocument(Element e);

	@Override
	protected void onPostExecute(String result)
	{
		if(mCompleteListener!=null)
			mCompleteListener.OnComplete(mSucess);
		if(mWebView!=null)
			mWebView.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
		super.onPostExecute(result);
	}

	public Context getContext()
	{
		return mContext;
	}

	public void setContext(Context mContext)
	{
		this.mContext = mContext;
	}

	/**
	 * 是否是已经处理过的内容，如果是已经处理过的内容，则不会再调用handleDocument，getDocument再处理内容
	 * @return
	 */
	public boolean isHandledContent()
	{
		return mIsHandledContent;
	}

	public void setIsHandledContent(boolean isHandledContent)
	{
		this.mIsHandledContent = isHandledContent;
	}
	
	/**
	 * 监听内容处理完成情况
	 * @return
	 */
	public OnCompleteListener getCompleteListener()
	{
		return mCompleteListener;
	}

	public void setCompleteListener(OnCompleteListener completeListener)
	{
		this.mCompleteListener = completeListener;
	}

	public boolean isSucess()
	{
		return mSucess;
	}

	public void setSucess(boolean sucess)
	{
		this.mSucess = sucess;
	}

	public interface OnCompleteListener
	{
		public boolean OnComplete(boolean issuc);
	}
}
