package com.bbel.eatnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bbel.eatnow.customview.MapView;

import java.lang.ref.WeakReference;

import static com.baidu.location.g.g.v;

public class StoreLocationActivity extends BaseActivity {

    MapView mapView;
    int[] mapIds = new int[]{R.raw.ding_xiang_map, R.raw.tian_jiao_yuan_map, R.raw.jing_yuan_map,
        R.raw.rose_map, R.raw.he_feng_map, R.raw.sun_map};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        Button btn = findViewById(R.id.btn_home);

        Toast.makeText(this, "抱歉，带我去功能尚未开发完成╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        String id = intent.getStringExtra("idRest");
        String map_id = intent.getStringExtra("canteenid");
        initWidgets();
        mapView.setChoose(id, mapIds[Integer.valueOf(map_id)]);


        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)

            {

                Intent intentToHomePage = new Intent(StoreLocationActivity.this,MainActivity.class);
                startActivity(intentToHomePage);


            }

        });

    }


    private void initWidgets() {
        mapView = findViewById(R.id.map);
    }




}
