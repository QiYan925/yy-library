package cn.com.epsoft.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
  int textX;
  int originalPaddingLeft,originalPaddingRight;

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
    ta.recycle();
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
  }

  boolean frist = true;

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    originalPaddingLeft = getPaddingLeft();
    originalPaddingRight=getPaddingRight();
    if (pureDirection == PURE_DIRECTION_LEFT) {
      calculationTextXByPureLeft();
    }
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (pureDirection == PURE_DIRECTION_RIGHT) {
      calculationTextXByPureRight();
    }
  }

  private void calculationTextXByPureLeft() {
    if (!TextUtils.isEmpty(pureText)) {
      Float textLength = pureTextPaint.measureText(pureText);
      textX = textLength.intValue() / 2 + originalPaddingRight + getCompoundDrawablePadding();
//      if (getCompoundDrawables() != null && getCompoundDrawables().length > 0
//          && getCompoundDrawables()[0] != null) {
//        textX -= getCompoundDrawables()[0].getIntrinsicWidth();
//      }
      if (frist) {
        frist = false;
        setPadding(getPaddingLeft() + textX + textLength.intValue() / 2, getPaddingTop(),
            originalPaddingRight, getPaddingBottom());
      }
    }
  }

  private void calculationTextXByPureRight() {
    if (!TextUtils.isEmpty(pureText)) {
      Float textLength = pureTextPaint.measureText(pureText);
      int paddingRight = originalPaddingRight * 2 + textLength.intValue();
      textX =
          getWidth() - textLength.intValue() / 2 - originalPaddingRight - getCompoundDrawablePadding();
      if (getCompoundDrawables() != null && getCompoundDrawables().length > 2
          && getCompoundDrawables()[2] != null) {
        textX -= getCompoundDrawables()[2].getIntrinsicWidth();
        paddingRight += getCompoundDrawables()[2].getIntrinsicWidth();
      }
      if (frist) {
        frist = false;
        setPadding(getPaddingLeft(), getPaddingTop(),
            paddingRight, getPaddingBottom());
      }
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
          || viewDivide == VIEWDIVIDE_PARTTOP) {
        lineX += originalPaddingLeft;
//        if (getCompoundDrawables() != null && getCompoundDrawables()[0] != null) {
//          lineX += getCompoundDrawablePadding();
//          lineX += getCompoundDrawables()[0].getIntrinsicWidth();
//        }
      }
      if (viewDivide != VIEWDIVIDE_ALLTOP && viewDivide != VIEWDIVIDE_PARTTOP) {
        canvas.drawLine(lineX, getHeight() - LINE_HEIGHT, getWidth(), getHeight() - LINE_HEIGHT,
            lineDrawablePaint);
      } else if (viewDivide == VIEWDIVIDE_PARTTOP) {
        canvas.drawLine(lineX, 0, getWidth(), 0, lineDrawablePaint);
      }
    }
    if (!TextUtils.isEmpty(pureText)) {
      //绘文字
      Paint.FontMetrics fm = pureTextPaint.getFontMetrics();
      int textY = (int) (getHeight() - fm.bottom - fm.top) / 2;
      canvas.drawText(pureText, textX, textY, pureTextPaint);
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

  public void setViewDivide(@ViewDivide int viewDivide) {
    this.viewDivide = viewDivide;
  }

  @IntDef({VIEWDIVIDE_NONE, VIEWDIVIDE_ALLBOTTOM, VIEWDIVIDE_PARTBOTTOM, VIEWDIVIDE_BOTHALL,
      VIEWDIVIDE_BOTHPARTBOTTOM, VIEWDIVIDE_ALLTOP, VIEWDIVIDE_PARTTOP})
  @Retention(RetentionPolicy.SOURCE)
  public @interface ViewDivide {

  }
}
