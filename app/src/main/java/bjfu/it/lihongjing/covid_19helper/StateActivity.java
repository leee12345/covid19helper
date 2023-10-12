package bjfu.it.lihongjing.covid_19helper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerData;
import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerLevelInfo;
import bjfu.it.lihongjing.covid_19helper.bean.dangerLevel.DangerPro;
import bjfu.it.lihongjing.covid_19helper.bean.state.NewAdd;
import bjfu.it.lihongjing.covid_19helper.bean.state.StateInfo;
import bjfu.it.lihongjing.covid_19helper.fragment.TopLevelFragment;
import bjfu.it.lihongjing.covid_19helper.network.DangerApiClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class StateActivity extends AppCompatActivity {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    private TextView today_add;
    private TextView total_add;
    private TextView title;
    private ImageView back;
    private OkHttpClient client;
    private Request request;
    private String provinceName;
    private String DEFAULT_PROVINCE_NAME="北京市";
    private String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
    private RadioGroup radioGroup;
    private RadioButton highRb;
    private RadioButton midRb;
    private RadioButton lowRb;
    private ImageView imgView;
    private List<DangerData> dangerDataList = new ArrayList<>();
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        today_add = findViewById(R.id.today_confirm);
        total_add = findViewById(R.id.total_confirm);
        title = findViewById(R.id.province);
        radioGroup = findViewById(R.id.radioGroup);
        highRb = findViewById(R.id.highRiskBtn);
        midRb = findViewById(R.id.midRiskBtn);
        lowRb = findViewById(R.id.lowRiskBtn);
        imgView=findViewById(R.id.province_image);
        back=findViewById(R.id.back_state);
        Intent intent = getIntent();
        String name = intent.getStringExtra(ProvincesNameActivity.NAME_KEY);
        String tel=intent.getStringExtra(ProvincesNameActivity.TEL_KEY);
        String provincename = intent.getStringExtra(ProvincesNameActivity.PROVINCE_NAME_KEY);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StateActivity.this, ProvincesNameActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivityForResult(intent, ProvincesNameActivity.PROVINCE_NAME_RESULT_CODE);
            }
        });
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        request = new Request.Builder().addHeader("User-Agent", "").url(url).build();


        if(!DEFAULT_PROVINCE_NAME.equals(provincename))
            refresh(provincename);
        else
            refresh(DEFAULT_PROVINCE_NAME);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ProvincesNameActivity.PROVINCE_NAME_RESULT_CODE){
            if(data!=null){
                String provinceName=data.getStringExtra(ProvincesNameActivity.PROVINCE_NAME_KEY);
                refresh(provinceName);
            }
        }
    }

    private void refresh(String provinceName){
        if (TextUtils.isEmpty(provinceName) || provinceName.equals(this.provinceName)) {
            return;
        }
        title.setText(provinceName);
        Resources res=getResources();
        if(provinceName.equals("北京市"))
        {
            imgView.setBackground(res.getDrawable(R.drawable.beijing));
        }
        else if(provinceName.equals("江苏省")){
            imgView.setBackground(res.getDrawable(R.drawable.jiangsu));
        }
        else{
            imgView.setBackground(res.getDrawable(R.drawable.shanghai));
        }

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(StateActivity.this,"请求State接口失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        String jsonString = null;
                        Matcher matcher;
                        Type type = new TypeToken<List<StateInfo>>() {}.getType();
                        matcher = Pattern.compile("(?<=Stat = ).*?(?=\\}catch)").matcher(responseString);
                        if (matcher.find()) {
                            jsonString = matcher.group();
                        }
                        List<StateInfo> stateInfos = gson.fromJson(jsonString, type);
                        for(StateInfo stateInfo:stateInfos){
                            if(stateInfo.getProvinceName().equals(provinceName)){
                                total_add.setText(getString(R.string.confirm_total,stateInfo.getTotal_add()));
                                break;
                            }

                        }
                    }
                });

            }
        });
        //疫情昨日统计
        Call callTotal = client.newCall(request);
        callTotal.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(StateActivity.this,"请求State接口失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        String jsonString = null;
                        //yestoday_add
                        Matcher matcher = Pattern.compile("(?<=fetchRecentStatV2 = ).*?(?=\\}catch)").matcher(responseString);
                        if (matcher.find()) {
                            jsonString = matcher.group();
                        }
                        Type type2 = new TypeToken<List<NewAdd>>(){}.getType();
                        List<NewAdd> new_adds = gson.fromJson(jsonString, type2);
                        boolean flag = false;
                        for(NewAdd na:new_adds){
                            if(na.getProvinceName().equals(provinceName)){
                                today_add.setText(getString(R.string.confirm_today,na.getNew_add()));
                                flag =true;
                                break;
                            }
                        }
                        if(!flag)
                            today_add.setText(getString(R.string.confirm_today, "0"));

                    }
                });

            }
        });
        //风险等级
        retrofit2.Call<DangerLevelInfo> callLevel = DangerApiClient.getInstance().getApi().getDangerLevelInfos();
        callLevel.enqueue(new retrofit2.Callback<DangerLevelInfo>() {
            @Override
            public void onResponse(retrofit2.Call<DangerLevelInfo> call, retrofit2.Response<DangerLevelInfo> response) {
                dangerDataList = response.body().getDangerDataList();
                for(DangerData dangerData:dangerDataList){
                    Integer dangerLevel = dangerData.getDangerLevel();
                    for(DangerPro dangerPro: dangerData.getDangerProList()){
                        if(provinceName.equals(dangerPro.getProvinceName())){
                            //TODO 风险等级单选框
                            radioGroup.clearCheck();
                            if(dangerLevel==1){
                                highRb.setChecked(true);
                            }else if(dangerLevel==2){
                                midRb.setChecked(true);
                            }else{
                                lowRb.setChecked(true);
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DangerLevelInfo> call, Throwable t) {
                Toast.makeText(StateActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}