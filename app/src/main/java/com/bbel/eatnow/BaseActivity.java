package com.bbel.eatnow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.bbel.eatnow.utils.ActivityCollector;

public class BaseActivity extends AppCompatActivity {

    String SERVER_URL = "http://193.112.6.8";

    private ForceOfflineReceiver forceOfflineReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.bbel.eatnow.FORCE_OFFLINE");
        forceOfflineReceiver = new ForceOfflineReceiver();
        registerReceiver(forceOfflineReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (forceOfflineReceiver != null) {
            unregisterReceiver(forceOfflineReceiver);
            forceOfflineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    class ForceOfflineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("登录已过期");
            builder.setMessage("请重新登陆");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: 2018/12/19 清除用户数据
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putString("id", "-1");
                    editor.putString("token", "0");
                    editor.apply();
                    ActivityCollector.finishAll();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
//            builder.setPositiveButton("确定", )
            builder.show();
        }
    }

}
