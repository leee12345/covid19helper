package bjfu.it.lihongjing.covid_19helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import bjfu.it.lihongjing.covid_19helper.fragment.MyFragment;
import bjfu.it.lihongjing.covid_19helper.utils.DBOpenHelper;

public class LoginActivity  extends AppCompatActivity {
    public static final String TEL_KEY = "tel";
    public static final String NAME_KEY = "name";
    private LinearLayout nameLayout;
    private DBOpenHelper dbOpenHelper;
    private RadioGroup radioGroup;
    private Spinner userType;
    private EditText telname;
    private EditText username;
    private EditText password;
    private Button forgetBtn;
    private CheckBox remCheck;
    private Button loginBtn;
    private TextView userText;
    private TextView telText;
    private TextView title;
    private TextView pwdText;
    private int loginMethod;
    private Integer vc_code;
    private long id_position;
    private boolean isRem;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbOpenHelper = new DBOpenHelper(this);
        initView();

    }
    private void initView() {
        title=findViewById(R.id.login_title);
        radioGroup = findViewById(R.id.loginType);
        userType = findViewById(R.id.userType);

        //电话
        telText = findViewById(R.id.telText);
        telname = findViewById(R.id.telId);
        //姓名
        nameLayout=findViewById(R.id.name_layout);
        userText = findViewById(R.id.nameText);
        username = findViewById(R.id.username);
        //密码
        pwdText = findViewById(R.id.pwText);
        password = findViewById(R.id.password);

        forgetBtn = findViewById(R.id.forgetBtn);
        remCheck = findViewById(R.id.remPw);
        loginBtn = findViewById(R.id.loginBtn);
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        isRem = preferences.getBoolean("remember_password", false);
        id_position = 0;
        loginMethod= R.id.pwLoginBtn;
        if(isRem){
            String rem_tel = preferences.getString("telname", "");
            String rem_pwd = preferences.getString("password", "");
            telname.setText(rem_tel);
            password.setText(rem_pwd);
            remCheck.setChecked(true);
        }
        Intent intent = getIntent();
        String newpw = intent.getStringExtra(ForgetActivity.PW_KEY);
        if(newpw!=null){
            password.setText(newpw);
        }
        //设置监听事件
        //记住密码
        remCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRem = b;
            }
        });
        //密码登录 验证码登录
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                loginMethod=i;
                if(i==R.id.pwLoginBtn){
                    pwdText.setText("密码：");
                    password.setHint("请输入密码");
                    forgetBtn.setText("忘记密码");
                    remCheck.setVisibility(View.VISIBLE);
                }else if(i==R.id.vcLogin){
                    pwdText.setText("验证码：");
                    password.setHint("请输入验证码");
                    forgetBtn.setText("发送验证码");
                    remCheck.setVisibility(View.GONE);
                }else {
                    Toast.makeText(LoginActivity.this, "Unkown radioButton!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_position = i;
                String content = adapterView.getItemAtPosition(i).toString();
                if(i==0){
                    //老用户
                    nameLayout.setVisibility(View.GONE);
                    forgetBtn.setVisibility(View.VISIBLE);
                    remCheck.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.VISIBLE);
                    remCheck.setVisibility(View.VISIBLE);
                    loginBtn.setText("登录");
                    title.setText("登录");
                }else if(i==1){
                    //新用户
                    password.setText("");
                    password.setHint("请输入密码");
                    pwdText.setText("密码：");
                    nameLayout.setVisibility(View.VISIBLE);
                    forgetBtn.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    remCheck.setVisibility(View.GONE);
                    remCheck.setVisibility(View.GONE);
                    loginBtn.setText("注册");
                    title.setText("注册");
                }else {
                    Toast.makeText(LoginActivity.this, "Unkown "+content, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //登录、注册btn
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_position==0){
                    //登录
                    String tel = telname.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    if (!TextUtils.isEmpty(tel) && !TextUtils.isEmpty(pwd)) {
                        boolean match=false;
                        if(loginMethod==R.id.pwLoginBtn){
                            //密码登录
                            match = dbOpenHelper.queryOneUser(tel, pwd);
                            if (match) {
                                //登录成功,记住密码
                                editor = preferences.edit();
                                if (isRem) {
                                    editor.putBoolean("remember_password", true);
                                    editor.putString("telname", tel);
                                    editor.putString("password", pwd);
                                } else {
                                    editor.clear();
                                }
                                editor.apply();

                            } else {
                                Toast.makeText(LoginActivity.this, "手机号或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                                password.setText("");
                            }
                        }else if(loginMethod==R.id.vcLogin) {
                            //验证码登录
                            match = TextUtils.equals(vc_code.toString(), pwd);
                            if (!match){
                                Toast.makeText(LoginActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                                password.setText("");
                            }
                        }
                        if(match){
                            //登录成功
                            String un=dbOpenHelper.queryUserName(tel,pwd);
                            AlertDialog dialog;
                            dialog = new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登录成功")  //设置标题
                                    .setIcon(R.mipmap.ic_launcher) //设置图标
                                    .setMessage("您的手机号码是"+tel+"，类型是个人用户。恭喜你通过登录验证，点击\"确定返回\"按钮返回上个页面") //提示信息
                                    .setPositiveButton("进入APP",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            intent.putExtra(TEL_KEY, tel);
                                            intent.putExtra(NAME_KEY, un);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })   //添加“确定”按钮
                                    .setNegativeButton("确定返回", null)   //添加“取消”按钮
                                    .create();  //创建对话框
                            dialog.show();  //显示对话框

                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "请输入您的手机号和密码", Toast.LENGTH_SHORT).show();
                    }


                }else if(id_position==1){
                    //注册
                    String tel = telname.getText().toString().trim();
                    String name = username.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    if(!TextUtils.isEmpty(tel)&&!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                        dbOpenHelper.addUser(tel,name, pwd);
                        Toast.makeText(LoginActivity.this, "已成功注册", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(TEL_KEY, tel);
                        intent.putExtra(NAME_KEY, name);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "请输入您的手机号，姓名和密码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //忘记密码，获取验证码btn
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = telname.getText().toString().trim();
                if(loginMethod==R.id.pwLoginBtn){
                    //密码登录->忘记密码
                    Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                    intent.putExtra(TEL_KEY, tel);
                    startActivity(intent);
                    finish();
                }else if(loginMethod==R.id.vcLogin){
                    //验证码登录->获取验证码
                    if(!TextUtils.isEmpty(tel)){
                        vc_code = getVCode(100000,999999);

                        AlertDialog dialog;
                        dialog = new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("验证码")  //设置标题
                                .setIcon(R.mipmap.ic_launcher) //设置图标
                                .setMessage("手机号"+tel+"，本次验证码是"+vc_code+"，请输入验证码。") //提示信息
                                .setPositiveButton("确定",null)   //添加“确定”按钮
                                .setNegativeButton("取消",null)   //添加“取消”按钮
                                .create();  //创建对话框
                        dialog.show();  //显示对话框
                    }else{
                        Toast.makeText(LoginActivity.this, "请先输入手机号", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, ""+loginMethod, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //获取6位验证码
    public static int getVCode(int min, int max) {
        if (min > max) {
            return (int) (max + (min - max + 1) * Math.random());
        }
        return (int) (min + (max - min + 1) * Math.random());
    }

    public static class NewsActivity {
    }
}
