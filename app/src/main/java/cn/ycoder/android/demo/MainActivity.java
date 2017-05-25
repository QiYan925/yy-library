package cn.ycoder.android.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.ycoder.android.demo.presenter.NotePresenter;
import cn.ycoder.android.library.ToolbarActivity;
import cn.ycoder.android.library.WebActivity;
import cn.ycoder.android.library.route.RouteUtil;
import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivity extends ToolbarActivity implements NotePresenter.View {

  private Button btn;
  private TextView txt;
  private NotePresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_main);
    //初始化及绑定
    this.presenter = new NotePresenter(this);
    this.txt = (TextView) findViewById(R.id.txt);
    this.btn = (Button) findViewById(R.id.btn);
    this.btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        presenter.load();
      }
    });
    findViewById(R.id.baidu).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(WebActivity.newIntent(getBaseContext(),"","http://www.baidu.com"));
      }
    });
  }

  @Override
  public void onLoadResult(final String msg) {
    txt.setText(msg);
    this.txt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ARouter.getInstance().build(RouteUtil.builderWithFragment("/main/test"))
            .withString("msg", msg)
            .navigation(MainActivity.this);
      }
    });
  }
}