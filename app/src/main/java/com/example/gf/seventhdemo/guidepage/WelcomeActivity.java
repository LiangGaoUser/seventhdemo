package com.example.gf.seventhdemo.guidepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.Constants;
import com.example.gf.seventhdemo.homepage.HomePageActivity;
import com.example.gf.seventhdemo.homepage.LoginActivity;

import java.lang.ref.WeakReference;

import cn.bmob.v3.Bmob;

public class WelcomeActivity extends AppCompatActivity {

    private boolean isFirstIn = false;  //是否启动引导页标志: true(启动); false(不启动)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MyHandler mHandler;
        mHandler = new MyHandler(WelcomeActivity.this);

        isFirstIn = getGuideFlag();
        // 判断程序是否是第一次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        Message message = new Message();
        if (isFirstIn) {
            //跳转到GuideActivity
            message.what = Constants.GO_GUIDE;
            mHandler.sendMessage(message);
        } else {
            //显示欢迎页
            setContentView(R.layout.activity_welcome);
            message.what = Constants.GO_HOMEPAGE;
            //3秒后跳转到HomePageActivity
            mHandler.sendMessageDelayed(message, Constants.KEEP_TIME);
        }
    }

    private boolean getGuideFlag() {
        boolean flag;
        // 从SharedPreferences中读取据
        SharedPreferences preferences = getSharedPreferences(
                Constants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        flag = preferences.getBoolean("isFirstIn", true);
        return flag;
    }

    private void goHomePage() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        boolean ifLogin = sharedPreferences.getBoolean("ifLogin", false);
        //初始化Bomb
        Bmob.initialize(this, "f22faad4328db80ce680513ff8d2220a");
        //判断是否已登陆
        if (ifLogin) {
            Intent intent = new Intent(WelcomeActivity.this, HomePageActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void goGuide() {
        Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    static class MyHandler extends Handler {
        WeakReference<WelcomeActivity> mWeakReference;

        public MyHandler(WelcomeActivity activity) {
            mWeakReference = new WeakReference<WelcomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WelcomeActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constants.GO_HOMEPAGE:
                        activity.goHomePage();
                        break;
                    case Constants.GO_GUIDE:
                        activity.goGuide();
                        break;
                }
            }
        }
    }
}
