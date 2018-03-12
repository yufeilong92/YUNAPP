package net.lawyee.mobilelib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Date;

import net.lawyee.mobilelib.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;

/**
 * 文件工具类
 * 
 * @author wuzhu
 * @date 2013-4-24 下午3:07:42
 * @version $id$
 */
@SuppressLint("DefaultLocale")
public class FileUtil
{

	private static final String TAG = FileUtil.class.getSimpleName();

	/**
	 * 获取实际的图片路径
	 * 
	 * @param contentUri
	 *            　Uri路径
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getRealPathFromURI(Uri contentUri, Activity activity)
	{
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
				null);
		if (cursor == null)
			return "";
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		if (cursor.moveToFirst())
			return cursor.getString(column_index);
		else
			return "";
	}

	/**
	 * 判断存储卡是否存在
	 */
	public static Boolean hasSDCard()
	{
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取存储卡路径，路径带/ 如果没有存储卡，返回空字符串
	 */
	public static String getSDCardPath()
	{
		if (hasSDCard())
			return Environment.getExternalStorageDirectory() + File.separator;
		else
			return "";
	}

	/**
	 * 临时存储照片路径
	 * 
	 * @return
	 */
	public static String getTempPhotoPath(Context c)
	{
		return Constants.getDataStoreDir(c) + Constants.CSTR_IMAGECACHEDIR + File.separator
				+ "tmpphoto.jpg";
	}

	/**
	 * 临时存储视频路径
	 * 
	 * @return
	 */
	public static String getTempVideoPath(Context c)
	{
		return Constants.getDataStoreDir(c) + Constants.CSTR_IMAGECACHEDIR + File.separator
				+ TimeUtil.dateToString(new Date(), "yyyyMMddHHmmss") + ".3gp";
	}

	/**
	 * 打开文件
	 * 
	 **/
	public static void OpenFile(File f, Context c)
	{
		if (!f.exists() || c == null)
			return;

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		c.startActivity(intent);
	}

	/**
	 * 判断是否是图片文件地址（包括URL或文件路径）
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isImageFile(String file)
	{
		String type = getMIMEType(file);
		if (StringUtil.isEmpty(type))
			return false;
		return type.startsWith("image");
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	public static String getMIMEType(File file)
	{
		if (file == null)
			return "*/*";
		return getMIMEType(file.getName());
	}

	public static String getMIMEType(String fName)
	{
		String type = "*/*";
		/* 获取文件的后缀名 */
		String end = getFileExtension(fName);
		if (StringUtil.isEmpty(end))
			return type;
		end = "." + end;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++)
		{
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 */
	public static String getFileExtension(String filename)
	{
		if (StringUtil.isEmpty(filename))
			return "";
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex < 0)
			return "";
		/* 获取文件的后缀名 */
		return filename.substring(dotIndex + 1, filename.length())
				.toLowerCase();
	}

