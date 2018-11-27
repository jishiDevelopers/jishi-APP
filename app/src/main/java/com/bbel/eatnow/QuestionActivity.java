package com.bbel.eatnow;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bbel.eatnow.adapter.CardFragmentPagerAdapter;
import com.bbel.eatnow.adapter.CardPagerAdapter;
import com.bbel.eatnow.utils.ShadowTransformer;
import com.bbel.eatnow.bean.CardItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_yes;
    private Button button_no;
    private Button button_go;
    private ViewPager mViewPager;

    //GET和POST的数据
    private String url = "http://193.112.6.8/question_request";
    private int http_code;
    public static final int UPDATE_TEXT = 1;
    private String string;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private String result;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;

    public class Dish {
        private String dishNum;
        private String idRecord;
        private List dish_list;

        public List getDish_list() {
            return dish_list;
        }

        public void setDish_list(List dish_list) {
            this.dish_list = dish_list;
        }

        public String getIdRecord() {

            return idRecord;
        }

        public void setIdRecord(String idRecord) {
            this.idRecord = idRecord;
        }

        public String getDishNum() {

            return dishNum;
        }

        public void setDishNum(String dishNum) {
            this.dishNum = dishNum;
        }
    }

    public class Recommend {
        private String idQuestions;
        private String id;
        private String token;
        private String ans;

        public String getAns() {
            return ans;
        }

        public void setAns(String ans) {
            this.ans = ans;
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

        public String getidQuestions() {

            return idQuestions;
        }

        public void setidQuestions(String question_id_list) {
            this.idQuestions = question_id_list;
        }
    }

    public class Question {
        private String content1;
        private String content2;
        private String content3;
        private String question_id_list;

        public String getQuestion_id_list() {
            return question_id_list;
        }

        public void setQuestion_id_list(String question_id_list) {
            this.question_id_list = question_id_list;
        }

        public String getContent3() {

            return content3;
        }

        public void setContent3(String content3) {
            this.content3 = content3;
        }

        public String getContent2() {

            return content2;
        }

        public void setContent2(String content2) {
            this.content2 = content2;
        }

        public String getContent1() {

            return content1;
        }

        public void setContent1(String content1) {
            this.content1 = content1;
        }
    }

    public class User {
        private String user_id;
        private String token;
        private int number = 3;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getToken() {

            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        mViewPager = findViewById(R.id.viewPager);
        button_yes = findViewById(R.id.button_yes);
        button_no = findViewById(R.id.button_no);
        button_go = findViewById(R.id.btn_go);
        // ((CheckBox) findViewById(R.id.checkBox)).setOnCheckedChangeListener(this);
        button_yes.setOnClickListener(this);
        button_no.setOnClickListener(this);
        button_go.setOnClickListener(this);

        button_go.setVisibility(View.INVISIBLE);

        sendRequestWithOkHttp();
        mCardAdapter = new CardPagerAdapter();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setPageTransformer(true, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(list.size() == 3) {
                    button_go.setVisibility(View.VISIBLE);
                } else {
                    button_go.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    // 在这里可以进行UI操作
                    string = msg.getData().getString("key");
                    mCardAdapter.addCardItem(new CardItem("1", msg.getData().getString("content1")));
                    mCardAdapter.addCardItem(new CardItem("2", msg.getData().getString("content2")));
                    mCardAdapter.addCardItem(new CardItem("3", msg.getData().getString("content3")));
                    mViewPager.setAdapter(mCardAdapter);
                    mCardShadowTransformer.enableScaling(true);
                    mFragmentCardShadowTransformer.enableScaling(true);
                    break;
                default:
                    break;
            }
        }
    };

    //GET数据
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //读取用户ID和token
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    String user_id = pref.getString("id", "0");
                    String token = pref.getString("token", "0");

                    User user = new User();
                    user.setUser_id(user_id);
                    user.setToken(token);
                    user.setNumber(3);

                    SharedPreferences.Editor editor = getSharedPreferences("question_answer", MODE_PRIVATE).edit();
                    editor.putString("id", user_id);
                    editor.putString("token", token);
                    editor.apply();

                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    String toJson = gson.toJson(user);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    http_code = response.code();
                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        Gson gson = new Gson();
        Question data = gson.fromJson(response, Question.class);
        //提取问题ID和问题内容
        String question_id_list = data.getQuestion_id_list();
        String content1 = data.getContent1();
        String content2 = data.getContent2();
        String content3 = data.getContent3();

        SharedPreferences.Editor editor = getSharedPreferences("question_answer", MODE_PRIVATE).edit();
        editor.putString("idQuestions", question_id_list);
        editor.apply();

        Message message = new Message();
        message.what = UPDATE_TEXT;
        Bundle bundle = new Bundle();
        bundle.putString("content1", content1);
        bundle.putString("content2", content2);
        bundle.putString("content3", content3);
        message.setData(bundle);
        handler.sendMessage(message); // 将Message对象发送出去
    }

    @Override
    public void onClick(View view) {
        Map<String,String> map = new HashMap<>();
        SharedPreferences.Editor editor = getSharedPreferences("question_answer", MODE_PRIVATE).edit();
        editor.putString("ans", list.toString());
        editor.apply();
        switch (view.getId()) {
            case R.id.button_yes:
                map = new HashMap<>();
                map.put("ans", "y");
                if(list.size() < 3) {
                    list.add(map);
                } else {
                    list.set(mViewPager.getCurrentItem(), map);
                }
                if (mViewPager.getCurrentItem() == 2) {
                    button_go.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.button_no:
                map = new HashMap<>();
                map.put("ans", "n");
                if(list.size() < 3) {
                    list.add(map);
                } else {
                    list.set(mViewPager.getCurrentItem(), map);
                }
                if (mViewPager.getCurrentItem() == 2) {
                    button_go.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_go:
                if(getDishWithOkHttp() == true) {
                    ActivityOptions oc2 = ActivityOptions.makeSceneTransitionAnimation(QuestionActivity.this);
                    //Intent i2 = new Intent(QuestionActivity.this,RecommendActivity.class);
                    //startActivity(i2, oc2.toBundle());
                }
                break;
            default:
                break;
        }
        if(mViewPager.getCurrentItem() < 2) {
            mViewPager.arrowScroll(2);
        }
    }

    private boolean getDishWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences pref = getSharedPreferences("question_answer", MODE_PRIVATE);
                    String id = pref.getString("id", "error");
                    String token = pref.getString("token", "error");
                    String idQuestions = pref.getString("idQuestions", "error");
                    String answer = pref.getString("ans", "error");
                    String ans = "";
                    for(int i=0; i<answer.length(); i++) {
                        if (answer.charAt(i) == '=') {
                            if(ans == "") {
                                ans = ans + answer.charAt(i+1);
                                i++;
                            } else {
                                ans = ans + "," + answer.charAt(i+1);
                                i++;
                            }
                        }
                    }

                    Recommend recommend = new Recommend();
                    recommend.setId(id);
                    recommend.setidQuestions(idQuestions);
                    recommend.setToken(token);
                    recommend.setAns(ans);

                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    String toJson = gson.toJson(recommend);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
                    Request request = new Request.Builder()
                            .url("http://193.112.6.8/recommend")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    http_code = response.code();

                    SharedPreferences.Editor editor = getSharedPreferences("result", MODE_PRIVATE).edit();
                    editor.putString("result", responseData);
                    editor.apply();

                    //showRecommend(responseData);
                    //Log.d("LoginActivity1", "dish is " + responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }
/*
    private void showRecommend(final String response) {
        Gson gson = new Gson();
        Dish dish = gson.fromJson(response, Dish.class);
        //提取问题ID和问题内容
        String dishNum = dish.getDishNum();
        String idRecord = dish.getIdRecord();
        List dish_list = dish.getDish_list();


        SharedPreferences.Editor editor = getSharedPreferences("question_answer", MODE_PRIVATE).edit();
        editor.putString("idQuestions", question_id_list);
        editor.apply();
    }*/

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    // @Override
    // public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    //    mCardShadowTransformer.enableScaling(b);
    //    mFragmentCardShadowTransformer.enableScaling(b);
    //}
}
