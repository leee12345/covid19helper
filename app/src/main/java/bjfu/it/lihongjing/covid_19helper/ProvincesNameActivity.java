package bjfu.it.lihongjing.covid_19helper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bjfu.it.lihongjing.covid_19helper.bean.state.ProvinceInfo;
import bjfu.it.lihongjing.covid_19helper.fragment.MyFragment;
import bjfu.it.lihongjing.covid_19helper.fragment.TopLevelFragment;
import bjfu.it.lihongjing.covid_19helper.utils.LeakCanaryUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ProvincesNameActivity extends AppCompatActivity{
    public static final String PROVINCE_NAME_KEY = "province_name";
    public static final int PROVINCE_NAME_RESULT_CODE = 1;
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";
    private OkHttpClient client;
    private Request request;
    private List<String> provinceNamesList=null;
    private ListView listView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinces);
        listView = findViewById(R.id.wd_list);
        Intent intent = getIntent();
        String name = intent.getStringExtra(TopLevelFragment.NAME_KEY);
        String tel=intent.getStringExtra(TopLevelFragment.TEL_KEY);
        initView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String provinceName = provinceNamesList.get(i);
                Intent intent = new Intent(ProvincesNameActivity.this, StateActivity.class);
                intent.putExtra(PROVINCE_NAME_KEY, provinceName);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                Toast.makeText(ProvincesNameActivity.this,provinceName, Toast.LENGTH_SHORT).show();
                startActivityForResult(intent,PROVINCE_NAME_RESULT_CODE);
                finish();
            }
        });
        LeakCanaryUtils.addContext(ProvincesNameActivity.this);
        back=findViewById(R.id.back_wd);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProvincesNameActivity.this, MainActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivityForResult(intent, PROVINCE_NAME_RESULT_CODE);
            }
        });
    }
    private void initView() {
        provinceNamesList = new ArrayList<>();
        String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        request = new Request.Builder().addHeader("User-Agent", "").url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("onFailure", "请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.body()!=null &&response.isSuccessful()){
                    String responseString = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            String jsonString = null;
                            Matcher matcher;
                            Type type = new TypeToken<List<ProvinceInfo>>() {
                            }.getType();
                            matcher = Pattern.compile("(?<=Stat = ).*?(?=\\}catch)").matcher(responseString);
                            if (matcher.find()) {
                                jsonString = matcher.group();
                            }
                            List<ProvinceInfo> provinceNames = gson.fromJson(jsonString, type);
                            for(ProvinceInfo pname:provinceNames){
                                provinceNamesList.add(pname.getProvinceName().toString());
                            }
                            Collections.sort(provinceNamesList, new Comparator<String>() {
                                @Override
                                public int compare(String s, String t1) {
                                    return Collator.getInstance(Locale.CHINESE).compare(s, t1);
                                }
                            });
                            listView.setAdapter(new ArrayAdapter<>(ProvincesNameActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    provinceNamesList));
                        }
                    });
                }
            }
        });
    }


}
