package bjfu.it.lihongjing.covid_19helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import bjfu.it.lihongjing.covid_19helper.bean.news.News;

public class ContentActivity extends AppCompatActivity {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    private WebView webView;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);

        Intent intent = getIntent();
        String name = intent.getStringExtra(NewsActivity.NAME_KEY);
        String tel=intent.getStringExtra(NewsActivity.TEL_KEY);

        back=findViewById(R.id.back_content);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, NewsActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivity(intent);
            }
        });
        webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String uri = getIntent().getStringExtra("uri");
        webView.loadUrl(uri);

    }


}