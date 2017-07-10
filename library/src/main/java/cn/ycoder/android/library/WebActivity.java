package cn.ycoder.android.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.HashMap;
import java.util.Map;

import cn.ycoder.android.library.route.RouteUtil;
import cn.ycoder.android.library.tool.ClipboardUtils;
import cn.ycoder.android.library.tool.ToastUtils;
import cn.ycoder.android.library.widget.NumberProgressBar;
import cn.ycoder.android.library.widget.ObservableWebView;


/**
 * @author 启研
 * @created at 2017/5/3 14:28
 */
@Route(path = RouteUtil.URI_APP_WEB_BROWSER)
public class WebActivity extends ToolbarActivity implements
    ObservableWebView.OnScrollChangedListener {

  private static final String TAG = WebActivity.class.getSimpleName();

  protected static final String EXTRA_URL = "extra_url";
  protected static final String EXTRA_TITLE = "extra_title";

  protected static final Map<String, Integer> URL_POSITION_CACHES = new HashMap<>();

  NumberProgressBar progressbar;
  ObservableWebView webView;
  TextSwitcher textSwitcher;

  String url, title;
  int positionHolder;
  boolean overrideTitleEnabled = true;


  /**
   * Using newIntent trick, return WebActivity Intent, to avoid `public static`
   * constant
   * variable everywhere
   *
   * @return Intent to start WebActivity
   */
  public static Intent newIntent(Context context, String extraTitle, String extraURL) {
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtra(EXTRA_TITLE, extraTitle);
    intent.putExtra(EXTRA_URL, extraURL);
    return intent;
  }


  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    overridePendingTransition(0, 0);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.yy_library_act_web);
    progressbar = (NumberProgressBar) findViewById(R.id.progressbar);
    webView = (ObservableWebView) findViewById(R.id.web_view);
    textSwitcher = (TextSwitcher) findViewById(R.id.title);

    url = getIntent().getStringExtra(EXTRA_URL);
    title = getIntent().getStringExtra(EXTRA_TITLE);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setLoadWithOverviewMode(true);
    settings.setAppCacheEnabled(true);
    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    settings.setSupportZoom(true);
    settings.setDomStorageEnabled(true);
    webView.setWebChromeClient(new ChromeClient());
    webView.setWebViewClient(new ReloadableClient());
    webView.setOnScrollChangedListener(this);
    // webView.addJavascriptInterface(new JSInterface(), "JSInterface");

    webView.loadUrl(url);

    textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
      @SuppressWarnings("deprecation")
      @Override
      public View makeView() {
        final Context context = WebActivity.this;
        final TextView textView = new TextView(context);
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setTextAppearance(context, R.style.WebTitle);
        textView.setSingleLine(true);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        TypedValue value = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.toolbarTitleSize, value, true)) {
          int titleSize = TypedValue
              .complexToDimensionPixelSize(value.data, context.getResources().getDisplayMetrics());
          textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        }
        if (context.getTheme().resolveAttribute(R.attr.titleTextColor, value, true)) {
          textView.setTextColor(ContextCompat.getColor(getBaseContext(), value.resourceId));
        }
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(final View v) {
            v.setSelected(!v.isSelected());
          }
        });
        return textView;
      }
    });
    textSwitcher.setInAnimation(this, android.R.anim.fade_in);
    textSwitcher.setOutAnimation(this, android.R.anim.fade_out);
    if (title != null) {
      setTitle(title);
    }
  }


  public void setOverrideTitleEnabled(boolean enabled) {
    this.overrideTitleEnabled = enabled;
  }


  @Override
  public void setTitle(CharSequence title) {
    super.setTitle(title);
    textSwitcher.setText(title);
  }


  private void refresh() {
    webView.reload();
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN) {
      switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
          if (webView.canGoBack()) {
            webView.goBack();
          } else {
            finish();
          }
          return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_web, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_refresh) {
      refresh();
      return true;
    } else if (id == R.id.action_copy_url) {
      ClipboardUtils.copyText(webView.getUrl());
      ToastUtils.showShort(R.string.tip_web_copy_done);
      return true;
    } else if (id == R.id.action_open_url) {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(intent);
      } else {
        ToastUtils.showLong(R.string.tip_web_open_fail);
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  @SuppressWarnings("deprecation")
  @Override
  protected void onDestroy() {
    final String url = webView.getUrl();
    int bottom = (int) Math.floor(webView.getContentHeight() * webView.getScale() * 0.8f);
    if (positionHolder >= bottom) {
      URL_POSITION_CACHES.remove(url);
    } else {
      URL_POSITION_CACHES.put(url, positionHolder);
    }
    super.onDestroy();
    if (webView != null) {
      webView.destroy();
    }
  }


  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }


  @Override
  public void onScrollChanged(WebView v, int x, int y, int oldX, int oldY) {
    positionHolder = y;
  }


  private class ChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      super.onProgressChanged(view, newProgress);
      progressbar.setProgress(newProgress);
      if (newProgress == 100) {
        progressbar.setVisibility(View.INVISIBLE);
      } else {
        progressbar.setVisibility(View.VISIBLE);
      }
    }


    @Override
    public void onReceivedTitle(WebView view, String title) {
      super.onReceivedTitle(view, title);
      if (overrideTitleEnabled) {
        setTitle(title);
      }
    }
  }


  private class ReloadableClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if (url != null) {
        view.loadUrl(url);
      }
      return true;
    }


    @Override
    public void onPageCommitVisible(WebView view, String url) {
      super.onPageCommitVisible(view, url);
      Integer _position = URL_POSITION_CACHES.get(url);
      int position = _position == null ? 0 : _position;
      view.scrollTo(0, position);
    }
  }

  @Override
  public boolean canBack() {
    return true;
  }
}
