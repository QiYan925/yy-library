package cn.ycoder.android.library.presenter;

import android.app.Activity;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * Created by Administrator on 2017/3/12.
 */

public interface IBaseView {

  /**
   * 加载 true 显示 false不显示
   */
  void showProgress(boolean show);

  /**
   * 显示加载，文字改变
   */
  void showProgress(String loadMessage);

  /**
   * 生命周期管理
   */
  <T> LifecycleTransformer<T> bindToLifecycle();

  /**
   * 拿到context
   */
  Activity context();

  /**
   * 通过resId等到string文字
   */
  String string(@StringRes int resId);

  /**
   * 通过resId得到array数组
   */
  String[] array(@ArrayRes int resId);

  interface IActivityView extends IBaseView {

    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event);
  }

  interface IFragmentView extends IBaseView {

    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event);
  }
}
