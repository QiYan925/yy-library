package cn.ycoder.android.library;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author 启研
 * @created at 2017/3/9.
 */
public abstract class BaseFragment extends RxFragment {

  Unbinder unbinder;

  /**
   * 绑定视图
   *
   * @param view 视图
   */
  protected final void bindView(View view) {
    this.unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (this.unbinder != null) {
      this.unbinder.unbind();
    }
  }

  public boolean onBackPressed() {
    return false;
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
    Activity activity = getActivity();
    if (activity instanceof BaseActivity) {
      ((BaseActivity) activity).showProgress(show, msg);
    }
  }

  public void showProgress(String loadMessage) {
    showProgress(true, loadMessage);
  }

  public Activity context() {
    return getActivity();
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

  private boolean isPrepared;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initPrepare();
  }

  /**
   * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
   */
  private boolean isFirstResume = true;

  @Override
  public void onResume() {
    super.onResume();
    if (isFirstResume) {
      isFirstResume = false;
      return;
    }
    if (getUserVisibleHint()) {
      onUserVisible();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (getUserVisibleHint()) {
      onUserInvisible();
    }
  }

  private boolean isFirstVisible = true;
  private boolean isFirstInvisible = true;

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      if (isFirstVisible) {
        isFirstVisible = false;
        initPrepare();
      } else {
        onUserVisible();
      }
    } else {
      if (isFirstInvisible) {
        isFirstInvisible = false;
        onFirstUserInvisible();
      } else {
        onUserInvisible();
      }
    }
  }

  private synchronized void initPrepare() {
    if (isPrepared) {
      onFirstUserVisible();
    } else {
      isPrepared = true;
    }
  }

  /**
   * 第一次fragment可见（进行初始化工作）
   */
  protected void onFirstUserVisible() {
    lazyLoad();
  }

  /**
   * fragment可见（切换回来或者onResume）
   */
  protected void onUserVisible() {
    lazyLoad();
  }

  /**
   * 第一次fragment不可见（不建议在此处理事件）
   */
  protected void onFirstUserInvisible() {

  }

  /**
   * fragment不可见（切换掉或者onPause）
   */
  protected void onUserInvisible() {

  }

  /**
   * 懒加载，onFirstUserVisible和onUserVisible集合
   */
  protected void lazyLoad() {
  }

}
