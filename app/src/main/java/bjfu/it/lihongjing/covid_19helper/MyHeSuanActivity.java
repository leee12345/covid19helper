package bjfu.it.lihongjing.covid_19helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bjfu.it.lihongjing.covid_19helper.fragment.MyFragment;
import bjfu.it.lihongjing.covid_19helper.utils.DBOpenHelper;

public class MyHeSuanActivity extends AppCompatActivity {
    final public static String TEL_KEY = "tel";
    final public static String NAME_KEY = "name";

    private ImageView back;
    private ImageView img_view;
    private TextView name_text;
    private TextView  result_text;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhesuan);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        name_text=findViewById(R.id.name_hesuan);
        result_text=findViewById(R.id.he_result);
        img_view=findViewById(R.id.hesuan_view);
        dbOpenHelper = new DBOpenHelper(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra(MyFragment.NAME_KEY);
        String tel=intent.getStringExtra(MyFragment.TEL_KEY);
        Integer result=dbOpenHelper.queryUserHe(tel);

        name_text.setText(name);
        if(result==0){
            result_text.setText("阴性");
            result_text.setTextColor(Color.parseColor("#98FB98"));
        }
        else if(result==1){
            result_text.setText("阳性");
            result_text.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            result_text.setText("无记录");
            result_text.setTextColor(Color.parseColor("#C0C0C0"));
        }

        back=findViewById(R.id.back_hesuan);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyHeSuanActivity.this, MainActivity.class);
                intent.putExtra(TEL_KEY, tel);
                intent.putExtra(NAME_KEY, name);
                startActivity(intent);
            }
        });
        String imgUrl=dbOpenHelper.queryUserImg(tel);
        img_view.setImageBitmap(returnBitMap(imgUrl));
    }


    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
