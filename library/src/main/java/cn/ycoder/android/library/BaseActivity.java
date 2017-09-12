package cn.ycoder.android.library;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import cn.ycoder.android.library.tool.ActivitiesManager;
import com.alibaba.android.arouter.facade.Postcard;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * @author 启研
 * @created at 2017/3/9.
 */
public class BaseActivity extends RxAppCompatActivity {
  public final static String TAG_HISTORY_POSTCARD = "historyPostcard";

  protected ProgressDialog dialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivitiesManager.getInstance().pushActivity(this);
    dialog = new ProgressDialog(this);
    TypedValue value = new TypedValue();
    if (getTheme().resolveAttribute(R.attr.indeterminateProgressBar, value, true)) {
      dialog.setIndeterminateDrawable(ContextCompat.getDrawable(getBaseContext(),value.resourceId));
    }
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
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    ActivitiesManager.getInstance().finishActivity(this);
    if (destroyClearPostcard() && BaseApplication.getInstance() != null) {
      Postcard postcard = (Postcard) BaseApplication.getInstance()
          .getTag(TAG_HISTORY_POSTCARD);
      if (postcard != null) {
        //清理意图
        BaseApplication.getInstance().setTag(TAG_HISTORY_POSTCARD,null);
      }
    }
  }

  /**
   * 销毁的时候清理意图
   */
  protected boolean destroyClearPostcard() {
    return true;
  }
}
