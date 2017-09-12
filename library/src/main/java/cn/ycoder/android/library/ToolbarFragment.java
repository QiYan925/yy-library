package cn.ycoder.android.library;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.ycoder.android.library.tool.ActivitiesManager;
import cn.ycoder.android.library.tool.LogUtils;

/**
 * @author 启研
 * @created at 2017/3/13 0:03
 */

public abstract class ToolbarFragment extends BaseFragment {

  @Override
  protected void bindView(View view) {
    super.bindView(view);
  }

  protected Toolbar toolbar;

  public void bindToolbarView(View view, int titleRes) {
    String title = null;
    if (getArguments() != null) {
      title = getArguments().getString("title");
    }
    if (titleRes != -1) {
      title = getString(titleRes);
    }
    this.bindToolbarView(view, title);
  }

  public void bindToolbarView(View view, String titleRes) {
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    if (toolbar != null) {
      if (titleRes != null) {
        toolbar.setTitle(titleRes);
      }
      if (!usedInflateMenu()) {
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
      }
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onBackPressed();
        }
      });
      if (!canBack()) {
        toolbar.setNavigationIcon(null);
      }
      ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(canBack());
        actionBar.setDisplayShowHomeEnabled(canBack());
      }
      setHasOptionsMenu(true);
    } else {
      throw new NullPointerException("if not toolbar,please used BaseFragment");
    }
    super.bindView(view);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (toolbar == null) {
      LogUtils.e("please call (super.bindToolbarView) method at onCreateView!");
    }
  }

  /**
   * 采用InflateMenu初始化菜单按钮
   */
  public boolean usedInflateMenu() {
    return false;
  }

  public boolean canBack() {
    return true;
  }

  @Override
  public boolean onBackPressed() {
    if (canBack()) {
      ActivitiesManager.getInstance().finishActivity(getActivity());
      return true;
    }
    return super.onBackPressed();
  }
}
