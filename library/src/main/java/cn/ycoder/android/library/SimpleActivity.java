package cn.ycoder.android.library;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import cn.ycoder.android.library.tool.LogUtils;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.lang.ref.WeakReference;
import java.util.Set;

import cn.ycoder.android.library.route.RouteUtil;
import cn.ycoder.android.library.tool.StringUtils;

/**
 * 公用显示的容器
 *
 * @created at 2017/3/9.
 */
@Route(path = RouteUtil.URI_SIMPLE)
public class SimpleActivity extends BaseActivity {

  public static final String FRAGMENT_NOT_FOUND = "/simple/notFound";

  private static final String TAG = "SimpleActivity";
  @Autowired(name = RouteUtil.Params.FRAGMENT_URI)
  String fragmentUri;
  @Autowired(name = RouteUtil.Params.SCREEN_ORIENTATION)
  int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
  private WeakReference<Fragment> mFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.yy_library_act_simple);
    ARouter.getInstance().inject(this);
    if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
      setRequestedOrientation(screenOrientation);
    }
    Intent data = getIntent();
    if (data == null) {
      throw new RuntimeException(
          "you must provide a page info to display");
    }
    try {
      Fragment fragment = (Fragment) ARouter.getInstance().build(fragmentUri).navigation();
      if (fragment != null) {
        String uriString = getIntent().getStringExtra(ARouter.RAW_URI);
        Uri uri = null;
        if (!TextUtils.isEmpty(uriString)) {
          uri = Uri.parse(uriString);
        }
        Bundle args = getIntent().getExtras();
        if (args == null) {
          args = new Bundle();
        }
        if (uri != null) {
          Set<String> keys = uri.getQueryParameterNames();
          for (String key : keys) {
            String value = uri.getQueryParameter(key);
            //类型处理，可增加
            if (StringUtils.isBoolean(value)) {
              args.putBoolean(key, Boolean.parseBoolean(value));
              continue;
            }
            args.putString(key, value);
          }
        }
        fragment.setArguments(args);
      } else {
        LogUtils.e("you fragmentUri is null [" + fragmentUri + "]");
        fragment = (Fragment) ARouter.getInstance().build(FRAGMENT_NOT_FOUND).navigation();
      }
      FragmentTransaction trans = getSupportFragmentManager()
          .beginTransaction();
      trans.replace(R.id.container, fragment, TAG);
      trans.commit();
      mFragment = new WeakReference<Fragment>(fragment);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException(
          "generate fragment error. by value:" + fragmentUri);
    }
  }

  @Override
  public void onBackPressed() {
    if (mFragment != null && mFragment.get() != null
        && mFragment.get() instanceof BaseFragment) {
      BaseFragment bf = (BaseFragment) mFragment.get();
      if (!bf.onBackPressed()) {
        super.onBackPressed();
      }
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onActivityResult(int arg0, int arg1, Intent arg2) {
    super.onActivityResult(arg0, arg1, arg2);
    if (mFragment != null && mFragment.get() != null) {
      mFragment.get().onActivityResult(arg0, arg1, arg2);
    }
  }
}
