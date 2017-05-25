package cn.ycoder.android.demo.store;

import cn.ycoder.android.library.store.AppTagStore;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 启研
 * @created at 2017/5/25 23:56
 */

public class AppStore implements AppTagStore {

  public static final String KEY_TAG1 = "tag1";
  public static final String KEY_TAG2 = "tag2";

  private Map<String, Object> map = new HashMap<>();

  /**
   * 得到tag数据
   */
  @Override
  public Object getTag(String tag) {
    return map.get(tag);
  }

  /**
   * 设置tag数据
   */
  @Override
  public void setTag(String tag, Object user) {
    map.put(tag, user);
  }
}
