package cn.ycoder.android.demo;

import cn.ycoder.android.library.BaseApplication;
import cn.ycoder.android.library.route.UriReplaceService;

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

  /**
   * 获得需转换的路径服务
   */
  @Override
  public UriReplaceService[] getUriReplaceService() {
    return null;
  }

  /**
   * 得到tag
   */
  @Override
  public Object getTag(String tag) {
    return null;
  }

  /**
   * 设置tag数据
   */
  @Override
  public void setTag(String tag, Object user) {

  }
}
