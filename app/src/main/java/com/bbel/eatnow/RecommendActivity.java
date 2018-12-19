package com.bbel.eatnow;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bbel.eatnow.adapter.CardFragmentPagerAdapter1;
import com.bbel.eatnow.adapter.CardPagerAdapter1;
import com.bbel.eatnow.bean.CardItem1;
import com.bbel.eatnow.bean.PostMessage;
import com.bbel.eatnow.utils.ShadowTransformer1;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecommendActivity extends BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private ViewPager mViewPager;
    private CardPagerAdapter1 mCardAdapter;
    private ShadowTransformer1 mCardShadowTransformer1;
    private CardFragmentPagerAdapter1 mFragmentCardAdapter;
    private ShadowTransformer1 mFragmentCardShadowTransformer1;
    private boolean mShowingFragments = false;
    /* 初始值可删 */
    private String[] restaurantArray = new String [5];
    /* 初始值可删 */
    private String[] dishesArray = new String [5];
    /* 初始值可删 */
    private String[] canteensArray = new String [5];
    /* 初始值可删 */
    private String[] dishNumArray = {"dish0","dish1", "dish2", "dish3", "dish4", "dish5", "dish6", "dish7", "dish8", "dish9", "dish10"};
    /* 初始值可删 */
    private String[] dishId = new String [5];
    /**
     *  初始值可删 */
    private String[] restaurantId = new String [5];
    private String [] pictureArray = new String [5];
    private int intDisuhNumber;
    private String demoDishes;
    private String intentMessage;
    private Button btGo;
    private Button btNextRest;
    private String idRecord;
    private String userId;
    private int httpCode;
    private String token;

    private String setDemoRestaurant(String restaurant, String canteenName) {
        return "那么就吃" + "\"" + canteenName + restaurant + "\"的:";
    }

    private String setDemoDishes(String dishs) {
        return this.demoDishes = "《" + dishs + "》";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter1();
        /**
        读取文件
         */
        SharedPreferences userMessage = getSharedPreferences("user", MODE_PRIVATE);
        token = userMessage.getString("token", "");
        userId = userMessage.getString("id", "");
        //SharedPreferences resultMessage = getSharedPreferences("result", MODE_PRIVATE);
        Intent getIntent =getIntent();
        /**
         *文垚给的信息;
         */
        intentMessage=getIntent.getStringExtra("intentMessage");
        parseJSONWithJSONObject(intentMessage);
        for (int i = 0; i < intDisuhNumber; i++) {
            String number = Integer.toString(i + 1);
            mCardAdapter.addCardItem(new CardItem1(setDemoRestaurant(restaurantArray[i], canteensArray[i]), setDemoDishes(dishesArray[i]), number));
        }
        mFragmentCardAdapter = new CardFragmentPagerAdapter1(getSupportFragmentManager(),
                dpToPixels(2, this));
        mCardShadowTransformer1 = new ShadowTransformer1(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer1 = new ShadowTransformer1(mViewPager, mFragmentCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer1);
        mViewPager.setOffscreenPageLimit(2);
        btGo = (Button) findViewById(R.id.take_me_go);
        btNextRest = (Button) findViewById(R.id.next_restaurant);
        btNextRest.setOnClickListener(this);
        btGo.setOnClickListener(this);
        //
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.take_me_go: {
                String item = Integer.toString(mViewPager.getCurrentItem());
                int i = Integer.valueOf(item);
                /**
                 * 需要更改活动名
                 */
                Intent intent =new Intent(RecommendActivity.this,HistoryActivity.class);
                intent.putExtra("idRest",restaurantId[i]);
                intent.putExtra("canteenid",pictureArray[i]);
               startActivity(intent);
                //Log.d("第几页啊:", item);
                startActivity(new Intent(RecommendActivity.this, StoreLocationActivity.class));
                postRequest(i);
                switch (httpCode) {
                    case 400: {
                        Toast.makeText(RecommendActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 401: {
                        Toast.makeText(RecommendActivity.this, "用户信息错误", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 402: {
                        Toast.makeText(RecommendActivity.this, "登录超时，请重新登陆", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 403: {
                        Toast.makeText(RecommendActivity.this, "评价插入失败", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;


                }
                break;
            }
            case R.id.next_restaurant: {

                int number = intDisuhNumber;
                int currentItem = mViewPager.getCurrentItem();
                currentItem = currentItem + 1;
                if (currentItem > number - 1) {
                    currentItem = currentItem % number;
                }
                mViewPager.setCurrentItem(currentItem);
            }
            default:
                break;
        }

    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer1.enableScaling(b);
        mFragmentCardShadowTransformer1.enableScaling(b);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            {
                JSONObject jsonObject = new JSONObject(jsonData);
                intDisuhNumber = Integer.parseInt(jsonObject.getString("dishNum"));
                if (intDisuhNumber >= 10) {
                    intDisuhNumber = 10;
                }
                Log.d("W",jsonObject.getString("dishNum"));
                idRecord = jsonObject.getString("idRecord");
                for (int i = 0; i < intDisuhNumber; i++) {
                    String message;
                    message = jsonObject.getString(dishNumArray[i]);
                    JSONObject messageDish = new JSONObject(message);
                    restaurantArray[i] = messageDish.getString("restName");
                    dishesArray[i] = messageDish.getString("dishName");
                    canteensArray[i] = messageDish.getString("canteen");
                    dishId[i] = messageDish.getString("idDish");
                    restaurantId[i] = messageDish.getString("idRest");
                    pictureArray[i]=messageDish.getString("canteenid");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void postRequest(int i) {
        String postURL = "http://193.112.6.8/record_store";
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        PostMessage postMessage = new PostMessage();
        postMessage.setFinalChoice(dishId[i]);
        postMessage.setId(userId);
        postMessage.setIdRecord(idRecord);
        postMessage.setToken(token);
        postMessage.setJudge("true");
        Gson gson = new Gson();
        String toJson = gson.toJson(postMessage);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), toJson);
        final Request request = new Request.Builder()
                .url(postURL)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("onFailure", "fail");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.w("我要返回的数据啊：", response.body().string());
                httpCode = response.code();
            }
        });


    }
}

