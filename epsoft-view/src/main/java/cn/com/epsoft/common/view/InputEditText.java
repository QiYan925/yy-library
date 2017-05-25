package cn.com.epsoft.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author 启研
 * @created at 2017/4/13 10:40
 */

public class InputEditText extends AppCompatEditText {

  /**
   * 线宽
   */
  private final int LINE_WIDTH = 2;
  private final int SPACING;
  /**
   * 左侧图片
   */
  private Drawable mLeftDrawable;
  /**
   * 左侧文字
   */
  private String mLeftText;
  /**
   * 左侧文字颜色
   */
  private int mLeftTextColor;
  /**
   * 左侧文字大小
   */
  private float mLeftTextSize;
  /**
   * 左侧文字位置
   */
  private int leftTextGravity;
  private int divideType;
  private int dividerColor;
  Paint mLeftDrawablePaint;
  Paint mLeftTextPaint;
  Paint mLineDrawablePaint;
  int paddingLeft;
  int originalPaddingLeft;

  public InputEditText(Context context) {
    this(context, null);
  }

  public InputEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputEditText);
    this.mLeftDrawable = ta.getDrawable(R.styleable.InputEditText_leftDrawable);
    this.mLeftText = ta.getString(R.styleable.InputEditText_leftText);
    this.mLeftTextColor = ta
        .getColor(R.styleable.InputEditText_leftTextColor, Color.parseColor("#333333"));
    this.mLeftTextSize = ta
        .getDimension(R.styleable.InputEditText_leftTextSize, DensityUtil.dip2px(getContext(), 13));
    this.divideType = ta.getInt(R.styleable.InputEditText_divideType, -1);
    this.dividerColor = ta
        .getColor(R.styleable.InputEditText_dividerColor, Color.parseColor("#999999"));
    this.leftTextGravity = ta.getInt(R.styleable.InputEditText_leftTextGravity, 0);
    int spacing = ta.getInt(R.styleable.InputEditText_spacing, 10);

    ta.recycle();
    mLeftDrawablePaint = new Paint();
    mLeftDrawablePaint.setFilterBitmap(true);
    mLeftDrawablePaint.setAntiAlias(true);
    mLeftTextPaint = new Paint();
    mLeftTextPaint.setAntiAlias(true);
    mLeftTextPaint.setColor(mLeftTextColor);
    mLeftTextPaint.setStyle(Paint.Style.FILL);
    mLeftTextPaint.setTextSize(mLeftTextSize);
    mLeftTextPaint.setTextAlign(Paint.Align.CENTER);
    mLineDrawablePaint = new Paint();
    mLineDrawablePaint.setAntiAlias(true);
    mLineDrawablePaint.setColor(dividerColor);
    mLineDrawablePaint.setStyle(Paint.Style.FILL);
    mLineDrawablePaint.setStrokeWidth(LINE_WIDTH);
    if (mLeftText == null) {
      mLeftText = "";
    }
    float textLength = mLeftTextPaint.measureText(mLeftText);
    SPACING = DensityUtil.px2dip(context, spacing);
    int leftWidth = (int) Math.ceil(textLength) + 2 * SPACING;
    if (mLeftDrawable != null) {
      leftWidth += mLeftDrawable.getIntrinsicWidth();
    }
    originalPaddingLeft = getPaddingLeft();
    paddingLeft = originalPaddingLeft;
    setPadding(paddingLeft + leftWidth, getPaddingTop(), getPaddingRight(), getPaddingBottom());
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int drawableStratX = paddingLeft;
    int drawableEndX = drawableStratX;
    int textX = 0;
    int lineX = 0;
    int viewHeight = getHeight();
    int viewWidth = getWidth();
    if (mLeftDrawable != null || !TextUtils.isEmpty(mLeftText)) {
      if (mLeftDrawable != null) {
        drawableEndX += mLeftDrawable.getIntrinsicWidth();
        //绘图像
        mLeftDrawable
            .setBounds(drawableStratX, (viewHeight - mLeftDrawable.getIntrinsicHeight()) / 2,
                drawableEndX, (viewHeight - mLeftDrawable.getIntrinsicHeight()) / 2 + mLeftDrawable
                    .getIntrinsicHeight());
        mLeftDrawable.draw(canvas);
      }
      float textLength = 0;
      if (!TextUtils.isEmpty(mLeftText)) {
        //绘文字
        Paint.FontMetrics fm = mLeftTextPaint.getFontMetrics();
        int baseline;
        if (leftTextGravity == 1) {
          baseline = getPaddingTop() + getExtendedPaddingTop() + (int) ((fm.bottom - fm.top) / 2
              + fm.leading);
        } else {
          baseline = (int) (viewHeight - fm.bottom - fm.top) / 2;
        }
        textLength = mLeftTextPaint.measureText(mLeftText);
        textX = drawableEndX + (int) Math.ceil(textLength / 2);
        canvas.drawText(mLeftText, textX, baseline, mLeftTextPaint);
      }
      lineX = textX + (int) Math.ceil(textLength / 2);
    }
    //绘线条
    float[] lines = null;
    //只绘制底部
    if (divideType == 1) {
      lines = new float[]{
          0, viewHeight - LINE_WIDTH, viewWidth, viewHeight - LINE_WIDTH
      };
    } else if (divideType == 2) {
      lines = new float[]{
          originalPaddingLeft, viewHeight - LINE_WIDTH, viewWidth, viewHeight - LINE_WIDTH
      };
    } else if (divideType == 0) {
      lines = new float[]{
          0, 0, viewWidth, 0,
          lineX, 0, lineX, viewHeight,
          0, viewHeight, viewWidth, viewHeight
      };
    } else if (divideType == 3) {
      lines = new float[]{
          0, 0, viewWidth, 0,
          0, viewHeight, viewWidth, viewHeight
      };
    }
    if (divideType != -1) {
      canvas.drawLines(lines, mLineDrawablePaint);
    }
  }
}
