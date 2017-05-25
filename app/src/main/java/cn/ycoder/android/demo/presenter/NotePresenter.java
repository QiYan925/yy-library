package cn.ycoder.android.demo.presenter;


import android.view.View;
import cn.ycoder.android.library.presenter.IBaseView;
import cn.ycoder.android.library.presenter.IPresenter;

/**
 * Created by Administrator on 2017/3/12.
 */

public class NotePresenter extends IPresenter<NotePresenter.View> {

  public interface View extends IBaseView {

    void onLoadResult(String msg);
  }

  public NotePresenter(NotePresenter.View view) {
    super(view);
  }

  public void load() {
    getView().showProgress(true);
    //可在此方法里使用网络请求
    testLaterRun(1, new TestCallBack() {
      @Override
      public void test() {
        getView().onLoadResult("文字可点击");
        getView().showProgress(false);
      }
    });
  }
}
