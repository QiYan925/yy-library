package cn.ycoder.android.library.tool.objectify;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * Created by tsung on 5/19/14.
 */
public class ObjectPreferenceLoader {
    StringPreferenceLoader stringPreferenceLoader;
    Type clazz;
    Context context;
    Gson gson;

    public <T> ObjectPreferenceLoader(Context context, final String key, Class<T> clazz) {
        this(context, key, clazz, new Gson());
    }

    public <T> ObjectPreferenceLoader(Context context, final String key, Class<T> clazz, Gson gson) {
        this.context = context;
        this.clazz = clazz;
        stringPreferenceLoader = new StringPreferenceLoader(context, key);
        this.gson = gson;
    }


    public <T> T load() throws NoSuchPreferenceFoundException {
        String value = stringPreferenceLoader.load();
        try {
            return gson.fromJson(value, clazz);
        } catch (Exception e) {
            throw new NoSuchPreferenceFoundException();
        }
    }

    public <T> T load(TypeToken typeToken) throws NoSuchPreferenceFoundException {
        String value = stringPreferenceLoader.load();
        try {
            return gson.fromJson(value, typeToken.getType());
        } catch (Exception e) {
            throw new NoSuchPreferenceFoundException();
        }
    }

    public <T> void save(T value) {
        if (((Class) clazz).isInstance(value)) {
            stringPreferenceLoader.save(gson.toJson(value));
        } else {
            throw new ShouldSaveSameTypeValueException();
        }
    }

    public void remove() {
        stringPreferenceLoader.remove();
    }

    public static void clear(Context context) {
        StringPreferenceLoader.clear(context);
    }

}
