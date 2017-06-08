package cn.ycoder.android.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.ycoder.android.demo.R;
import cn.ycoder.android.demo.store.AppStore;
import cn.ycoder.android.library.BaseApplication;
import cn.ycoder.android.library.ToolbarFragment;
import cn.ycoder.android.library.tool.ToastUtils;
import cn.ycoder.android.library.widget.MultipleStatusView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;

/**
 * @author 启研
 * @created at 2017/4/20 11:29
 */
@Route(path = "/main/test")
public class TestFragment extends ToolbarFragment {
  MultipleStatusView multipleStatusView;
  TextView text;
  @Autowired
  String msg;
  @Autowired
  int tab;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_test, container, false);
    super.initToolbar(view, "测试界面");
    text = (TextView) view.findViewById(R.id.text);
    multipleStatusView= (MultipleStatusView) view.findViewById(R.id.multipleStatusView);
    multipleStatusView.showLoading();
    ARouter.getInstance().inject(this);
    text.setText("显示的是：" + msg);
    Gson gson=new Gson();
    ToastUtils.showLong(BaseApplication.getInstance().getTag(AppStore.KEY_TAG1).toString()
        + BaseApplication.getInstance().getTag(AppStore.KEY_TAG2).toString());
    return view;
  }

  @Override
  public boolean canBack() {
    return true;
  }
}
