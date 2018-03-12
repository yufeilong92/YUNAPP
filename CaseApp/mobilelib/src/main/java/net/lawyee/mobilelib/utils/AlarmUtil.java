package net.lawyee.mobilelib.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * 定时工具类
 * @author wuzhu
 * @date 2013-12-12 09:22:00
 * @version $id$
 */
public class AlarmUtil
{
	private static final String TAG = AlarmUtil.class.getSimpleName();
	/**
	 * 默认第一次刷新在启动后多久(单位S)
	 */
	public static final int CINT_DEFAULT_AFTERFIRSTSTART_DELAY = 5;

	/**
	 * 默认刷新时间(单位S)
	 */
	public static final int CINT_DEFAULT_REFRESH_DELAY = 30;
	
	/**
	 * 是否停止定时器
	 */
	private boolean isPaused=false;
	
	/**
	 * 默认刷新时间间隔
	 */
	private int minterval;
	
	private int mafterfirststart;
	
	private Context mContext;
	
	private  PendingIntent msender = null;
	private  AlarmManager mam = null;
	/**
	 * 推送消息专用定时器
	 */
	private static AlarmUtil pushMsgAlarm;
	
	public static AlarmUtil getPushMsgAlarm(Context context) {
		if (pushMsgAlarm == null) {
			pushMsgAlarm = new AlarmUtil(context);
		}
		
		return pushMsgAlarm;
	}
	
	/**
	 * 构造函数，使用默认的时间
	 * @param context
	 */
	public AlarmUtil(Context context)
	{
		this(context,CINT_DEFAULT_AFTERFIRSTSTART_DELAY,CINT_DEFAULT_REFRESH_DELAY);
	}
			
	
	/**
	 * 构造函数
	 * @param context 当前context
	 * @param afterfirststart 第一次是在启动后多久触发，单位S，必须大于0
	 * @param interval 以后触发间隔时间，单位S，必须大于等于0(等于0表示只触发一次)
	 */
	public AlarmUtil(Context context,int afterfirststart,int interval)
	{
		mContext=context;
		if(afterfirststart>0)
			mafterfirststart=afterfirststart*1000;
		else
			mafterfirststart=CINT_DEFAULT_AFTERFIRSTSTART_DELAY*1000;
		if(interval>=0)
			minterval=interval*1000;
		else
			minterval=CINT_DEFAULT_REFRESH_DELAY*1000;
	}
	
	/**
	 * 设置执行广播任务(自动根据interval值设置重复(值大于0时)还是只执行一次(值等于0时))
	 * @param clazz extends BroadcastReceiver
	 */
	public void pendingBroadcastTask(String action)
	{
		Intent intent=new Intent(action);
		pendingBroadcastTask(intent);
	}
	
	/**
	 * 设置执行广播任务(自动根据interval值设置重复(值大于0时)还是只执行一次(值等于0时))
	 * @param clazz extends BroadcastReceiver
	 */
	public void pendingBroadcastTask(Class<?> clazz) 
	{
		Intent intent=new Intent(mContext,clazz);
		pendingBroadcastTask(intent);
		
	}
	
	/**
	 * 设置下一次间隔interval秒之后执行广播任务
	 * @param intent
	 * @param interval
	 */
	public void pendingBroadcastTask(Intent intent, int interval) {
		if (isPaused) {
			return;
		}
		
		if(interval>=0)
			minterval=interval*1000;
		else
			minterval=CINT_DEFAULT_REFRESH_DELAY*1000;
		
		long settime = SystemClock.elapsedRealtime() + minterval;
		
		msender=PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		mam=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		
		mam.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, settime, msender);
		
		Log.i(TAG, "alarm next time at "+minterval/1000+" seconds later!!");
	}
	
	private void pendingBroadcastTask(Intent intent)
	{
		msender=PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		mam=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		long settime=SystemClock.elapsedRealtime()+mafterfirststart;
		
		if(minterval>0)
		{
			mam.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, settime, minterval,msender);
			Log.i(TAG, "start alarm repeating!!");
		}
		else
		{
			mam.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, settime, msender);
			Log.i(TAG, "start alarm once!!");
		}
	}
	
	/**
	 * 中止执行任务
	 */
	public void stopTask()
	{
		if(mam!=null)
			mam.cancel(msender);
		Log.i(TAG, "stop alarm!!");
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

}
