package bjfu.it.lihongjing.covid_19helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import bjfu.it.lihongjing.covid_19helper.fragment.MyFragment;

public class MyTripActivity extends AppCompatActivity {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    private WebView tripView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrip);

        Intent intent = getIntent();
        String name = intent.getStringExtra(MyFragment.NAME_KEY);
        String tel=intent.getStringExtra(MyFragment.TEL_KEY);

        back=findViewById(R.id.back_trip);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTripActivity.this, MainActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivity(intent);
            }
        });
        tripView = (WebView)findViewById(R.id.trip_view);
        tripView.getSettings().setJavaScriptEnabled(true);
        //跳转
        WebSettings webSettings=tripView.getSettings();
        webSettings.setDomStorageEnabled(true);

        tripView.setWebViewClient(new WebViewClient());
        String uri ="https://xc.caict.ac.cn/#/login";
        tripView.loadUrl(uri);

    }

}