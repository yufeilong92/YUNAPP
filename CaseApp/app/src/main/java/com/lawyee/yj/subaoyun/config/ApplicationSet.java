package com.lawyee.yj.subaoyun.config;

import android.app.Activity;
import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfl on 2016/10/21 14:11.
 *
 * @purpose:
 */

public class ApplicationSet extends Application {
    private List<Activity> mActivities = new ArrayList<>();

    private static ApplicationSet mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
    }

    public synchronized static ApplicationSet getInstance() {
        return mApplication;
    }
    public void addActivity(Activity activity) {
        if (activity == null)
            return;
        if (mActivities != null && mActivities.size() > 0) {
            if (!mActivities.contains(activity)) {
                mActivities.add(activity);
            }
        } else {
            mActivities.add(activity);
        }

    }

    public void removeActivity(Activity activity) {
        if (mActivities != null && mActivities.size() > 0) {
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
        }
    }

    // 遍历所有Activity并finish
    public void finishAllActivity() {
        if (mActivities != null && mActivities.size() > 0) {
            for (Activity activity : mActivities) {
                activity.finish();
            }
        }
    }
}
