package com.bbel.eatnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String restaurant;
    private String dishes;

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    private String demoDishes;

    private void setDemoDishes(String dishs) {
        this.demoDishes ="《"+dishs + "》吧";
    }

    private String demoRestaurant;

    private void setDemoRestaurant(String restaurant) {
        this.demoRestaurant = "今天就吃《" + restaurant + "》的:";
    }

    private int which;
    public void addWhich(int which) {

        this.which=++which;
        if (which > 3) {
            which = which % 4;
            this.which=which;
        }

    }
    public void setWhich(int which) {
        this.which = which;
    }

    TextView tvRecommendDishes;
    TextView tvRecommendRestaurant;
    private String[] restaurantArray = {"腊么香", "港式秘密", "新概念", "小米米"};
    private String[] dishesArray = {"黄金全腿饭", "玉米三鲜", "荔枝肉饭", "鱼香肉丝"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button btRecommendGo = (Button) findViewById(R.id.bt_recommend_go);
        Button btRecommendNext = (Button) findViewById(R.id.bt_recommend_next_restaurant);
        Button btChooseFitst =(Button)findViewById(R.id.bt_recommend_first) ;
        Button btChooseSecond=(Button)findViewById(R.id.bt_recommend_second);
        Button btChooseThird=(Button)findViewById(R.id.bt_recommend_third);
        Button btChooseFourth=(Button)findViewById(R.id.bt_recommend_fourth);
        btChooseFitst.setOnClickListener(this);
        btChooseSecond.setOnClickListener(this);
        btChooseThird.setOnClickListener(this);
        btChooseFourth.setOnClickListener(this);
        btRecommendGo.setOnClickListener(this);
        btRecommendNext.setOnClickListener(this);
        tvRecommendDishes = (TextView) findViewById(R.id.tv_recommend_dish);
        tvRecommendRestaurant = (TextView) findViewById(R.id.tv_recommend_restaurant);
        setWhich(0);
        setRestaurant(restaurantArray[which]);
        setDishes(dishesArray[which]);
        setDemoRestaurant(restaurant);
        setDemoDishes(dishes);
        tvRecommendRestaurant.setText(demoRestaurant);
        tvRecommendDishes.setText(demoDishes);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //换一家
            case R.id.bt_recommend_next_restaurant: {
                addWhich(which);
                setResultRecommend(which);
                break;
            }
            case R.id.bt_recommend_go: {
                //sendRequestWithOkhttp();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.bt_recommend_first:{
                setWhich(0);
                setResultRecommend(which);
                break;
            }
            case R.id.bt_recommend_second:{
                setWhich(1);
                setResultRecommend(which);
                break;
            }
            case R.id.bt_recommend_third:{
                setWhich(2);
                setResultRecommend(which);
                break;
            }
            case R.id.bt_recommend_fourth:{
                setWhich(3);
                setResultRecommend(which);
                break;
            }
            default:
                break;
        }
    }

    private void sendRequestWithOkhttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(MainActivity.this.toString(), responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void setResultRecommend(int which) {
        setRestaurant(restaurantArray[which]);
        setDishes(dishesArray[which]);
        setDemoRestaurant(restaurant);
        setDemoDishes(dishes);
        tvRecommendRestaurant.setText(demoRestaurant);
        tvRecommendDishes.setText(demoDishes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup: {
                Toast.makeText(this, "This is our logo", Toast.LENGTH_SHORT).show();
                break;
            }

            default:
        }
        return true;
    }
}
