package cn.ycoder.android.library.route;

import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PathReplaceService;

import cn.ycoder.android.library.BaseApplication;
import cn.ycoder.android.library.tool.LogUtils;

/**
 * 全局地址转换
 *
 * @author 启研
 * @created at 2017/4/10 11:10
 */
@Route(path = "/epsoft/pathReplace")
public class PathReplaceServiceImpl implements PathReplaceService {

  Context mContext;
  UriReplaceService[] uris;

  @Override
  public String forString(String path) {
    if (!path.startsWith("/arouter/")) {
      LogUtils.e("path (" + path + ") please use uri");
    }
    return path;
  }

  @Override
  public Uri forUri(Uri uri) {
    if (uris != null) {
      for (UriReplaceService service : uris) {
        uri = service.transform(uri);
      }
    }
    return uri;
  }

  @Override
  public void init(Context context) {
    this.mContext = context;
    this.uris = BaseApplication.getInstance().getUriReplaceService();
  }
}
