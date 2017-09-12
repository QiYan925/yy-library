package cn.com.epsoft.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 该控件还存在使用缺陷，如无法满足您的需求，请自动布局
 *
 * @author 启研
 * @created at 2017/3/18 15:10
 */
public class PureRowTextView extends AppCompatTextView {

  private final static int PURE_DIRECTION_LEFT = 0;
  private final static int PURE_DIRECTION_RIGHT = 1;

  /**
   * 线高
   */
  private final int LINE_HEIGHT = 1;
  /**
   * 文字
   */
  private String pureText;
  /**
   * 颜色
   */
  private int pureTextColor;
  /**
   * 分割线颜色
   */
  private int dividerColor;
  /**
   * 文字大小
   */
  private float pureTextSize;
  /**
   * 分割线位置
   */
  @ViewDivide
  private int viewDivide;
  /**
   * pure的位置
   */
  int pureDirection;
  /**
   * 是否提示
   */
  private boolean isHint = true;

  Paint pureTextPaint;
  Paint lineDrawablePaint;
  Paint mBitPaint;
  int textX;


  boolean frist = true;

  int originalPaddingLeft, originalPaddingRight, drawableLeftWidth, drawableRightWidth, drawablePadding;
  Drawable drawableLeft, drawableRight;

  public PureRowTextView(Context context) {
    this(context, null);
  }

