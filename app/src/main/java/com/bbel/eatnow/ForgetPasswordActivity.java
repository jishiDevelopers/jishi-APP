package com.bbel.eatnow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
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

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mob.tools.utils.ResHelper.getStringRes;

public class ForgetPasswordActivity extends BaseActivity {

    private EditText userTel;
    private EditText userPasswd;
    private EditText userPasswdRepeat;
    private Button buttonSend;
    private EditText userCode;
    private TextView now;

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

    private String iPhone;
    private String iCord;
    private int time = 60;
    private boolean flag = true;

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
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        forget = findViewById(R.id.forget);
        buttonReset = findViewById(R.id.bt_go);
        buttonSend = findViewById(R.id.button_send);
        userTel = findViewById(R.id.et_username);
        userCode = findViewById(R.id.et_code);
        userPasswd = findViewById(R.id.et_password);
        userPasswdRepeat = findViewById(R.id.et_repeat_password);
        now = findViewById(R.id.now);
    }

    private void setListener() {
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if(!TextUtils.isEmpty(userCode.getText().toString().trim())){
                    if(userCode.getText().toString().trim().length()==4){
                        iCord = userCode.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                        flag = false;
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        userCode.requestFocus();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    userCode.requestFocus();
                }

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
                }
                else if(!user_passwd.equals(user_passwd_repeat)) {
                    Toast.makeText(ForgetPasswordActivity.this, "密码输入不一致",
                            Toast.LENGTH_SHORT).show();
                }else {
                    sendRequestWithOkHttp();
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(userTel.getText().toString().trim())){
                    if(userTel.getText().toString().trim().length()==11){
                        iPhone = userTel.getText().toString().trim();
                        SMSSDK.getVerificationCode("86",iPhone);
                        userCode.requestFocus();
                        buttonSend.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        userTel.requestFocus();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    userTel.requestFocus();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
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
                    //Log.d("RegisterActivity1", "code is " + http_code);
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
/*
        String info = data.getInfo();
        String id = data.getId();
        String token = data.getToken();

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

    private class HHanlder extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    int http_code = bundle.getInt("http_code");
                    //判断HTTP状态码
                    if(code == true) {
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
                    }
                    break;
            }
        }
    }

    //验证码送成功后提示文字
    private void reminderText() {
        now.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handlerText =new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==1){
                if(time>0){
                    now.setText("验证码已发送"+time+"秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                }else{
                    now.setText("提示信息");
                    time = 60;
                    now.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                }
            }else{
                userCode.setText("");
                now.setText("提示信息");
                time = 60;
                now.setVisibility(View.GONE);
                buttonSend.setVisibility(View.VISIBLE);
            }
        }
    };

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);

            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    //Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    code = true;
                    handlerText.sendEmptyMessage(2);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    buttonSend.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetPasswordActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    userTel.requestFocus();
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(ForgetPasswordActivity.this, "smssdk_network_error");
                    Toast.makeText(ForgetPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    userCode.selectAll();
/*                    if (resId > 0) {
                        Toast.makeText(ForgetPasswordActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }*/
                }

            }

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hHandler.removeCallbacksAndMessages(null);
        SMSSDK.unregisterAllEventHandler();
    }

}
