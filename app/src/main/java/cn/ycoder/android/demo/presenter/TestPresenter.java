package cn.ycoder.android.demo.presenter;

import cn.ycoder.android.library.presenter.IBaseView;
import cn.ycoder.android.library.presenter.IPresenter;

/**
 * @author 启研
 * @created at 2017/6/22 15:21
 */

public class TestPresenter extends IPresenter<TestPresenter.View>{

  public interface View extends IBaseView{
    void onLoadResult();
  }

  public TestPresenter(View view) {
    super(view);
  }

  public void load(){
    getView().showProgress(true);
    testLaterRun(3, new TestCallBack() {
      @Override
      public void test() {
        getView().onLoadResult();
        getView().showProgress(false);
      }
    });
  }

}
