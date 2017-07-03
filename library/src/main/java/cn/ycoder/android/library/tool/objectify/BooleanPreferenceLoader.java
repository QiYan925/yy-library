package cn.ycoder.android.library.tool.objectify;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tsung on 5/19/14.
 */
public class BooleanPreferenceLoader extends PreferenceLoader {

    public BooleanPreferenceLoader(Context context, String key) {
        super(context, key);
    }

    public boolean load(boolean defaultValue) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean value = appSharedPrefs.getBoolean(getKey(), defaultValue);
        return value;
    }

    public void save(boolean value) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean(getKey(), value);
        prefsEditor.apply();
    }

}
