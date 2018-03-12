package net.lawyee.mobilelib.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 视频操作相关
 * 
 * @author wuzhu
 * 
 */
public class VideoUtil
{
	/**
	 * 视频缩略图字段
	 */
	private static String[] thumbColumns = new String[] {
			MediaStore.Video.Thumbnails.DATA,
			MediaStore.Video.Thumbnails.VIDEO_ID };

	/**
	  * 视频信息字段
	  */
	private static String[] mediaColumns = new String[] {
			MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID,
			MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE };

	/**
	 * 视频信息
	 * 
	 * @author wuzhu
	 * 
	 */
	public static class VideoInfo
	{
		public String filePath;
		public String mimeType;
		public String thumbPath;
		public Bitmap thumb;
		public String title;
	}

	/**
	 * 获取所有的视频信息
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static ArrayList<VideoInfo> getALLVideoInfo(Activity activity)
	{
		ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
		Cursor cursor = activity.managedQuery(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
				null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				VideoInfo info = new VideoInfo();

				info.filePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				info.mimeType = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
				info.title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));

				// 获取当前Video对应的Id，然后根据该ID获取其Thumb
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
				String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";
				String[] selectionArgs = new String[] { id + "" };
				Cursor thumbCursor = activity.managedQuery(
						MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
						thumbColumns, selection, selectionArgs, null);
				
				if (thumbCursor.moveToFirst())
				{
					info.thumbPath = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

				}
				info.thumb=MediaStore.Video.Thumbnails.getThumbnail(activity.getContentResolver(),
						id, MediaStore.Video.Thumbnails.MICRO_KIND, null);

				// 然后将其加入到videoList
				videoList.add(info);

			} while (cursor.moveToNext());
		}
		return videoList;
	}
	
	/**
	 * 获取某个视频文件的文件信息
	 * @param videofile
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static VideoInfo getVideoInfo(String videofile,Activity activity)
	{
		String selection = MediaStore.Video.Media.DATA + "=?";
		String[] selectionArgs = new String[] { videofile };
		Cursor cursor = activity.managedQuery(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
				selection, selectionArgs, null);
		if(cursor==null||!cursor.moveToFirst())
			return null;
		VideoInfo info = new VideoInfo();
		info.filePath = cursor.getString(cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
		info.mimeType = cursor
				.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
		info.title = cursor.getString(cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));

		// 获取当前Video对应的Id，然后根据该ID获取其Thumb
		int id = cursor.getInt(cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
		String selection2 = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";
		String[] selectionArgs2 = new String[] { id + "" };
		Cursor thumbCursor = activity.managedQuery(
				MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
				thumbColumns, selection2, selectionArgs2, null);

		if (thumbCursor.moveToFirst())
		{
			info.thumbPath = cursor
					.getString(cursor
							.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

		}
		info.thumb=MediaStore.Video.Thumbnails.getThumbnail(activity.getContentResolver(),
				id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
		return info;
	}
	

	
	/**
	 * 播放视频
	 * @param c
	 * @param videfile
	 */
	public static void playVideo(Context c,String videfile)
	{
		if(StringUtil.isEmpty(videfile))
			return;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(videfile);
		intent.setDataAndType(uri, "video/*");//"video/3gp"
		c.startActivity(intent);
	}
}
