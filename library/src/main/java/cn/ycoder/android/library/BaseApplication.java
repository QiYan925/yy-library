package cn.ycoder.android.library;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import cn.ycoder.android.library.store.AppTagStore;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;

import cn.ycoder.android.library.route.UriReplaceService;
import cn.ycoder.android.library.tool.ErrorHandlers;
import cn.ycoder.android.library.tool.LogUtils;
import cn.ycoder.android.library.tool.Utils;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author 启研
 * @created at 2017/4/5 13:57
 */

public abstract class BaseApplication extends Application {

  public static boolean debug = true;

  private static BaseApplication app;

  /**
   * 获取Application单例
   */
  public static BaseApplication getInstance() {
    if (app == null) {
      new NullPointerException("MyApplication is null!");
    }
    return app;
  }

  private AppTagStore mAppTag;

  @Override
  public void onCreate() {
    super.onCreate();
    if (isMainProcess(this)) {
      app = this;
      //工具使用App
      Utils.init(app, app.getPackageName());
      RxJavaPlugins.setErrorHandler(ErrorHandlers.displayErrorConsumer(app));
      onlyInit();
      //开启日志
      LogUtils.setAllEnable(debug);
      //开启路由
      if (debug) {
        ARouter.openLog();
        ARouter.openDebug();
      }
      ARouter.init(app);
      mAppTag = initAppTag();
    }
  }


  /**
   * 仅初始化一次
   */
  public abstract void onlyInit();

  public abstract AppTagStore initAppTag();

  private boolean isMainProcess(Context context) {
    ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
    List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
    String mainProcessName = context.getPackageName();
    int myPid = android.os.Process.myPid();
    for (ActivityManager.RunningAppProcessInfo info : processInfoList) {
      if (info.pid == myPid && mainProcessName.equals(info.processName)) {
        packageName = mainProcessName;
        return true;
      }
    }
    return false;
  }

  protected String packageName;

  /**
   * 获取app的包
   */
  public String getAppPackage() {
    return packageName;
  }


  /**
   * 历史Postcard
   */
  protected Postcard historyPostcard;

  /**
   * 获得历史Postcard
   */
  public Postcard getHistoryPostcard() {
    return historyPostcard;
  }

  /**
   * 存放历史Postcard，可在路由拦截器中设值
   */
  public void setHistoryPostcard(Postcard historyPostcard) {
    this.historyPostcard = historyPostcard;
  }

  /**
   * 获得需转换的路径服务
   */
  public abstract UriReplaceService[] getUriReplaceService();

  /**
   * 得到tag数据
   */
  public Object getTag(String tag) {
    if (mAppTag != null) {
      return mAppTag.getTag(tag);
    }
    return null;
  }

  /**
   * 设置tag数据
   */
  public void setTag(String tag, Object value) {
    if (mAppTag != null) {
      mAppTag.setTag(tag, value);
    }
  }
}
