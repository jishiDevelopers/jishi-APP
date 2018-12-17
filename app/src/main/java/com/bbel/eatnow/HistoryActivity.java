package com.bbel.eatnow;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HistoryActivity extends AppCompatActivity {
    private MyAdapter adapter;
    private String url = "http://193.112.6.8/history";
    private int httpCode;
    private String responseData;
    private RecyclerView mRecyclerView;
    private int intRecordNumber;
    /**
     * 最后选择
     */
    private String[] finalChooseArray;
    /**
     * 评价结果
     */
    private String[] judgeArray ;
    /**
     * 时间戳
     */
    private String[] timeArray ;
    private String record = "record";
    private String[] recordArray;
    List<ContactInfo> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        /**
         * 初始化适配器
         */
        adapter=new MyAdapter(mList);
        mRecyclerView.setAdapter(adapter);
        //发送POST
        sendRequestWithOkHttp();
        //解析post后的东西
//        parseJSONWithJSONObject(responseData);
////实例化MyAdapter并传入mList对象
//        initInfo();
//        adapter = new MyAdapter(mList);
////为RecyclerView对象mRecyclerView设置adapter
//        mRecyclerView.setAdapter(adapter);


    }

    //创建构造函数
    public class ContactInfo {
        protected String timeIs = "辣不辣";
        protected String finalChoose = "这一家";
//        protected static final String DISH_NAME_RECOMMEND = "推荐的菜是";
//        protected static final String QUESTION_NAME = "问题是";
//        protected static final String TIME_IS = "时间戳";
//        protected static final String FINAL_CHOOSE="";

        public ContactInfo(String finalChoose, String timeIs) {
            this.timeIs = timeIs;
            this.finalChoose = finalChoose;
            //自己添加
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        //create the viewHolder class
        protected TextView vTitle;
        protected TextView timestamp;

        public ContactViewHolder(View itemView) {
            super(itemView);
            //栏目中名字
            timestamp = itemView.findViewById(R.id.time);
            //小栏目标题
            vTitle = itemView.findViewById(R.id.title_name);
            //可自己选择增添
        }
    }

    public class MyAdapter extends RecyclerView.Adapter
            <MyAdapter.ContactViewHolder> { //MyAdapter类 开始

        //MyAdapter的成员变量contactInfoList, 这里被我们用作数据的来源
        private List<ContactInfo> contactInfoList;

        //MyAdapter的构造器
        public MyAdapter(List<ContactInfo> contactInfoList) {
            this.contactInfoList = contactInfoList;
        }

        //重写3个抽象方法
//onCreateViewHolder()方法 返回我们自定义的 ContactViewHolder对象
        @Override
        public ContactViewHolder onCreateViewHolder
        (ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.activity_history_card_view, parent, false);
            return new ContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder
                (ContactViewHolder holder, int position) {

//contactInfoList中包含的都是ContactInfo类的对象
//通过其get()方法可以获得其中的对象
            ContactInfo ci = contactInfoList.get(position);

//将viewholder中hold住的各个view与数据源进行绑定(bind)
            //定义文本内容
            holder.vTime.setText(ci.timeIs);
            holder.vTitle.setText(ci.finalChoose);
        }

        //此方法返回列表项的数目
        @Override
        public int getItemCount() {
            return contactInfoList.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            protected TextView vTitle;
            protected TextView vTime;

            public ContactViewHolder(View itemView) {
                super(itemView);
                vTime = itemView.findViewById(R.id.time);
                vTitle = itemView.findViewById(R.id.title_name);
            }
        }
    }

    private void initInfo() {
        ContactInfo [] elementArray = new ContactInfo[intRecordNumber];
//        测试数据
//        ContactInfo [] element =new  ContactInfo[10];  之后用数组
        /**
         * 初始化
         */
        for(int i=intRecordNumber-1;i>=0;i++){
            elementArray[i]=new ContactInfo(finalChooseArray[i],timeArray[i]);
            mList.add(elementArray[i]);
        }
//        ContactInfo element1 = new ContactInfo("菜品名", "时间戳");
//        mList.add(element1);
//        ContactInfo element2 = new ContactInfo("菜品名", "时间戳");
//        mList.add(element2);
//        ContactInfo element3 = new ContactInfo("菜品名", "时间戳");
//        mList.add(element3);
//        ContactInfo element4 = new ContactInfo("菜品名", "时间戳");
//        mList.add(element4);
//        ContactInfo element5 = new ContactInfo("菜品名", "时间戳");
//        mList.add(element5);

    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            {
                JSONObject jsonObject = new JSONObject(jsonData);
                /**
                 * 记录个数
                 */
                intRecordNumber = Integer.parseInt(jsonObject.getString("recordNum"));
                /**
                 * 开数组
                 */
                judgeArray=new String[intRecordNumber];
                finalChooseArray=new String[intRecordNumber];
                timeArray=new String[intRecordNumber];

                /**
                 * 构造record数组
                 */
                recordArray = new String[intRecordNumber];
                for (int i = 0; i < intRecordNumber; i++) {
                    recordArray[i] = record + Integer.toString(i);
                }

                Log.d("ABCD",recordArray[0]);//
                /**
                 对recordX赋值,获取finalChoice,judge,data.其中"**X"的是代表有再嵌套内容
                 */
                for (int i = 0; i < intRecordNumber; i++) {
                    String message;
                    String messageTime;
                    message = jsonObject.getString(recordArray[i]);
                    JSONObject messageRecordX = new JSONObject(message);
                    finalChooseArray[i] = messageRecordX.getString("finalchoice");
                    judgeArray[i] = messageRecordX.getString("judge");
                    messageTime = messageRecordX.getString("time");
                    JSONObject messageTimeX = new JSONObject(messageTime);
                    timeArray[i] = messageTimeX.getString("date");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Post并且解析数据，其中--parse---用于赋值到各个Array
     */
    private void sendRequestWithOkHttp() {

        try {
            //读取用户ID和token
            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
            String user_id = pref.getString("id", "0");
            String token = pref.getString("token", "0");
            User user = new User();
            user.setUser_id(user_id);
            user.setToken(token);
//
//                    SharedPreferences.Editor editor = getSharedPreferences("question_answer", MODE_PRIVATE).edit();
//                    editor.putString("id", user_id);
//                    editor.putString("token", token);
//                    editor.apply();
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String toJson = gson.toJson(user);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    http_code = response.code();
//                    /**
//                    解析
//                     */
//                    parseJSONWithJSONObject(responseData);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("onFailure", "fail");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    /**
                     * 异步回调，获取内容
                     */
                    runOnUiThread(()-> {
                        try {
                            responseData = response.body().string();
                            httpCode = response.code();
                            parseJSONWithJSONObject(responseData);
//实例化MyAdapter并传入mList对象
                            initInfo();
                            adapter = new MyAdapter(mList);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(adapter);
//为RecyclerView对象mRecyclerView设置adapter
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class User {
        private String user_id;
        private String token;

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
}
