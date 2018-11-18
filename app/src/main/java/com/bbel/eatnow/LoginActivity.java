package com.bbel.eatnow;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button buttonGo;
    private CardView login;
    private FloatingActionButton fabRegister;
    private TextView textViewForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    private void initView() {
        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        buttonGo = findViewById(R.id.bt_go);
        login = findViewById(R.id.login);
        fabRegister = findViewById(R.id.fab);
        textViewForgetPassword = findViewById(R.id.forget_password);
    }

    private void setListener() {
        //跳转到登录成功界面
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc1 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i1 = new Intent(LoginActivity.this, LoginSuccessActivity.class);
                startActivity(i1, oc1.toBundle());
            }
        });
        //跳转到注册界面
        fabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fabRegister, fabRegister.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });
        //跳转到忘记密码界面
        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions oc2 = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i2 = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(i2, oc2.toBundle());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fabRegister.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fabRegister.show();
    }
}

