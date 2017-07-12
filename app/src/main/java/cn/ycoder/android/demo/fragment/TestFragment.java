package cn.ycoder.android.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import cn.ycoder.android.demo.R;
import cn.ycoder.android.demo.presenter.TestPresenter;
import cn.ycoder.android.demo.store.AppStore;
import cn.ycoder.android.library.BaseApplication;
import cn.ycoder.android.library.ToolbarFragment;
import cn.ycoder.android.library.tool.ToastUtils;
import cn.ycoder.android.library.widget.MultipleStatusView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author 启研
 * @created at 2017/4/20 11:29
 */
@Route(path = "/main/test")
public class TestFragment extends ToolbarFragment implements TestPresenter.View {

  @BindView(R.id.multipleStatusView)
  MultipleStatusView multipleStatusView;
  @BindView(R.id.text)
  TextView text;
  @Autowired
  String msg;
  @Autowired
  int tab;
  TestPresenter presenter = new TestPresenter(this);

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_test, container, false);
    super.bindToolbarView(view, "测试界面");
    multipleStatusView.showLoading();
    ARouter.getInstance().inject(this);
    text.setText("显示的是：" + msg);
    ToastUtils.showLong(BaseApplication.getInstance().getTag(AppStore.KEY_TAG1).toString()
        + BaseApplication.getInstance().getTag(AppStore.KEY_TAG2).toString());
    presenter.load();
    return view;
  }

  @Override
  public void onLoadResult() {
    ToastUtils.showLong("加载成功");
    multipleStatusView.showNoNetwork();
  }

  @Override
  public void showProgress(boolean show) {
    //使用MultipleStatusView加载后就不调用ProgressDialog了
    if (show && multipleStatusView.getViewStatus() != MultipleStatusView.STATUS_LOADING) {
      super.showProgress(show);
    }
  }

  @Override
  public boolean canBack() {
    return true;
  }
}
