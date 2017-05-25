package cn.ycoder.android.library.store;

/**
 * @author 启研
 * @created at 2017/5/25 23:50
 */

public interface AppTagStore {

  /**
   * 得到tag数据
   */
  Object getTag(String tag);

  /**
   * 设置tag数据
   */
  void setTag(String tag, Object user);
}
