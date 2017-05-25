package cn.ycoder.android.library.store;

import com.google.gson.Gson;

import cn.ycoder.android.library.tool.Utils;
import cn.ycoder.android.library.tool.objectify.BooleanPreferenceLoader;
import cn.ycoder.android.library.tool.objectify.LongPreferenceLoader;
import cn.ycoder.android.library.tool.objectify.ObjectPreferenceLoader;
import cn.ycoder.android.library.tool.objectify.StringPreferenceLoader;

/**
 * @author 启研
 * @created at 2017/4/24 10:44
 */

public class PreferenceStores {

  public static ObjectPreferenceLoader objectPreference(final String key, Class clazz) {
    return new ObjectPreferenceLoader(Utils.getContext(), key, clazz);
  }

  public static ObjectPreferenceLoader objectPreference(final String key, Class clazz, Gson gson) {
    return new ObjectPreferenceLoader(Utils.getContext(), key, clazz, gson);
  }


  public static StringPreferenceLoader stringPreference(String key) {
    return new StringPreferenceLoader(Utils.getContext(), key);
  }

  public static BooleanPreferenceLoader booleanPreference(String key) {
    return new BooleanPreferenceLoader(Utils.getContext(), key);
  }


  public static LongPreferenceLoader longPreference(String key) {
    return new LongPreferenceLoader(Utils.getContext(), key);
  }
}
