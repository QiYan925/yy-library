package cn.ycoder.android.demo;

import android.content.Context;
import cn.ycoder.android.demo.store.AppStore;
import cn.ycoder.android.library.BaseApplication;
import cn.ycoder.android.library.route.UriReplaceService;
import cn.ycoder.android.library.store.AppTagStore;

/**
 * @author 启研
 * @created at 2017/5/25 11:40
 */

public class App extends BaseApplication {

  /**
   * 仅初始化一次
   */
  @Override
  public void onlyInit() {

  }

  @Override
  public AppTagStore initAppTag(Context context) {
    return new AppStore();
  }

  /**
   * 获得需转换的路径服务
   */
  @Override
  public UriReplaceService[] getUriReplaceService() {
    return null;
  }
}
