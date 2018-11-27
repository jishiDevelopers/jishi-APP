package com.bbel.eatnow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText userTel;
    private EditText userPasswd;
    private EditText userPasswdRepeat;
    private EditText userCode;
    private Button buttonSend;

    private String user_tel;
    private String user_passwd;
    private String user_passwd_repeat;
    private String user_code;
    private int http_code;
    private Handler hHandler;
    private boolean code = false;

    private FloatingActionButton fab;
    private CardView forget;
    private Button buttonReset;
    private String url = "http://193.112.6.8/password_forget";

    public class Data {
        private String id;
        private String token;
        private String tel;
        private String info;
        private String passwd;

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getToken() {

            return token;
        }

        public void setToken(String token) {
            this.token = token;
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
        setContentView(R.layout.activity_forget_password);
        ShowEnterAnimation();
        hHandler = new HHanlder();
        initView();
        setListener();

        //初始化MobSDK
        MobSDK.init(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        forget = findViewById(R.id.forget);
        buttonReset = findViewById(R.id.bt_go);
        buttonSend = findViewById(R.id.button_send);
        userTel = findViewById(R.id.et_username);
        //userCode = findViewById(R.id.et_code);
        userPasswd = findViewById(R.id.et_password);
        userPasswdRepeat = findViewById(R.id.et_repeat_password);
    }

    private void setListener() {
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                user_tel = userTel.getText().toString();
                user_passwd = userPasswd.getText().toString();
                user_passwd_repeat = userPasswdRepeat.getText().toString();
                //user_code = userCode.getText().toString();
                //账号或密码不能为空
                if(user_tel.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "账号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if(user_passwd.isEmpty() && user_passwd_repeat.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if(user_passwd.equals(user_passwd_repeat) && code == true) {
                    sendRequestWithOkHttp();
                }else if(user_passwd.equals(user_passwd_repeat) && code == false) {
                    Toast.makeText(ForgetPasswordActivity.this, "请进行短信验证",
                            Toast.LENGTH_SHORT).show();
                }
                else if(!user_passwd.equals(user_passwd_repeat)) {
                    Toast.makeText(ForgetPasswordActivity.this, "密码输入不一致",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(ForgetPasswordActivity.this);
            }
        });
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                forget.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(forget, forget.getWidth()/2,0, fab.getWidth() / 2, forget.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                forget.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(forget, forget.getWidth()/2,0, forget.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                forget.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                ForgetPasswordActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Data data = new Data();
                    data.setTel(user_tel);
                    //data.setId("3");
                    //data.setPasswd(user_passwd);
                    //data.setToken("c48b234fa780da9c63f42ccadb115dee50d77783");
                    Gson gson = new Gson();
                    String toJson = gson.toJson(data);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    http_code = response.code();
                    Log.d("RegisterActivity1", "code is " + http_code);
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        Data data = gson.fromJson(jsonData, Data.class);
        String info = data.getInfo();
        String id = data.getId();
        String token = data.getToken();
/*
        Log.d("RegisterActivity1", "info is " + info);
        Log.d("RegisterActivity1", "id is " + id);
        Log.d("RegisterActivity1", "token is " + token);
*/
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("http_code", http_code);
        message.setData(bundle);
        message.what = 1;
        hHandler.sendMessage(message);
    }

    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    code = true;
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }


    private class HHanlder extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    int http_code = bundle.getInt("http_code");
                    //判断HTTP状态码
                    if(http_code == 200) {
                        ActivityOptions oc2 = ActivityOptions.makeSceneTransitionAnimation(ForgetPasswordActivity.this);
                        Intent i2 = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                        startActivity(i2, oc2.toBundle());
                        Toast.makeText(ForgetPasswordActivity.this, "重置密码成功",
                                Toast.LENGTH_SHORT).show();
                    } else if (http_code == 400){
                        Toast.makeText(ForgetPasswordActivity.this, "该账号不存在",
                                Toast.LENGTH_SHORT).show();
                    } else if(http_code == 402) {
                        Toast.makeText(ForgetPasswordActivity.this, "请重新获取验证码",
                                Toast.LENGTH_SHORT).show();
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
