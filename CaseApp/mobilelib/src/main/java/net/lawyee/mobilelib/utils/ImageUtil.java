package net.lawyee.mobilelib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import net.lawyee.mobilelib.Constants;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 图像工具类
 * @author wuzhu
 * @date 2013-12-17 上午9:57:07
 * @version $id$
 */
public class ImageUtil 
{
	protected static final String Tag = ImageUtil.class.getSimpleName();
	/**
	 * 小图默认的高度
	 */
	public static final int IMAGE_DEFHEIGHT_L = 160;
	/**
	 * 小图默认的宽度
	 */
	public static final int IMAGE_DEFWIDTH_L = 160;

	/**
	 * 中图默认的高度
	 */
	public static final int IMAGE_DEFHEIGHT_M = 480;
	/**
	 * 中图默认的宽度
	 */
	public static final int IMAGE_DEFWIDTH_M = 480;

	/**
	 * 大图默认的高度
	 */
	public static final int IMAGE_DEFHEIGHT_H = 640;
	/**
	 * 大图默认的宽度
	 */
	public static final int IMAGE_DEFWIDTH_H = 640;

	/**
	 * 超大图默认的高度
	 */
	public static final int IMAGE_DEFHEIGHT_XH = 1024;
	/**
	 * 超大图默认的宽度
	 */
	public static final int IMAGE_DEFWIDTH_XH = 1024;

	/**
	 * 变更图片文件大小
	 * 
	 * @param bmfilename
	 *            　原图文件名
	 * @return　处理后的文件名
	 */
	public static String resizeBitmapFile(Context c,String bmfilename, int newwidth)
	{
		if (StringUtil.isEmpty(bmfilename))
			return "";

		File file = new File(bmfilename);
		if (!file.exists())
			return "";
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 该值设为true那么将不返回实际的bitmap不给其分配内存空间而里面只包括一些解码边界信息即图片大小信息,获取到outHeight(图片原始高度)和
											// outWidth(图片的原始宽度)
		bm = BitmapFactory.decodeFile(bmfilename, options); // 此时返回bm为空
		// 获取这个图片的宽和高
		int be = (int) (options.outWidth / (float) newwidth);
		if (be <= 0)
			be = 1;
		options.inJustDecodeBounds = false;// 重新载入图片
		options.inSampleSize = be;
		bm = BitmapFactory.decodeFile(bmfilename, options);
		return saveBitmap(c,bm, true);
		// return saveBitmap(resizeBitmap(bm, Cint_PhotoRealWidth,true), true);
	}

	/**
	 * 变更图片大小（只在比原图小时才变更）
	 * 
	 * @param bitmap
	 *            原图片
	 * @param newWidth
	 *            　新图片的宽度
	 * @param brecycle
	 *            是否在变更完成后回收
	 * @return　　变更后的图
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth,
			boolean brecycle)
	{
		int width = bitmap.getWidth();
		// 如果原图
		if (newWidth >= width)
			return bitmap;
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		if (brecycle)
			bitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * 根据设定的最大宽和高返回获取图片
	 * 
	 * @param filename
	 *            图片文件名称
	 * @param maxwidth
	 *            最大宽度,设置值小于等于0时忽略
	 * @param maxheight
	 *            　最大高度,设置值小于等于0时忽略
	 * @return 图片不存在或不能读取，返回null，如果原图大小设定的高或宽，则返回缩略图，如果小于高或宽则返回原图
	 */
	public static Bitmap getBitmapFromFile(String filename, int maxwidth,
			int maxheight)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 该值设为true那么将不返回实际的bitmap不给其分配内存空间而里面只包括一些解码边界信息即图片大小信息,获取到outHeight(图片原始高度)和
											// outWidth(图片的原始宽度)
		Bitmap bm = BitmapFactory.decodeFile(filename, options); // 此时返回by为空

		if (options == null || options.outHeight <= 0 || options.outWidth <= 0)
			return null;
		
		int be =1;
		//图像宽度大于最大宽度时计算缩放
		if(maxwidth>0&&options.outWidth>maxwidth)
		{
			be=(int)(options.outWidth/(float)maxwidth);
		} else
		if(maxwidth<=0&&options.outWidth>IMAGE_DEFWIDTH_XH)
		{
			be=(int)(options.outWidth/(float)IMAGE_DEFWIDTH_XH);
		}
		//图像高度大于最大高度时计算缩放
		if(maxheight>0&&options.outHeight>maxheight)
		{
			int tmpbe=(int)(options.outHeight/(float)maxheight);
			//如果缩放比例大于宽度，则以高度的缩放比例
			if(tmpbe>be)
				be=tmpbe;
		}else
			if(maxheight<=0&&options.outHeight>IMAGE_DEFHEIGHT_XH)
			{
				int tmpbe=(int)(options.outHeight/(float)IMAGE_DEFHEIGHT_XH);
				if(tmpbe>be)
					be=tmpbe;
			}
		if (be <= 0)
			be = 1;
		options.inJustDecodeBounds = false;// 重新载入图片
		options.inSampleSize = be;
		try
		{
			bm = BitmapFactory.decodeFile(filename, options);
		} catch (Exception e)
		{
			e.printStackTrace();
			//Log.e(Tag, e.getMessage());
			bm = null;
		}
		return bm;
	}

	/**
	 * 存储图片，返回存储文件成功后的文件名
	 * 
	 * @param bitmap
	 * @param brecycle
	 *            是否在存储完成后回收
	 * @return
	 */
	public static String saveBitmap(Context c, Bitmap bitmap, boolean brecycle)
	{
		if (bitmap == null)
			return "";
		String strRand="" ;
		for(int j=0;j<20;j++){
			strRand += String.valueOf((int)(Math.random() * 20)) ;
		}
		String tempfilename = TimeUtil.dateToString(null, "yyyyMMddHHmmss")

				+strRand+ ".jpg";
		String filepath = Constants.getDataStoreDir(c) + Constants.CSTR_IMAGECACHEDIR;
		FileOutputStream fos = null;
		if (!FileUtil.isExistFolder(filepath))
			return "";
		try
		{
			File file = new File(filepath, tempfilename);
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
			if (brecycle)
				bitmap.recycle();
		} catch (Exception ex)
		{
			return "";
		}
		return filepath + tempfilename;
	}

	/**
	 * drawable 转为bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable == null)
			return null;
		return ((BitmapDrawable) drawable).getBitmap();
	}
	
	/**
	 * 获取图片默认存储路径
	 * @param imageUrl 原图片路径，可为空
	 * @return 如果为空，则根据当前时间生成随机文件名。不为空时，返回文件名称加图片缓存路径组成的文件名
	 */
	public static String getDefaultImageFileFullPath(Context c,String imageUrl)
	{
		if(StringUtil.isEmpty(imageUrl))
		{
			String tempfilename = TimeUtil.dateToString(null, "yyyyMMddHHmmss")
					+ ".jpg";
			String filepath = Constants.getDataStoreDir(c) + Constants.CSTR_IMAGECACHEDIR;
			return filepath+tempfilename;
		}
		return Constants.getDataStoreDir(c).concat(
				Constants.CSTR_IMAGECACHEDIR).concat(
				FileUtil.getFileName(imageUrl));
	}
	
	/**
	 * 是否存在本地文件
	 * @param imageurl
	 * @return
	 */
	public static boolean hasLocalFile(Context c,String imageurl)
	{
		File file = new File(
				ImageUtil.getDefaultImageFileFullPath(c,imageurl));
		return file.exists();
	}
}
