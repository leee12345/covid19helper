package bjfu.it.lihongjing.covid_19helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import bjfu.it.lihongjing.covid_19helper.fragment.MyFragment;
import bjfu.it.lihongjing.covid_19helper.utils.DBOpenHelper;

public class ForgetActivity extends AppCompatActivity {
    public static final String PW_KEY = "pw";
    private DBOpenHelper dbOpenHelper;
    private EditText telname;
    private EditText newpassword;
    private EditText confirmpassword;
    private EditText VcCode;
    private Button getVcBtn;
    private Button resetBtn;

    private Integer vc_code;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpw);
        initView();
        dbOpenHelper = new DBOpenHelper(ForgetActivity.this);
    }
    private void initView() {
        telname=findViewById(R.id.tel);
        newpassword=findViewById(R.id.newpass);
        confirmpassword=findViewById(R.id.confirm);
        VcCode=findViewById(R.id.vcLogin);
        getVcBtn=findViewById(R.id.getVcBtn);
        resetBtn=findViewById(R.id.resetBtn);
        Intent intent = getIntent();
        String tel=intent.getStringExtra(LoginActivity.TEL_KEY);
        telname.setText(tel);
        //重置
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置
                String tel = telname.getText().toString().trim();
                String pwd = newpassword.getText().toString().trim();
                String cpwd = confirmpassword.getText().toString().trim();
                String vcCode = VcCode.getText().toString().trim();
                if(!TextUtils.isEmpty(tel)  && !TextUtils.isEmpty(pwd)&& !TextUtils.isEmpty(cpwd)&& !TextUtils.isEmpty(vcCode)) {
                    boolean matchPw=false;
                    boolean matchVc=false;
                    matchPw=TextUtils.equals(pwd, cpwd);
                    matchVc=TextUtils.equals(vcCode, vc_code.toString());
                    if (matchPw&&matchVc){
                        dbOpenHelper.updateUser(tel,pwd);
                        Toast.makeText(ForgetActivity.this, "密码更新成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                        intent.putExtra(PW_KEY, pwd);
                        startActivity(intent);
                        finish();
                    }
                    else if(!matchPw){
                        Toast.makeText(ForgetActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                        confirmpassword.setText("");
                    }
                    else{
                        Toast.makeText(ForgetActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        VcCode.setText("");
                    }

                }else if(TextUtils.isEmpty(tel)){
                    Toast.makeText(ForgetActivity.this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(ForgetActivity.this, "请输入您的新密码", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(cpwd)){
                    Toast.makeText(ForgetActivity.this, "请确认您的新密码", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(vcCode)){
                    Toast.makeText(ForgetActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //忘记密码，获取验证码btn
        getVcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取验证码
                String tel = telname.getText().toString().trim();
                if(!TextUtils.isEmpty(tel)){
                    vc_code = getVCode(100000,999999);
                    AlertDialog dialog;
                    dialog = new AlertDialog.Builder(ForgetActivity.this)
                            .setTitle("验证码")  //设置标题
                            .setIcon(R.mipmap.ic_launcher) //设置图标
                            .setMessage("手机号"+tel+"，本次验证码是"+vc_code+"，请输入验证码。") //提示信息
                            .setPositiveButton("确定",null)   //添加“确定”按钮
                            .setNegativeButton("取消",null)   //添加“取消”按钮
                            .create();  //创建对话框
                    dialog.show();  //显示对话框

                }else{
                    Toast.makeText(ForgetActivity.this, "请先输入手机号", Toast.LENGTH_SHORT).show();
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
}