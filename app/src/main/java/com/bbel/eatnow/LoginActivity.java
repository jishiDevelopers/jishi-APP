package com.bbel.eatnow;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userTel;
    private EditText userPasswd;
    private Button buttonGo;
    private CardView login;
    private FloatingActionButton fabRegister;
    private TextView textViewForgetPassword;
    private String user_tel;
    private String user_passwd;
    private int http_code;
    private String user_id;
    private String token;
    private Handler hHandler;

    String url = "http://193.112.6.8/login";

    public class User {
        private String tel;
        private String passwd;

        public void User(String tel, String passwd) {
            this.tel = tel;
            this.passwd = passwd;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }

    public class Data {
        private String id;
        private String info;
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getInfo() {

            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hHandler = new HHanlder();
        initView();
        setListener();
    }

    private void initView() {
        userTel = findViewById(R.id.et_username);
        userPasswd = findViewById(R.id.et_password);
        buttonGo = findViewById(R.id.bt_go);
        login = findViewById(R.id.login);
        fabRegister = findViewById(R.id.fab);
        textViewForgetPassword = findViewById(R.id.forget_password);
    }

    private void setListener() {
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Explode explode = new Explode();
                explode.setDuration(500);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);

                user_tel = userTel.getText().toString();
                user_passwd = userPasswd.getText().toString();
                if(user_tel.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "账号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if(user_passwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }else {
                    //POST
                    sendRequestWithOkHttp();
                }

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

    //POST数据测试
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    User user = new User();
                    user.setTel(user_tel);
                    user.setPasswd(user_passwd);

                    Gson gson = new Gson();
                    String toJson = gson.toJson(user);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    http_code = response.code();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData, http_code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData, int http_code){
        Gson gson = new Gson();
        Data data = gson.fromJson(jsonData, Data.class);
        String info = data.getInfo();
        user_id = data.getId();
        token = data.getToken();
/*
        Log.d("LoginActivity1", "info is " + info);
        Log.d("LoginActivity1", "id is " + user_id);
        Log.d("LoginActivity1", "token is " + token);
*/
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("http_code", http_code);
        bundle.putString("user_id", user_id);
        bundle.putString("token", token);
        message.setData(bundle);
        message.what = 1;
        hHandler.sendMessage(message);
    }


    private class HHanlder extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    int http_code = bundle.getInt("http_code");
                    String user_id = bundle.getString("user_id");
                    String token = bundle.getString("token");
                    if(http_code == 400) {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    } else if(http_code == 200) {
                        //保存用户ID和token
                        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.putString("id", user_id);
                        editor.putString("token", token);
                        editor.apply();
                        //跳转页面
                        ActivityOptionsCompat oc1 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                        Intent i1 = new Intent(LoginActivity.this, QuestionActivity.class);
                        startActivity(i1, oc1.toBundle());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hHandler.removeCallbacksAndMessages(null);
    }
}

