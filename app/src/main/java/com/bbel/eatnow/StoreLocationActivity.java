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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        Intent intent = getIntent();

        initWidgets();
        mapView.setChoose("40", R.raw.sun_map);
    }


    private void initWidgets() {
        mapView = findViewById(R.id.map);
    }

}