  public PureRowTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PureRowTextView);
    this.pureText = ta.getString(R.styleable.PureRowTextView_pureText);
    this.pureTextColor = ta
        .getColor(R.styleable.PureRowTextView_pureTextColor, Color.parseColor("#999999"));
    this.dividerColor = ta
        .getColor(R.styleable.PureRowTextView_dividerColor, Color.parseColor("#999999"));
    this.pureTextSize = ta.getDimension(R.styleable.PureRowTextView_pureTextSize,
        DensityUtil.dip2px(getContext(), 13));
    //noinspection WrongConstant
    this.viewDivide = ta.getInt(R.styleable.PureRowTextView_viewDivide, VIEWDIVIDE_NONE);
    this.pureDirection = ta.getInt(R.styleable.PureRowTextView_pureDirection, PURE_DIRECTION_RIGHT);
    int spacing = ta.getInt(R.styleable.PureRowTextView_spacing, 0);
    ta.recycle();
    StringBuilder spacingSb = new StringBuilder();
    for (int i = 0; i < spacing; i++) {
      spacingSb.append("  ");
    }
    if (!TextUtils.isEmpty(pureText)) {
      if (pureDirection == PURE_DIRECTION_LEFT) {
        pureText = pureText + spacingSb;
      } else {
        pureText = spacingSb + pureText;
      }
    }
    pureTextPaint = new Paint();
    pureTextPaint.setAntiAlias(true);
    pureTextPaint.setColor(pureTextColor);
    pureTextPaint.setStyle(Paint.Style.FILL);
    pureTextPaint.setTextSize(pureTextSize);
    pureTextPaint.setTextAlign(Paint.Align.CENTER);
    lineDrawablePaint = new Paint();
    lineDrawablePaint.setAntiAlias(true);
    lineDrawablePaint.setColor(dividerColor);
    lineDrawablePaint.setStyle(Paint.Style.FILL);
    lineDrawablePaint.setStrokeWidth(LINE_HEIGHT);
    mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mBitPaint.setFilterBitmap(true);
    mBitPaint.setDither(true);
    originalPaddingLeft = getPaddingLeft();
    originalPaddingRight = getPaddingRight();
    drawablePadding = getCompoundDrawablePadding();
    if (getCompoundDrawables() != null) {
      if (getCompoundDrawables().length > 0
          && getCompoundDrawables()[0] != null) {
        drawableLeft = getCompoundDrawables()[0];
        drawableLeftWidth = drawableLeft.getIntrinsicWidth();
      }
      if (getCompoundDrawables().length > 2
          && getCompoundDrawables()[2] != null) {
        drawableRight = getCompoundDrawables()[2];
        drawableRightWidth = drawableRight.getIntrinsicWidth();
      }
      setCompoundDrawables(null, null, null, null);
    }
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (pureDirection == PURE_DIRECTION_RIGHT) {
      calculationTextXByPureRight();
    } else if (pureDirection == PURE_DIRECTION_LEFT) {
      calculationTextXByPureLeft();
    }
  }

  private void calculationTextXByPureLeft() {
    Float textLength = 0f;
    textX = (drawableLeftWidth == 0) ? 0 : (drawablePadding + drawableLeftWidth);
    if (!TextUtils.isEmpty(pureText)) {
      textLength = pureTextPaint.measureText(pureText);
      textX += textLength.intValue() / 2 + originalPaddingLeft;
    }else{
      textX +=originalPaddingLeft;
    }
    if (frist) {
      frist = false;
      setPadding(textX + textLength.intValue() / 2, getPaddingTop(),
          originalPaddingRight + ((drawableRightWidth == 0) ? 0
              : (drawablePadding + drawableRightWidth)), getPaddingBottom());
    }
  }

  private void calculationTextXByPureRight() {
    int paddingRight = originalPaddingRight * 2;
    if (!TextUtils.isEmpty(pureText)) {
      Float textLength = pureTextPaint.measureText(pureText);
      paddingRight += textLength.intValue();
      textX =
          getWidth() - textLength.intValue() / 2 - originalPaddingRight
              - drawablePadding - drawableRightWidth;
      paddingRight += drawableRightWidth;
    }
    if (frist) {
      frist = false;
      setPadding(originalPaddingLeft + ((drawableLeftWidth == 0) ? 0
              : (drawablePadding + drawableLeftWidth)), getPaddingTop(),
          paddingRight, getPaddingBottom());
    }
  }


  @Override
  protected void onDraw(Canvas canvas) {
    if (viewDivide != VIEWDIVIDE_NONE) {
      int lineX = 0;
      if (viewDivide == VIEWDIVIDE_BOTHALL || viewDivide == VIEWDIVIDE_BOTHPARTBOTTOM
          || viewDivide == VIEWDIVIDE_ALLTOP) {
        canvas.drawLine(lineX, 0, getWidth(), 0, lineDrawablePaint);
      }
      if (viewDivide == VIEWDIVIDE_PARTBOTTOM || viewDivide == VIEWDIVIDE_BOTHPARTBOTTOM
          || viewDivide == VIEWDIVIDE_PARTTOP || viewDivide == VIEWDIVIDE_BOTHPART) {
        lineX += originalPaddingLeft;
      }
      if (viewDivide != VIEWDIVIDE_ALLTOP && viewDivide != VIEWDIVIDE_PARTTOP) {
        canvas.drawLine(lineX, getHeight() - LINE_HEIGHT, getWidth(), getHeight() - LINE_HEIGHT,
            lineDrawablePaint);
      } else if (viewDivide == VIEWDIVIDE_PARTTOP) {
        canvas.drawLine(lineX, 0, getWidth(), 0, lineDrawablePaint);
      }
      if (viewDivide == VIEWDIVIDE_BOTHPART) {
        canvas.drawLine(lineX, 0, getWidth(), 0, lineDrawablePaint);
      } else if (viewDivide == VIEWDIVIDE_BOTHPARTTOP) {
        canvas.drawLine(originalPaddingLeft, 0, getWidth(), 0, lineDrawablePaint);
      }
    }
    if (!TextUtils.isEmpty(pureText)) {
      //绘文字
      Paint.FontMetrics fm = pureTextPaint.getFontMetrics();
      int textY = (int) (getHeight() - fm.bottom - fm.top) / 2;
      canvas.drawText(pureText, textX, textY, pureTextPaint);
    }
    if (drawableLeft != null) {
      int drawableTop = (getHeight() - drawableLeft.getIntrinsicHeight()) / 2;
      drawableLeft.setBounds(originalPaddingLeft, drawableTop,
          originalPaddingLeft + drawableLeft.getIntrinsicWidth(),
          drawableTop + drawableLeft.getIntrinsicHeight());
      drawableLeft.draw(canvas);
    }
    if (drawableRight != null) {
      int drawableTop = (getHeight() - drawableRight.getIntrinsicHeight()) / 2;
      drawableRight.setBounds(getWidth() - originalPaddingRight - drawableRight.getIntrinsicWidth(),
          drawableTop,
          getWidth() - originalPaddingRight,
          drawableTop + drawableRight.getIntrinsicHeight());
      drawableRight.draw(canvas);
    }
    super.onDraw(canvas);
  }

  public void setPureText(String txt) {
    this.pureText = txt;
    isHint = false;
    invalidate();
  }

  public void setPureTextColor(@ColorRes int color) {
    this.pureTextColor = getResources().getColor(color);
    isHint = false;
    invalidate();
  }

  public void setPureText(@StringRes int tex) {
    this.pureText = getResources().getString(tex);
    isHint = false;
    invalidate();
  }

  @Override
  public void invalidate() {
    if (pureDirection == PURE_DIRECTION_LEFT) {
      calculationTextXByPureLeft();
    } else {
      calculationTextXByPureRight();
    }
    super.invalidate();
  }

  public String getPureText() {
    if (isHint) {
      return "";
    }
    return pureText;
  }

  public final static int VIEWDIVIDE_NONE = -1;
  public final static int VIEWDIVIDE_ALLBOTTOM = 0;
  public final static int VIEWDIVIDE_PARTBOTTOM = 1;
  public final static int VIEWDIVIDE_BOTHALL = 2;
  public final static int VIEWDIVIDE_BOTHPARTBOTTOM = 3;
  public final static int VIEWDIVIDE_ALLTOP = 4;
  public final static int VIEWDIVIDE_PARTTOP = 5;
  public final static int VIEWDIVIDE_BOTHPART = 6;
  public final static int VIEWDIVIDE_BOTHPARTTOP = 7;

  public void setViewDivide(@ViewDivide int viewDivide) {
    this.viewDivide = viewDivide;
  }

  @IntDef({VIEWDIVIDE_NONE, VIEWDIVIDE_ALLBOTTOM, VIEWDIVIDE_PARTBOTTOM, VIEWDIVIDE_BOTHALL,
      VIEWDIVIDE_BOTHPARTBOTTOM, VIEWDIVIDE_ALLTOP, VIEWDIVIDE_PARTTOP, VIEWDIVIDE_BOTHPART,
      VIEWDIVIDE_BOTHPARTTOP})
  @Retention(RetentionPolicy.SOURCE)
  public @interface ViewDivide {

  }
}
