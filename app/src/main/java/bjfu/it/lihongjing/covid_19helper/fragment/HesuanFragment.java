package bjfu.it.lihongjing.covid_19helper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import bjfu.it.lihongjing.covid_19helper.MainActivity;
import bjfu.it.lihongjing.covid_19helper.R;

public class HesuanFragment extends Fragment {
    String tags = "";
    private WebView heSuanView;
    private ImageView back;
    public static HesuanFragment newInstance() {
        Bundle args = new Bundle();
        HesuanFragment fragment = new HesuanFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_hesuan, container, false);
        back=v.findViewById(R.id.back_hsyy);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        heSuanView = (WebView)v.findViewById(R.id.hsyy_view);
        heSuanView.getSettings().setJavaScriptEnabled(true);
        //跳转
        WebSettings webSettings=heSuanView.getSettings();
        webSettings.setDomStorageEnabled(true);

        heSuanView.setWebViewClient(new WebViewClient());
        String uri ="http://m.bj.bendibao.com/news/hesuanjiance/";
        heSuanView.loadUrl(uri);
        return v;
    }

}
