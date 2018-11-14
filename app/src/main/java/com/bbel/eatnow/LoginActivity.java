package com.bbel.eatnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bbel.eatnow.R;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button buttonGo;
    private CardView login;
    private FloatingActionButton fabRegister;
    private FloatingActionButton fabForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_Login);
        initView();
        setListener();
    }

    private void initView() {
        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        buttonGo = findViewById(R.id.bt_go);
        login = findViewById(R.id.login);
        fabRegister = findViewById(R.id.fab);
        fabForgetPassword = findViewById(R.id.fab_f);
    }

    private void setListener() {
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                Intent i2 = new Intent(LoginActivity.this,LoginSuccessActivity.class);
                startActivity(i2, oc2.toBundle());
            }
        });
        fabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fabRegister, fabRegister.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });
        fabForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fabForgetPassword, fabForgetPassword.getTransitionName());
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class), options.toBundle());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fabRegister.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fabRegister.setVisibility(View.VISIBLE);
    }
}

