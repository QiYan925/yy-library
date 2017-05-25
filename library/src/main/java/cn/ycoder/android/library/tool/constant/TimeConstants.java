package cn.ycoder.android.library.tool.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/03/13
 *     desc  : 时间相关常量
 * </pre>
 */
public final class TimeConstants {
    public static final String yyyyMM = "yyyyMM";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy = "yyyy";

    /**
     * 秒与毫秒的倍数
     */
    public static final int MSEC = 1;
    /**
     * 秒
     */
    public static final int SEC = 1000 * MSEC;
    /**
     * 分
     */
    public static final int MIN = 60 * SEC;
    /**
     * 时
     */
    public static final int HOUR = 60 * MIN;
    /**
     * 天
     */
    public static final int DAY = 24 * HOUR;
    /**
     * 周
     */
    public static final int WEEK = 7 * DAY;

    @IntDef({MSEC, SEC, MIN, HOUR, DAY, WEEK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
