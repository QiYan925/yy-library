package cn.ycoder.android.library.tool;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.ycoder.android.library.R;

/**
 * @author 启研
 * @created at 2017/5/19 15:27
 */

public class SimplePopupWindow extends PopupWindow {
    /**
     * 防止误操作时间
     */
    public final static int MISOPERATION_TIME = 200;
    long lastPopDismissTime;

    public SimplePopupWindow(final Builder builder) {
        super(builder.view.getContext());
        Context context = builder.view.getContext();
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(R.styleable.AppCompatTheme);
        int barHeight = typedArray.getDimensionPixelOffset(R.styleable.AppCompatTheme_actionBarSize, ScreenInfo.dip2px(40));
        typedArray.recycle();
        LinearLayout frameLayout = new LinearLayout(context);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ScreenInfo.getInstance(context).screenHeight - barHeight));
        frameLayout.setBackgroundColor(Color.parseColor("#B3000000"));
        frameLayout.setGravity(builder.gravity);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        frameLayout.addView(builder.view);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                lastPopDismissTime = System.currentTimeMillis();
                if (builder.dismissListener != null)
                    builder.dismissListener.onDismiss();
            }
        });
        setContentView(frameLayout);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (System.currentTimeMillis() - lastPopDismissTime > 300)
            super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (System.currentTimeMillis() - lastPopDismissTime > MISOPERATION_TIME)
            super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (System.currentTimeMillis() - lastPopDismissTime > MISOPERATION_TIME)
            super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (System.currentTimeMillis() - lastPopDismissTime > MISOPERATION_TIME)
            super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public static class Builder {
        View view;
        int gravity = Gravity.BOTTOM;
        OnSimpleDismissListener dismissListener;

        public Builder(View contentView) {
            view = contentView;
        }

        public Builder resize(int width, int height) {
            if (view.getLayoutParams() == null)
                view.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            else {
                view.getLayoutParams().height = height;
                view.getLayoutParams().width = width;
            }
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder onDismiss(OnSimpleDismissListener onDismissListener) {
            dismissListener = onDismissListener;
            return this;
        }

        public SimplePopupWindow build() {
            return new SimplePopupWindow(this);
        }
    }

    public interface OnSimpleDismissListener {
        void onDismiss();
    }
}
