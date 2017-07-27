package cn.ycoder.android.library.tool;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * @author 启研
 * @created at 2017/4/19 11:30
 */

public class ImageUtils {
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colorStateList) {
        Drawable drawable1 = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable1, colorStateList);
        return drawable1;
    }

    public static Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
        Drawable drawable1 = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable1, ColorStateList.valueOf(color));
        return drawable1;
    }

    public static int getDrawableResource(String drawableName,@DrawableRes int defaultRes) {
        Context ctx = Utils.getContext();
        int resId = ctx.getResources().getIdentifier(drawableName, "drawable", ctx.getPackageName());
        return resId;
    }
}