	/**
	 * 建立一个MIME类型与文件后缀名的匹配表
	 */
	public static final String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" },
			{ ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" },
			{ ".h", "text/plain" }, { ".htm", "text/html" },
			{ ".html", "text/html" }, { ".jar", "application/java-archive" },
			{ ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" },
			{ ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" }, { ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".prop", "text/plain" },
			{ ".rar", "application/x-rar-compressed" },
			{ ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" },
			// {".xml", "text/xml"},
			{ ".xml", "text/plain" }, { ".z", "application/x-compress" },
			{ ".zip", "application/zip" }, { "", "*/*" } };

	/**
	 * 读取文件
	 * 
	 * @param file
	 *            完成文件路径
	 * @return
	 */
	public static byte[] ReadFileToBytes(String file)
	{
		FileInputStream fIn = null;

		byte[] buffer = null;
		try
		{
			File f = new File(file);
			fIn = new FileInputStream(f);
			buffer = new byte[fIn.available()];
			fIn.read(buffer);
		} catch (Exception e)
		{
			e.printStackTrace();
			// Log.e(TAG, e.getMessage());
		} finally
		{
			try
			{
				if (fIn != null)
					fIn.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				// Log.e(TAG, e.getMessage());
			}
		}
		return buffer;
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 *            完成文件路径
	 * @return
	 */
	public static String ReadFile(String file)
	{
		return ReadFile(file, "UTF-8");
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 *            完成文件路径
	 * @param charset
	 *            字符集,默认UTF-8
	 * @return
	 */
	public static String ReadFile(String file, String charset)
	{
		File f = new File(file);
		if (!f.exists())
			return null;
		Charset cs;
		try
		{
			cs = Charset.forName(charset);
		} catch (Exception e)
		{
			cs = Charset.forName("UTF-8");
		}
		FileInputStream fIn = null;
		InputStreamReader isr = null;
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		try
		{
			fIn = new FileInputStream(f);
			isr = new InputStreamReader(fIn, cs);
			br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null)
			{
				result.append(temp);
				result.append("\r\n");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			// Log.e(TAG, e.getMessage());
		} finally
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
				// Log.e(TAG, e.getMessage());
			}
		}
		return result.toString();
	}

	/**
	 * 保存文件内容
	 */
	public static void WriteFile(String file, byte[] data)
	{
		FileOutputStream fOut = null;
		try
		{
			File f = new File(file);
			fOut = new FileOutputStream(f, true);
			fOut.write(data);
		} catch (Exception e)
		{
			e.printStackTrace();
			// Log.e(TAG, e.getMessage());
		} finally
		{
			try
			{
				if (fOut != null)
					fOut.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				// Log.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * 写入文件内容,如果原文件存在，不保存原文件内容
	 * 
	 * @param file
	 *            　文件名
	 * @param data
	 *            　要写入的数据
	 */
	public static void WriteFile(String file, String data)
	{
		WriteFile(file, data, false);
	}

	/**
	 * 保存文件内容
	 */
	public static void WriteFile(String file, String data, Boolean append)
	{
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		try
		{
			isExistFolderFromFile(file); // 文件夹存在与否检测，不存在则创建

			File f = new File(file);
			fOut = new FileOutputStream(f, append);
			osw = new OutputStreamWriter(fOut);
			osw.write(data);
			osw.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
			// Log.e(TAG, e.toString());
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
				// Log.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static boolean deleteFile(String filename)
	{
		File f = new File(filename);

		if (!f.exists())
		{
			return true;
		}

		boolean flag = f.delete();

		return flag;
	}

	/**
	 * 删除文件夹
	 *
	 * @return boolean
	 */

	public static Boolean delFolder(String folderPath)
	{
		try
		{
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			// Log.d("delFolder", filePath.toString());
			File f = new File(filePath);
			if (f.exists())
				f.delete(); // 删除空文件夹
			return true;

		} catch (Exception e)
		{
			e.printStackTrace();
			// Log.e("delFolder", "删除文件夹操作出错:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static boolean delAllFile(String path)
	{
		File file = new File(path);

		if (!file.exists())
		{
			Log.d("no_file", "nofile");
			return true;
		}
		if (!file.isDirectory())
		{
			Log.d("no_file", "File_Directory");
			return true;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++)
		{
			if (path.endsWith(File.separator))
			{
				temp = new File(path + tempList[i]);
				Log.d("FileUtil ", "Temp_Name" + temp.getName());

			} else
			{
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile())
			{
				temp.delete();
			}
			if (temp.isDirectory())
			{
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
		return true;
	}

	/**
	 * inputstream 存为文件，旧文件存在的话会被删除
	 * 
	 * @param inputStream
	 * @param destFile
	 * @return
	 */
	public static boolean copyToFile(InputStream inputStream, File destFile)
	{
		try
		{
			if (destFile.exists())
			{
				destFile.delete();
			} else
			{
				if (!isExistFolderFromFile(destFile.getPath()))
					return false;
			}
			OutputStream out = new FileOutputStream(destFile);
			try
			{
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0)
				{
					out.write(buffer, 0, bytesRead);
				}
			} finally
			{
				out.close();
			}
			return true;
		} catch (IOException e)
		{
			return false;
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param strfilename
	 * @return
	 */
	public static boolean isExistFile(String strfilename)
	{
		if (StringUtil.isEmpty(strfilename))
			return false;
		File f = new File(strfilename);
		if (f == null || !f.exists())
			return false;
		return true;
	}

	/**
	 * 判断文件的文件夹是否存在,不存在则会偿试进行创建
	 * 
	 * @param strfilename
	 *            文件的完整文件名
	 * @return
	 */
	public static boolean isExistFolderFromFile(String strfilename)
	{
		if (StringUtil.isEmpty(strfilename))
			return false;
		int index = strfilename.lastIndexOf(File.separator);
		if (index <= 0)
			return false;
		String fdir = strfilename.substring(0, index);
		return isExistFolder(fdir);
	}

	/**
	 * 判断是否存在文件夹,不存在则会偿试进行创建
	 */
	public static boolean isExistFolder(String strFolder)
	{
		if (StringUtil.isEmpty(strFolder))
			return true;
		boolean bReturn = false;

		File f = new File(strFolder);
		if (!f.exists())
		{
			/* 创建文件夹 */
			if (f.mkdirs())
			{
				bReturn = true;
			} else
			{
				bReturn = false;
			}
		} else
		{
			bReturn = true;
		}
		return bReturn;
	}

	/**
	 * 获取文件名
	 * 
	 * @param fullfilename
	 *            　文件的完成路径名或URL地址
	 * @return 文件名,如果完整路径里不包括/或\则直接返回fullfilename
	 */
	public static String getFileName(String fullfilename)
	{
		if (StringUtil.isEmpty(fullfilename))
			return "";
		int index = -1;
		if (fullfilename.contains(File.separator))
		{
			index = fullfilename.lastIndexOf(File.separator);
		} else
			index = fullfilename.lastIndexOf("\\");
		if (index == -1)
			return fullfilename;
		else if (index >= fullfilename.length() - 1)
			return "";
		return fullfilename.substring(index + 1);
	}

	/**
	 * 存储内容
	 * 
	 * @param filename
	 *            文件名，如果不包括/路径，则存储到存储卡目录下
	 * @param filecontent
	 *            要存储的内容
	 */
	public static void saveContent(String filename, String filecontent)
	{
		if (StringUtil.isEmpty(filename) || StringUtil.isEmpty(filecontent))
			return;
		String filefullpath;
		if (filename.contains(File.separator))
			filefullpath = filename;
		else
			filefullpath = getSDCardPath() + filename;
		WriteFile(filefullpath, filecontent, false);
	}

	/**
	 * 读取文件的内容
	 * 
	 * @param filename
	 *            文件名，如果不包括/路径,则读取存储卡根目录下文件
	 * @return
	 */
	public static String readContent(String filename)
	{
		if (StringUtil.isEmpty(filename))
			return "";
		String filefullpath;
		if (filename.contains(File.separator))
			filefullpath = filename;
		else
			filefullpath = getSDCardPath() + filename;
		return ReadFile(filefullpath);
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为B
	 */
	public static long getFolderSize(File file)
	{
		long size = 0;
		if (file == null)
			return size;
		if (file.isDirectory())
		{
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++)
			{
				if (fileList[i].isDirectory())
				{
					size = size + getFolderSize(fileList[i]);
				} else
				{
					size = size + fileList[i].length();
				}
			}
		}else
		{
			size = file.length();
		}
		return size;
	}

	/**
	 * 格式化大小
	 */
	public static String FormatFileSize(Context c, long size)
	{
		return Formatter.formatFileSize(c, size);
	}

	/**
	 * 文件大小单位转换
	 * 
	 * @param size
	 * @return
	 */
	public static String setFileSize(long size)
	{
		DecimalFormat df = new DecimalFormat("###.##");
		float f = ((float) size / (float) (1024 * 1024));

		if (f < 1.0)
		{
			float f2 = ((float) size / (float) (1024));

			return df.format(Float.valueOf(f2).doubleValue()) + "KB";

		} else
		{
			return df.format(Float.valueOf(f).doubleValue()) + "M";
		}

	}

	/**
	 * 获取文件保存完整路径
	 * 
	 * @param urlPath
	 * @return
	 */
	public static String GetUpdateFileSaveFileName(Context c,String urlPath)
	{
		String fileName = FileUtil.getFileName(urlPath);

		return Constants.getDataStoreDir(c) + fileName;
	}

	/**
	 * 获取图片缩略图 只有Android2.1以上版本支持
	 * 
	 * @param imgName
	 * @param kind
	 *            MediaStore.Images.Thumbnails.MICRO_KIND
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap loadImgThumbnail(Activity activity, String imgName,
			int kind)
	{
		Bitmap bitmap = null;

		String[] proj = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME };

		Cursor cursor = activity.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
				MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName + "'",
				null, null);

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
		{
			ContentResolver crThumb = activity.getContentResolver();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb,
					cursor.getInt(0), kind, options);
		}
		return bitmap;
	}

	/**
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Activity activity,
			String fileName)
	{
		Bitmap image = null;
		AssetManager am = activity.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return image;
	}
	
	/**
	 * 是否为AssetsFile文件路径
	 * @param str
	 * @return
	 */
	public static boolean isAssetsFileStr(String str)
	{
		if(StringUtil.isEmpty(str))
			return false;
		String tmpstr = str.toLowerCase();
		return tmpstr.startsWith("file:///android_asset/");
	}
	
	/**
	 * 从完整AssetsFile文件路径中获取AssetsFile文件名
	 * @param str
	 * @return
	 */
	public static String getFileNameFromAssetsFileStr(String str)
	{
		if(!isAssetsFileStr(str))
			return "";
		return str.substring("file:///android_asset/".length());
	}
	
	/**
	 * 读取完整路径名
	 * @param context
	 * @param fullfilename
	 * @return
	 */
	public static String readFileFromAssetsFullFile(Context context, String fullfilename)
	{
		return readFileFromAssetsFile(context,getFileNameFromAssetsFileStr(fullfilename));
	}

	/**
	 * 从Assets中读取文件
	 */
	public static String readFileFromAssetsFile(Context context, String fileName)
	{
		if(context==null||StringUtil.isEmpty(fileName))
			return "";
		StringBuilder result = new StringBuilder();
		AssetManager am = context.getResources().getAssets();
		try
		{
			InputStream is = am.open(fileName);
			Charset cs = Charset.forName("UTF-8");
			InputStreamReader isr = null;
			BufferedReader br = null;
			try
			{
				isr = new InputStreamReader(is, cs);
				br = new BufferedReader(isr);
				String temp;
				while ((temp = br.readLine()) != null)
				{
					result.append(temp);
					result.append("\r\n");
				}
			} catch (Exception e)
			{
				Log.e(TAG, "readFileFromAssetsFile error:" + e.getMessage());
			} finally
			{
				try
				{
					if (br != null)
						br.close();
					if (isr != null)
						isr.close();
				} catch (IOException e)
				{
					Log.e(TAG,
							"readFileFromAssetsFile IOError:" + e.getMessage());
				}
			}
			is.close();
		} catch (Exception e)
		{
			Log.e(TAG, "readFileFromAssetsFile error:" + e.getMessage());
		}
		return result.toString();
	}
}
