package com.bbel.eatnow;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bbel.eatnow.HistoryActivity.ContactInfo.FINAL_CHOOSE;
import static com.bbel.eatnow.HistoryActivity.ContactInfo.QUESTION_ANSWER;
import static com.bbel.eatnow.HistoryActivity.ContactInfo.DISH_NAME_RECOMMEND;
import static com.bbel.eatnow.HistoryActivity.ContactInfo.QUESTION_NAME;

public class HistoryActivity extends AppCompatActivity {
    private MyAdapter adapter;
    private int intRecordNumber=5;

    private String [] dishName=new String[intRecordNumber];
    private String [] questionId=new String[intRecordNumber];
    private String [] questionAnswer=new String[intRecordNumber];
    private String [] dishsName=new String[intRecordNumber];
    private String [] finalChoose=new String[intRecordNumber];
    List<ContactInfo> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
//实例化MyAdapter并传入mList对象
        initInfo();
        adapter = new MyAdapter(mList);
//为RecyclerView对象mRecyclerView设置adapter
        mRecyclerView.setAdapter(adapter);


    }
    //创建构造函数
    public class ContactInfo {
        protected String questionList = "辣不辣";
        protected String questionAnswer = "y,s,y";
        protected String dishList = "balabala";
        protected String finalChoose ="这一家";
        protected String titleDishName="这一家";
        protected static final String DISH_NAME_RECOMMEND = "推荐的菜是";
        protected static final String QUESTION_NAME = "问题是";
        protected static final String QUESTION_ANSWER = "勾选的答案是";
        protected static final String FINAL_CHOOSE="最后选的是";

        public ContactInfo(String questionList, String questionAnswer, String dishList,String finalChoose,String titleDishName) {
            this.questionList = questionList;
            this.questionAnswer = questionAnswer;
            this.dishList = dishList;
            this.finalChoose =finalChoose;
            this.titleDishName=titleDishName;
            //自己添加
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        //create the viewHolder class
        protected TextView vDishRecommend;
        protected TextView vQuestionName;
        protected TextView vQuestionAnswer;
        protected TextView vTitle;
        protected TextView vFinalChoose;

        public ContactViewHolder(View itemView) {
            super(itemView);
            //栏目中名字
            vDishRecommend = itemView.findViewById(R.id.text_dish_list);

            vQuestionName = itemView.findViewById(R.id.text_question_list);
            vQuestionAnswer = itemView.findViewById(R.id.text_question_answer);
            vFinalChoose=itemView.findViewById(R.id.text_final_choose);
            //小栏目标题
            vTitle = itemView.findViewById(R.id.title);

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
                    inflate(R.layout.activity_history_card_view,parent,false);
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
            holder.vDishRecommend.setText(DISH_NAME_RECOMMEND +ci.questionList);
            holder.vQuestionName.setText(QUESTION_NAME +ci.questionAnswer);
            holder.vQuestionAnswer.setText(QUESTION_ANSWER +ci.dishList);
            holder.vFinalChoose.setText(FINAL_CHOOSE+ci.finalChoose);
            holder.vTitle.setText(ci.titleDishName);
        }

        //此方法返回列表项的数目
        @Override
        public int getItemCount() {
            return contactInfoList.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            //create the viewHolder class

            protected TextView vDishRecommend;
            protected TextView vQuestionName;
            protected TextView vQuestionAnswer;
            protected TextView vTitle;
            protected TextView vFinalChoose;
            public ContactViewHolder(View itemView) {
                super(itemView);
                vDishRecommend = itemView.findViewById(R.id.text_dish_list);
                vQuestionName = itemView.findViewById(R.id.text_question_list);
                vQuestionAnswer = itemView.findViewById(R.id.text_question_answer);
                vFinalChoose=itemView.findViewById(R.id.text_final_choose);
                vTitle = itemView.findViewById(R.id.title);
            }

        }
    }
    private void initInfo() {

//        测试数据
//        ContactInfo [] element =new  ContactInfo[10];  之后用数组
        ContactInfo element1 = new ContactInfo("小明", "西门", "feverdg@icloud.com","这一家","烤腿饭");
        mList.add(element1);
        ContactInfo element2 = new ContactInfo("小红", "南宫", "146793455@icloud.com","这一家","烤腿饭");
        mList.add(element2);
        ContactInfo element3 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com","这一家","烤腿饭");
        mList.add(element3);
        ContactInfo element4 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com","这一家","烤腿饭");
        mList.add(element4);
        ContactInfo element5 = new ContactInfo("小九九", "欧阳", "17987453@icloud.com","这一家","烤腿饭");
        mList.add(element5);

    }
//    private void parseJSONWithJSONObject(String jsonData) {
//        try {
//            {
//                JSONObject jsonObject = new JSONObject(jsonData);
//                intRecordNumber = Integer.parseInt(jsonObject.getString("recordId"));
//                idRecord = jsonObject.getString("idRecord");
//                for (int i = 0; i < intDisuhNumber; i++) {
//                    String message;
//                    message = jsonObject.getString(dishNumArray[i]);
//                    JSONObject messageDish = new JSONObject(message);
//                    restaurantArray[i] = messageDish.getString("RestName");
//                    dishesArray[i] = messageDish.getString("dishName");
//                    canteensArray[i] = messageDish.getString("canteen");
//                    dishId[i] = messageDish.getString("idDish");
//                    restaurantId[i] = messageDish.getString("idRest");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}