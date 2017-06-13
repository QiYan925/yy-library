package cn.ycoder.android.library.tool;

import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.text.MessageFormat;

/**
 * @author maxczc
 * @since 5/5/17
 */

public class StringUtils {

  /**
   * 格式化加颜色
   *
   * @param argsColor 插入字符颜色
   * @param format 格式为：{坐标}，姓名：{0}-科目：{1}-{2}-{3}-{4}，
   * @param argsSize 字体大小
   */
  public static SpannableStringBuilder formatTextStyle(@ColorInt int argsColor, String format,
      int argsSize, Object... args) {
    SpannableStringBuilder style = new SpannableStringBuilder(MessageFormat.format(format, args));
    String formatTemp = format;
    for (int i = 0; i < args.length; i++) {
      String key = "{" + i + "}";
      int start = formatTemp.indexOf(key);
      if (start < 0) {
        continue;
      }
      formatTemp = formatTemp.replace(key, String.valueOf(args[i]));
      int end = String.valueOf(args[i]).length();
      style.setSpan(new ForegroundColorSpan(argsColor), start, start + end,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      if (argsSize != -1) {
        style.setSpan(new AbsoluteSizeSpan(argsSize), start, start + end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }
    return style;
  }

  /**
   * 格式化加颜色
   *
   * @param argsColor 插入字符颜色
   * @param format 格式为：{坐标}，姓名：{0}-科目：{1}-{2}-{3}-{4}，
   */
  public static SpannableStringBuilder formatTextStyle1(@ColorInt int argsColor, String format,
      Object... args) {
    return formatTextStyle(argsColor, format, -1, args);
  }


  /**
   * 是否数字
   */
  public static boolean isInteger(String str) {
    try {
      Integer.valueOf(str);
      return true;
    } catch (Exception e) {

    }
    return false;
  }

  /**
   * 是否布尔
   */
  public static boolean isBoolean(String str) {
    try {
      if (TextUtils.isEmpty(str)) {
        return false;
      }
      return "false".equals(str.toLowerCase()) || "true".equals(str.toLowerCase());
    } catch (Exception e) {

    }
    return false;
  }

  /**
   * 验证坐标在数组的位置
   *
   * @param array 数组
   * @param index 位置
   * @param failIndex 如果超出了则取的位置
   */
  public static String getArrayIndex(String[] array, int index, int failIndex) {
    int length = array.length;
    if (index > length - 1 || index < 0) {
      index = failIndex;
    }
    return array[index];
  }

  /**
   * 验证坐标在数组的位置
   *
   * @param array 数组
   * @param index 位置
   * @param failIndex 如果超出了则取的位置
   */
  public static int getArrayIndex(int[] array, int index, int failIndex) {
    int length = array.length;
    if (index > length - 1 || index < 0) {
      index = failIndex;
    }
    return array[index];
  }
}
