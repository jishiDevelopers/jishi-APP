package com.bbel.eatnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bbel.eatnow.adapter.RankAdapter;
import com.bbel.eatnow.bean.RankItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RankActivity extends BaseActivity {

    private String url = "http://193.112.6.8/dishRank";

    public class Data {
        private String canteenName;
        private String restName;
        private String dishName;
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDishName() {

            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public String getRestName() {

            return restName;
        }

        public void setRestName(String restName) {
            this.restName = restName;
        }

        public String getCanteenName() {

            return canteenName;
        }

        public void setCanteenName(String canteenName) {
            this.canteenName = canteenName;
        }
    }

    private int[] images = {R.drawable.number1, R.drawable.number2, R.drawable.number3, R.drawable.number4, R.drawable.number5};

    private List<RankItem> rankList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        sendRequestWithOkHttp();

        for(int i = 0;i<100000000;i++){
            int j = i;
        }
        Log.d("cnt", "ok");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RankAdapter adapter = new RankAdapter(rankList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //sendRequestWithOkHttp();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("123", responseData);
                    List<Data> data;
                    Gson gson = new Gson();
                    data = gson.fromJson(responseData, new TypeToken<List<Data>>() {}.getType());
                    for(int i=0; i<5; i++) {
                        RankItem rankItem = new RankItem("食堂：" + data.get(i).getCanteenName(), "店名：" + data.get(i).getRestName(), data.get(i).getDishName(), "推荐次数：" + data.get(i).getCount(), images[i]);
                        rankList.add(rankItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
