package cn.ycoder.android.library;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.alibaba.android.arouter.facade.Postcard;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.ycoder.android.library.tool.ActivitiesManager;

/**
 * @author 启研
 * @created at 2017/3/9.
 */
public class BaseActivity extends RxAppCompatActivity {

  private ProgressDialog dialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivitiesManager.getInstance().pushActivity(this);
    dialog = new ProgressDialog(this);
    dialog.setIndeterminate(true);
    dialog.setMessage("请稍候...");
    dialog.setCanceledOnTouchOutside(false);
  }

  /**
   * 显示加载条
   */
  public void showProgress(boolean show) {
    showProgress(show, "请稍候...");
  }

  /**
   * 显示加载条
   */
  protected void showProgress(boolean show, String msg) {
    if (show) {
      dialog.setMessage(msg);
      dialog.show();
    } else {
      dialog.dismiss();
    }
  }

  /**
   * 显示不同文案
   */
  public void showProgress(String loadMessage) {
    showProgress(true, loadMessage);
  }

  public Activity context() {
    return this;
  }

  /**
   * 获取字符串
   */
  public String string(@StringRes int resId) {
    return getString(resId);
  }

  /**
   * 获取字符数组
   */
  public String[] array(@ArrayRes int resId) {
    return getResources().getStringArray(resId);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    showProgress(false);
    if (destroyClearPostcard() && cn.ycoder.android.library.BaseApplication.getInstance() != null) {
      Postcard postcard = cn.ycoder.android.library.BaseApplication.getInstance().getHistoryPostcard();
      if (postcard != null) {
        //清理意图
        cn.ycoder.android.library.BaseApplication.getInstance().setHistoryPostcard(null);
      }
    }
  }

  /**
   * 销毁的时候清理意图
   */
  protected boolean destroyClearPostcard() {
    return true;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    ActivitiesManager.getInstance().finishActivity(this);
  }
}
