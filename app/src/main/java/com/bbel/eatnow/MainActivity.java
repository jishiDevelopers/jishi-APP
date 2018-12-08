package com.bbel.eatnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    DrawerLayout mDrawerLayout;
    Button button;

    MapView mapView;
    BaiduMap mBaiduMap;

    LHanlder lHanlder;

    LocationClient mLocationClient;
    LatLng currentLocation;
    BitmapDescriptor bitmapDescriptor;
    Overlay myLocationOverlay;
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);

        lHanlder = new LHanlder();

        initWidgets();
        requestPermission();
    }


    private void initWidgets() {
        mapView = findViewById(R.id.mmap);
        mBaiduMap = mapView.getMap();

        getStoreLocation();

        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.location);

        Button button = findViewById(R.id.main_button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StoreLocationActivity.class));
            }
        });

        button = findViewById(R.id.main_button_test);
        button.setOnClickListener((v) -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", currentLocation.latitude);
            bundle.putDouble("longitude", currentLocation.longitude);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        lHanlder.removeCallbacksAndMessages(null);
    }


    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            currentLocation = ll;
            if (isFirst) {
                moveTo(ll);
                isFirst = false;
            }
            int errorCode = bdLocation.getLocType();
            Log.d("LocationTest", "receive  " + errorCode);
            Log.d("LocationTest", bdLocation.getLatitude() + "  " + bdLocation.getLongitude());
            MarkerOptions options = new MarkerOptions().position(ll).icon(bitmapDescriptor);
            if (myLocationOverlay != null) {
                myLocationOverlay.remove();
            }
            myLocationOverlay = mBaiduMap.addOverlay(options);
        }
    }


    private void moveTo(LatLng LL) {
        MapStatus mMapStatus = new MapStatus.Builder().target(LL).zoom(19).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "允许此权限以获得更好的使用体验", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    mLocationClient.restart();
                    requestLocation();
                } else {
                    Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            mLocationClient.restart();
            requestLocation();
        }
    }


    private void requestLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置百度坐标类型 地图坐标类型为bd0911
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setScanSpan(5000);
//        option.setLocationNotify(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    private void getStoreLocation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String url = "http://193.112.6.8/map_request";
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = response.body().string();
                    message.arg1 = response.code();
                    lHanlder.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private void handleResponse(String receiveData) {
        try {
            List<StoreLocation> storeLocations;
            String responseData = receiveData;
            Gson gson = new Gson();
            storeLocations = gson.fromJson(responseData, new TypeToken<List<StoreLocation>>() {}.getType());

            for (StoreLocation storeLocation : storeLocations) {
                LatLng latLng = new LatLng(Double.valueOf(storeLocation.latitude), Double.valueOf(storeLocation.longitude));
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
                OverlayOptions overlayOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmapDescriptor);
                mBaiduMap.addOverlay(overlayOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class StoreLocation {

        String name;

        String longitude;

        String latitude;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }

    private class LHanlder extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String responseData = (String) msg.obj;
                    if (msg.arg1 == 202) {
                        handleResponse(responseData);
                    } else {
                        Log.d("getLocation", "get data failed");
                    }
                    break;
                default:
                    break;
            }

        }
    }


}

