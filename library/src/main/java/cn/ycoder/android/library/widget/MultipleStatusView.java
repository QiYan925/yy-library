package cn.ycoder.android.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ycoder.android.library.R;


/**
 * 类描述：  一个方便在多种状态切换的view
 * <p>
 * 创建人:   续写经典
 * 创建时间: 2016/1/15 10:20.
 */
public class MultipleStatusView extends RelativeLayout {

  public static final int STATUS_CONTENT = 0x00;
  public static final int STATUS_LOADING = 0x01;
  public static final int STATUS_EMPTY = 0x02;
  public static final int STATUS_ERROR = 0x03;
  public static final int STATUS_NO_NETWORK = 0x04;

  private static final int NULL_RESOURCE_ID = -1;

  private View mEmptyView;
  private View mErrorView;
  private View mLoadingView;
  private View mNoNetworkView;
  private View mContentView;
  private View mEmptyRetryView;
  private View mErrorRetryView;
  private TextView errorTextView;
  private View mNoNetworkRetryView;
  private int mEmptyViewResId;
  private int mErrorViewResId;
  private int mLoadingViewResId;
  private int mNoNetworkViewResId;
  private int mContentViewResId;
  private int mViewStatus;

  private LayoutInflater mInflater;
  private OnClickListener mOnRetryClickListener;
  private final ViewGroup.LayoutParams mLayoutParams =
      new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);

  public MultipleStatusView(Context context) {
    this(context, null);
  }

  public MultipleStatusView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    final TypedArray a = context
        .obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0);

    mEmptyViewResId = a
        .getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.multi_status_view_empty);
    mErrorViewResId = a
        .getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.multi_status_view_error);
    mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView,
        R.layout.multi_status_view_loading);
    mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView,
        R.layout.multi_status_view_no_network);
    mContentViewResId = a
        .getResourceId(R.styleable.MultipleStatusView_contentView, NULL_RESOURCE_ID);
    a.recycle();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mInflater = LayoutInflater.from(getContext());
    showContent();
  }

  /**
   * 获取当前状态
   */
  @SuppressWarnings("unused")
  public int getViewStatus() {
    return mViewStatus;
  }

  /**
   * 设置重试点击事件
   *
   * @param onRetryClickListener 重试点击事件
   */
  public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
    this.mOnRetryClickListener = onRetryClickListener;
  }

  /**
   * 显示空视图
   */
  public final void showEmpty() {
    mViewStatus = STATUS_EMPTY;
    if (null == mEmptyView) {
      mEmptyView = mInflater.inflate(mEmptyViewResId, null);
      mEmptyRetryView = mEmptyView.findViewById(R.id.emptyRetryView);
      if (null != mOnRetryClickListener && null != mEmptyRetryView) {
        mEmptyRetryView.setOnClickListener(mOnRetryClickListener);
      }
      addView(mEmptyView, 0, mLayoutParams);
    }
    showViewByStatus(mViewStatus);
  }

  /**
   * 显示错误视图
   */
  public final void showError(String msg) {
    mViewStatus = STATUS_ERROR;
    if (null == mErrorView) {
      mErrorView = mInflater.inflate(mErrorViewResId, null);
      mErrorRetryView = mErrorView.findViewById(R.id.errorRetryView);
      errorTextView = (TextView) mErrorView.findViewById(R.id.errorTextView);
      if (null != mOnRetryClickListener && null != mErrorRetryView) {
        mErrorRetryView.setOnClickListener(mOnRetryClickListener);
      }
      addView(mErrorView, 0, mLayoutParams);
    }
    if (null != errorTextView) {
      errorTextView.setText(msg);
    }
    showViewByStatus(mViewStatus);
  }

  /**
   * 显示加载中视图
   */
  public final void showLoading() {
    mViewStatus = STATUS_LOADING;
    if (null == mLoadingView) {
      mLoadingView = mInflater.inflate(mLoadingViewResId, null);
      addView(mLoadingView, 0, mLayoutParams);
    }
    showViewByStatus(mViewStatus);
  }

  /**
   * 显示无网络视图
   */
  public final void showNoNetwork() {
    mViewStatus = STATUS_NO_NETWORK;
    if (null == mNoNetworkView) {
      mNoNetworkView = mInflater.inflate(mNoNetworkViewResId, null);
      mNoNetworkRetryView = mNoNetworkView.findViewById(R.id.noNetworkRetryView);
      if (null != mOnRetryClickListener && null != mNoNetworkRetryView) {
        mNoNetworkRetryView.setOnClickListener(mOnRetryClickListener);
      }
      addView(mNoNetworkView, 0, mLayoutParams);
    }
    showViewByStatus(mViewStatus);
  }

  /**
   * 显示内容视图
   */
  public final void showContent() {
    mViewStatus = STATUS_CONTENT;
    if (null == mContentView) {
      if (mContentViewResId != NULL_RESOURCE_ID) {
        mContentView = mInflater.inflate(mContentViewResId, null);
        addView(mContentView, 0, mLayoutParams);
      } else {
        mContentView = findViewById(R.id.contentView);
      }
    }
    showViewByStatus(mViewStatus);
  }

  private void showViewByStatus(int viewStatus) {
    if (null != mLoadingView) {
      mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
    }
    if (null != mEmptyView) {
      mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
    }
    if (null != mErrorView) {
      mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
    }
    if (null != mNoNetworkView) {
      mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
    }
    if (null != mContentView) {
      mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
    }
  }

}
