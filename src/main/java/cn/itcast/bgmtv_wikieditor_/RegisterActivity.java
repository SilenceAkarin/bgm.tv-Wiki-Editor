package cn.itcast.bgmtv_wikieditor_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Button reg_button;
    private EditText emailinput;
    private EditText passwordinput;
    private EditText passwordinput_again;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button reg_button = findViewById(R.id.register);
        EditText emailinput = findViewById(R.id.emailinput);
        EditText passwordinput = findViewById(R.id.passwordinput);
        EditText passwordinput_again = findViewById(R.id.passwordinput_again);

        //返回登录页面
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击注册
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailinput.getText().toString();
                String password = passwordinput.getText().toString();
                String password_ag = passwordinput_again.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(password_ag)) {
                    Toast.makeText(RegisterActivity.this, "请输入邮箱和密码", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(password_ag)) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    int row = UserDBHelper.getInstance(RegisterActivity.this).register(email,password,"Empty");
                    if (row>0) {
                        Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}