package com.bbel.eatnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bbel.eatnow.customview.MapView;

import java.lang.ref.WeakReference;

public class StoreLocationActivity extends BaseActivity {

    MapView mapView;
    int[] mapIds = new int[]{R.raw.ding_xiang_map, R.raw.tian_jiao_yuan_map, R.raw.jing_yuan_map,
        R.raw.rose_map, R.raw.he_feng_map, R.raw.sun_map};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        Intent intent = getIntent();
        String id = intent.getStringExtra("idRest");
        String map_id = intent.getStringExtra("canteenid");
        initWidgets();
        mapView.setChoose(id, mapIds[Integer.valueOf(map_id)]);
    }


    private void initWidgets() {
        mapView = findViewById(R.id.map);
    }

}
