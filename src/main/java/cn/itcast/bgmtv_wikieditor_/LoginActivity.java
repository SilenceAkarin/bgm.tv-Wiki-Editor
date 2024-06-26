package cn.itcast.bgmtv_wikieditor_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button login_button;
    private TextView reg_button;
    private EditText emailinput;
    private EditText passwordinput;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_button = findViewById(R.id.loginbutton);
        TextView reg_button = findViewById(R.id.registerbutton);
        EditText emailinput = findViewById(R.id.emailinput);
        EditText passwordinput = findViewById(R.id.passwordinput);

        //获取mSharedPreferences
        mSharedPreferences = getSharedPreferences("email",MODE_PRIVATE);

        //默认记住密码
        boolean isLogin = mSharedPreferences.getBoolean("is_login",true);
        String email = mSharedPreferences.getString("email",null);
        String password = mSharedPreferences.getString("password",null);

        if (isLogin){
            emailinput.setText(email);
            passwordinput.setText(password);
        }
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailinput.getText().toString();
                String password = passwordinput.getText().toString();
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "请输入邮箱和密码", Toast.LENGTH_SHORT).show();
                }else {
                    //保存账号密码
                    UserInfo login = UserDBHelper.getInstance(LoginActivity.this).login(email);
                    if (login != null) {
                        if(email.equals(login.getEmail()) && password.equals(login.getPassword())) {
                            SharedPreferences.Editor edit = mSharedPreferences.edit();
                            edit.putBoolean("is_login",isLogin);
                            edit.commit();

                            //登录成功
                            //Intent intent = new Intent(LoginActivity.this,MainActivity.class);startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "邮箱或密码有误", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "邮箱或密码有误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}