package cn.ycoder.android.library.api;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author 启研
 * @created at 2017/5/10 14:37
 */

public class IntegerDefaultAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

  final int defaultValue;

  public IntegerDefaultAdapter(int defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    try {
      if (!TextUtils.isEmpty(json.getAsString())) {
        return json.getAsInt();
      }
    } catch (Exception ignore) {
    }
    return defaultValue;
  }

  @Override
  public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src);
  }
}
