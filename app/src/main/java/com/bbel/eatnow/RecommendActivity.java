package com.bbel.eatnow;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecommendActivity extends AppCompatActivity implements View.OnClickListener {
    private String restaurant;
    private String dishes;
    private String canteenName;
    private String intentMessage;
    private String demoDishes;
    private String demoRestaurant;
    private String[] dishId = new String[4];
    private String[] restaurantId = new String[4];
    private String[] dishNumArray = {"dish1", "dish2", "dish3", "dish4", "dish5", "dish6"};
    private int which;
    private int dishNum;
    private Response response = null;
    private String[] restaurantArray = new String[4];
    private String[] dishesArray = new String[4];
    private String[] canteensArray = new String[4];

    private void setDemoRestaurant(String restaurant) {
        this.demoRestaurant = "今天就吃" + "\"" + canteenName + "的" + restaurant + "\"的:";
    }

    private void setDemoDishes(String dishs) {
        this.demoDishes = "《" + dishs + "》";
    }

    public void setWhich(int which) {
        this.which = which;
    }

    public void setCanteenName(String canteenName){
        this.canteenName=canteenName;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public void addWhich(int which) {

        this.which = ++which;
        if (which > dishNum-1) {
            which = which % dishNum;
            this.which = which;
        }

    }

    final OkHttpClient client = new OkHttpClient();
    TextView tvRecommendDishes;
    TextView tvRecommendRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        intent数据;
         */
//        Intent intent=getIntent();
//        intentMessage=intent.getStringExtra("");
        getRequest();
        setWhich(0);
        setContentView(R.layout.activity_recommend);
        Button btRecommendGo = (Button) findViewById(R.id.bt_recommend_go);
        Button btRecommendNext = (Button) findViewById(R.id.bt_recommend_next_restaurant);
        btRecommendGo.setOnClickListener(this);
        btRecommendNext.setOnClickListener(this);
        tvRecommendDishes = (TextView) findViewById(R.id.tv_recommend_dish);
        tvRecommendRestaurant = (TextView) findViewById(R.id.tv_recommend_restaurant);
        setResultRecommend(which);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*换一道菜*/
            case R.id.bt_recommend_next_restaurant: {
                addWhich(which);
                setResultRecommend(which);
                break;
            }
            /*下一家*/
            case R.id.bt_recommend_go: {
                //postRequest();
                Intent intent = new Intent(RecommendActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

    }
    /**
     * 展示结果文本
     */
    public void setResultRecommend(int which) {
        setRestaurant(restaurantArray[which]);
        setDishes(dishesArray[which]);
        setCanteenName(canteensArray[which]);
        setDemoRestaurant(restaurant);
        setDemoDishes(dishes);
        tvRecommendRestaurant.setText(demoRestaurant);
        tvRecommendDishes.setText(demoDishes);
    }

    /**
     * 请求数据GET
     */
    private void getRequest() {
        final Request request = new Request.Builder()
                .get()
                .tag(this)
                .url("http://10.0.2.2/get_json.json")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resposeData = null;
                                try {
                                    resposeData = response.body().string();
                                    Log.i("网页", "打印GET响应的数据：" + resposeData);
                                    parseJSONWithJSONObject(resposeData);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        throw new IOException("Unexpected code " + response);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
private void parseJSONWithJSONObject(String jsonData) {
    try {
        {
            JSONObject jsonObject = new JSONObject(jsonData);
            dishNum =Integer.parseInt(jsonObject.getString("dishNum"));
            for (int i = 0; i < dishNum; i++) {
                String message;
                message = jsonObject.getString(dishNumArray[i]);
                JSONObject messageDish = new JSONObject(message);
                restaurantArray[i] = messageDish.getString("RestName");
                dishesArray[i] = messageDish.getString("dishName");
                canteensArray[i] = messageDish.getString("canteen");
                dishId[i]=messageDish.getString("idDish");
                restaurantId[i]=messageDish.getString("idRest");
            }
        }
        setResultRecommend(0);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    /**
     * POST
     */
    private void postRequest() {
        String postURL = "待输入";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("键值", restaurantArray[which])
                .add("键值", dishesArray[which])
                .add("评价", "评价")
                .build();
        Request request = new Request.Builder()
                .url(postURL)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

