package cn.ycoder.android.library.tool;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Created by Yu on 2016/3/14 0014.
 */
public class ActivitiesManager {
    private static Stack<Activity> activityStack;
    private static ActivitiesManager instance;

    private ActivitiesManager() {
        if (activityStack == null) {
            activityStack = new Stack();
        }

    }

    public static ActivitiesManager getInstance() {
        if (instance == null)
            synchronized (ActivitiesManager.class) {
                if (instance == null) {
                    instance = new ActivitiesManager();
                }
            }
        return instance;
    }

    /**
     * 持有的activity总数
     *
     * @return
     */
    public int holdActivityCount() {
        if (activityStack == null) return 0;
        return activityStack.size();
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }

    }

    public void finishActivity(Class... cles) {
        int cycleCount = 0;
        if (activityStack != null) {
            int count = activityStack.size();
            int i = 0;

            while (true) {
                Activity activity;
                do {
                    if (i >= count) {
                        return;
                    }

                    activity = activityStack.get(i);
                    if (cycleCount > 50) {
                        return;
                    }
                } while (activity == null);

                Class[] var6 = cles;
                int var7 = cles.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    Class cls = var6[var8];
                    if (activity.getClass().equals(cls)) {
                        activity.finish();
                        activityStack.remove(activity);
                        activity = null;
                        count = activityStack.size();
                        break;
                    }
                }

                if (activity != null) {
                    ++i;
                }

                ++cycleCount;
            }
        }
    }

    public void finishActivity() {
        Activity activity = this.currentActivity();
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }

    }

    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.empty()) {
            activity = activityStack.lastElement();
        }

        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }

        activityStack.add(activity);
    }

    public boolean finishAllActivityExceptOne(Class<?> cls) {
        boolean flag = true;
        while (flag) {
            Activity activity = this.currentActivity();
            if (activity == null) {
                break;
            }

            if (activity.getClass().equals(cls)) {
                flag = false;
            } else if (activity.isFinishing()) {
                activityStack.remove(activity);
                activity = null;
            } else {
                this.finishActivity(activity);
            }
        }
        return flag;
    }

    public void finishAllActivity() {
        int i = 0;
        for (int size = activityStack.size(); i < size; ++i) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 当前队列是否拥有这个Activity
     *
     * @return
     */
    public static boolean isExist(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        return false;
    }

    /**
     * 完全退出应用程序
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
