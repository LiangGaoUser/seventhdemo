package com.example.gf.seventhdemo.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.Constants;
import com.example.gf.seventhdemo.common.TopBar;
import com.example.gf.seventhdemo.homepage.LoginActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        topBar = new TopBar(SettingsActivity.this);
        initTopBar();//设置标题栏
        findViewById(R.id.tr_about_app).setOnClickListener(this);
        findViewById(R.id.tr_login_psd_update).setOnClickListener(this);
        findViewById(R.id.tr_exit_login).setOnClickListener(this);
    }

    private void initTopBar() {
        topBar.setTitleName("设 置");
        topBar.setMenuVisible(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.tr_about_app://关于
                intent.setClass(SettingsActivity.this,AboutActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tr_exit_login://退出登录
                SharedPreferences preferences = getSharedPreferences(Constants.SHAREDPREFERENCES_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor =preferences.edit();
                editor.putBoolean("ifLogin",false);
                editor.putString("loginName","");
                editor.putString("loginPsd","");
                editor.putString("loginId","");
                editor.apply();
                intent.setClass(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tr_login_psd_update://修改密码
                intent.setClass(SettingsActivity.this,LoginPsdUpdateActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
