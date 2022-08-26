package com.example.activitytest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        // 类似于栈 先获取最先进入的活动->判断状态->finish关闭
        for (Activity activity : activities) {
            // 判断是否活动是否已经结束
            if (!activity.isFinishing()) {
                System.out.println("这个活动==>" + activity.toString());
                activity.finish();
                System.out.println("结束");
            }
        }
        activities.clear();
    }
}
