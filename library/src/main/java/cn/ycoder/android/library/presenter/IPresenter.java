package cn.ycoder.android.library.presenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author 启研
 * @created at 2017/3/14 10:52
 */

public abstract class IPresenter<V extends IBaseView> {

  V view;

  public IPresenter(V v) {
    this.view = v;
  }

  public V getView() {
    return this.view;
  }

  /**
   * 测试延时运行
   */
  protected void testLaterRun(int second, final TestCallBack callBack) {
    testLaterRun(second, TimeUnit.SECONDS, callBack);
  }

  /**
   * 测试延时运行
   */
  protected void testLaterRun(int second, TimeUnit unit, final TestCallBack callBack) {
    Flowable.timer(second, unit)
        .compose(view.<Long>bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Long>() {
          @Override
          public void accept(@NonNull Long aLong) throws Exception {
            callBack.test();
          }
        });
  }

  protected interface TestCallBack {

    void test();
  }
}
