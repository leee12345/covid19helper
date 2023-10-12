package bjfu.it.lihongjing.covid_19helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SegregationActivity extends AppCompatActivity {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    private Spinner start_spinner;
    private Spinner end_spinner;
    private Button transfer;
    private ImageView imgV;
    private ListView listView;
    private EditText start_et;
    private EditText end_et;
    private String cityJson = null;
    private final String API_KEY = "jY5mPdutfO9f9bEaLndkl4E80YpG05Cw";
    //location

    private Button search_btn;
    private OkHttpClient client;
    private final String baseurl = ".bendibao.com/news/gelizhengce/all.php?";
    private TextView start_content;
    private TextView end_content;
    private TextView tv_s;
    private TextView tv_e;
    private LinearLayout seg_content;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segregation);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ProvincesNameActivity.NAME_KEY);
        String tel=intent.getStringExtra(ProvincesNameActivity.TEL_KEY);

        initView();
        back=findViewById(R.id.back_seg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SegregationActivity.this, MainActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivity(intent);
            }
        });
        cityJson = readLocalJson(SegregationActivity.this,"cityList.json");
        initAction();

//        getMyLocation();
    }

    private void initAction() {
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                String start_city = start_et.getText().toString().trim();
                String end_city = end_et.getText().toString().trim();
                String start_pro = start_spinner.getSelectedItem().toString();
                String end_pro = end_spinner.getSelectedItem().toString();
                if(TextUtils.equals(start_city,end_city)){
                    Toast.makeText(SegregationActivity.this, "目的地和出发地不能一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                //拼音缩写
                String start_pinyin =null;
                String end_pinyin = null;
                start_pinyin = getPyChar(SegregationActivity.this, start_pro, start_city);
                end_pinyin = getPyChar(SegregationActivity.this, end_pro, end_city);
                if(start_pinyin==null||end_pinyin==null){
                    AlertDialog dialog;
                    dialog = new AlertDialog.Builder(SegregationActivity.this)
                            .setTitle("警告")  //设置标题
                            .setIcon(R.mipmap.ic_launcher) //设置图标
                            .setMessage("请输入正确的城市名") //提示信息
                            .setPositiveButton("确定",null)   //添加“确定”按钮
                            .setNegativeButton("取消",null)   //添加“取消”按钮
                            .create();  //创建对话框
                    dialog.show();  //显示对话框
                    return;
                }


                //网络请求
                String url="http://"+start_pinyin+baseurl+"leavecity="+end_pinyin;
                Request request = new Request.Builder().addHeader("User-Agent", "").url(url).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Toast.makeText(SegregationActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseString = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Matcher matcher = Pattern.compile("<div class=\"result\".*?>(.+?)</div>",Pattern.DOTALL|Pattern.MULTILINE).matcher(responseString);
                                List<String> results = new ArrayList<>();
                                results.clear();
                                while (matcher.find()) {
                                    results.add(matcher.group(1));
                                }
                                if(results.size()!=0){
                                    if(results.size()>0){
                                        start_content.setText(results.get(0).replace("<br>","\n"));
                                    }
                                    if(results.size()>1){
                                        end_content.setText(results.get(1).replace("<br>","\n"));
                                    }
                                }else{
                                    Toast.makeText(SegregationActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                //标题
                tv_s.setText(start_city);
                tv_e.setText(end_city);
                seg_content.setVisibility(View.VISIBLE);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //交换下拉框内容
                Integer start = start_spinner.getSelectedItemPosition();
                Integer end = end_spinner.getSelectedItemPosition();
                start_spinner.setSelection(end);
                end_spinner.setSelection(start);
                //swap editText
                String start_tmp = start_et.getText().toString().trim();
                String end_tmp = end_et.getText().toString().trim();
                start_et.setText(end_tmp);
                end_et.setText(start_tmp);

            }
        });
        //location dialog
        String province = "北京";
        String city = "北京";
        AlertDialog.Builder builder = new AlertDialog.Builder(SegregationActivity.this);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.location);
        builder.setMessage("您当前定位城市为" + city + ",是否需要选择" + city + "为出发城市");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] proList = getResources().getStringArray(R.array.pro_list);
                Integer position = 0;
                for (String pro : proList) {
                    if (province.equals(pro))
                        break;
                    position++;
                }
                start_spinner.setSelection(position);
                start_et.setText(city);
                dialogInterface.dismiss(); //关闭dialog
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); //关闭dialog
            }
        });//设置标题
        builder.create().show();



    }

    private String getPyChar(Context context, String pro, String city){
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(cityJson);
            JSONObject citylist = jsonObject.getJSONObject(pro).getJSONObject("citylist");
            JSONObject cityname = citylist.getJSONObject(city);
            if(cityname!=null) {
                return cityname.getString("code");
            }
        }catch (JSONException e){
            return null;
        }
        return null;
    }

    private void initView() {
        start_spinner = findViewById(R.id.start_place);
        end_spinner = findViewById(R.id.end_place);
        transfer = findViewById(R.id.transfer);
        imgV = findViewById(R.id.geli_imgView);
        start_et = findViewById(R.id.start_et);
        end_et = findViewById(R.id.end_et);
        search_btn = findViewById(R.id.geli_search_btn);
        start_content = findViewById(R.id.start_content);
        end_content = findViewById(R.id.end_content);
        tv_s = findViewById(R.id.tv_s);
        tv_e = findViewById(R.id.tv_e);
        seg_content = findViewById(R.id.seg_content);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SegregationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static String readLocalJson(Context context, String fileName){
        String resultString="";
        try {
            InputStream inputStream=context.getResources().getAssets().open(fileName);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString=new String(buffer,"utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultString;
    }

}
