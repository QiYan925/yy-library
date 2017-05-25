package cn.com.epsoft.common.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author 启研
 * @created at 2017/5/1 14:14
 */

public class VerifyCodeView extends android.support.v7.widget.AppCompatTextView {

  CountDownTimer timer = null;
  String mText;

  public VerifyCodeView(Context context) {
    this(context, null);
  }

  public VerifyCodeView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mText = getText().toString();
    timer = new CountDownTimer(60 * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        setText(String.format("%ds",
            millisUntilFinished / 1000));
      }

      @Override
      public void onFinish() {
        timeFinish();
      }
    };
  }

  /**
   * 开始计数器
   */
  public void start() {
    timer.start();
    setClickable(false);
    setAlpha(0.5f);
  }


  private void timeFinish() {
    setClickable(true);
    setText(mText);
    setAlpha(1f);
  }

  /**
   * 状态还原
   */
  public void revert() {
    if (timer != null) {
      timer.cancel();
      timeFinish();
    }
  }

  /**
   * 结束倒计时
   */
  public void finishDownTimer() {
    if (timer != null) {
      timer.onFinish();
    }
  }
}
