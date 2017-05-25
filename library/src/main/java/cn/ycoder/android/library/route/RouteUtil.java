package cn.ycoder.android.library.route;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 启研
 * @created at 2017/4/7 9:25
 */

public class RouteUtil {

  /**
   * 可使用simple套fragment，减少Activity数量
   * {@link cn.ycoder.android.library.SimpleActivity}
   */
  public static final String URI_SIMPLE = "/base/simple";
  /**
   * uri_http前缀
   */
  public static final String URI_PREFIX_HTTP = "http";
  /**
   * 网页浏览器
   * {@link cn.ycoder.android.library.WebActivity}
   */
  public static final String URI_APP_WEB_BROWSER = "/app/webBrowser";

  public interface Params {

    /**
     * fragmentUri
     */
    String FRAGMENT_URI = "fragmentUri";
    /**
     * 屏幕方向
     */
    String SCREEN_ORIENTATION = "screenOrientation";

  }

  /**
   * 提取path
   */
  public static String extractPath(Postcard postcard) {
    String path = postcard.getPath();
    //simple里的界面
    if (RouteUtil.URI_SIMPLE.equals(path)) {
      String fragmentUri = null;
      Bundle bundle = postcard.getExtras();
      if (bundle != null) {
        fragmentUri = bundle.getString(Params.FRAGMENT_URI, path);
      }
      path = fragmentUri;
    }
    return path;
  }

  /**
   * 快速构建
   *
   * @param activityStr activiy的uri
   */
  public static android.net.Uri builder(String activityStr) {
    return new RouteUtil.Builder(activityStr).build();
  }

  /**
   * 快速构建
   *
   * @param activityStr activiy的uri
   * @param stealUri 窃取uri的参数(uri.toString)
   */
  public static android.net.Uri builder(String activityStr, String stealUri) {
    return new RouteUtil.Builder(activityStr).stealParams(stealUri).build();
  }

  /**
   * 在SimpleActivity下的fragment
   */
  public static android.net.Uri builderWithFragment(String fragStr) {
    return new RouteUtil.Builder().
        fragment(fragStr).build();
  }

  /**
   * 在SimpleActivity下的fragment
   *
   * @param stealUri 窃取uri的参数(uri.toString)
   */
  public static android.net.Uri builderWithFragment(String fragStr, String stealUri) {
    return new RouteUtil.Builder().
        fragment(fragStr).stealParams(stealUri).build();
  }


  private static class Builder {

    private String url;
    private String fragment;
    private Map<String, Object> map = new HashMap();

    public Builder() {
      this.url = URI_SIMPLE;
    }

    public Builder(String url) {
      this.url = url;
    }

    public Builder fragment(String url) {
      this.fragment = url;
      return this;
    }

    /**
     * 偷取其他参数
     */
    public Builder stealParams(String uri) {
      android.net.Uri u = android.net.Uri.parse(uri);
      Set<String> set = u.getQueryParameterNames();
      for (String key : set) {
        map.put(key, u.getQueryParameter(key));
      }
      return this;
    }


    public android.net.Uri build() { // 构建，返回一个新对象
      if (TextUtils.isEmpty(url)) {
        throw new IllegalArgumentException("url not null");
      }
      android.net.Uri.Builder builder = new android.net.Uri.Builder();
      builder.path(url);
      if (!TextUtils.isEmpty(fragment)) {
        builder.appendQueryParameter(Params.FRAGMENT_URI, fragment);
      }
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
      }
      return builder.build();
    }
  }
}
