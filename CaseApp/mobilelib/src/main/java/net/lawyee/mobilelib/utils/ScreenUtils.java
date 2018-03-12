package net.lawyee.mobilelib.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 *  屏幕工具
 * Created by nereo on 15/11/19.
 * Updated by nereo on 2016/1/19.
 */
public class ScreenUtils {

    public static Point getScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point out = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(out);
        }else{
            int width = display.getWidth();
            int height = display.getHeight();
            out.set(width, height);
        }
        return out;
    }
    /**
     * 获取屏幕宽
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context)
    {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //WindowManager windowManager = act.getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        return display.getWidth();
    }
    /**
     * 获取屏幕宽
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context)
    {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //WindowManager windowManager = act.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }

    public static int dipTopx(Context context, float dipValue){
      final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale +0.5f);
    }
    public static int pxTodip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale +0.5f);
    }
}
