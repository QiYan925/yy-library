package cn.ycoder.android.library;

import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

/**
 * @author 启研
 * @created at 2017/3/9.
 */
public class ToolbarActivity extends BaseActivity {

  public Toolbar toolbar;

  public boolean canBack() {
    return true;
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    initToolbar();
  }

  /**
   * 初始化toolbar
   */
  protected void initToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onBackPressed();
        }
      });
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayShowHomeEnabled(canBack());
        actionBar.setDisplayHomeAsUpEnabled(canBack());
      }
      if (!TextUtils.isEmpty(getTitle())) {
        toolbar.setTitle(getTitle());
      }
    } else {
      throw new NullPointerException("if not toolbar,please used BaseActivity");
    }
  }

  public void setDisplayShowTitleEnabled(boolean enabled) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowTitleEnabled(enabled);
    }
  }
}
