package com.example.gf.seventhdemo.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gf.seventhdemo.JavaBean.User;
import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.Constants;
import com.example.gf.seventhdemo.homepage.LoginActivity;
import com.example.gf.seventhdemo.homepage.RegisterActivity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginPsdUpdateActivity extends AppCompatActivity {

    private EditText et_psd_old;
    private EditText et_psd_new;
    private EditText et_login_name;
    private String psdOld;
    private String psdNew;
    private String loginName;
    private String loginPsd;
    private String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_psd_update);
        initData();
        et_psd_old = (EditText) findViewById(R.id.et_loginpsd_old);
        et_psd_new = (EditText) findViewById(R.id.et_loginpsd_new);
        et_login_name = (EditText) findViewById(R.id.et_psdupdate_name);
        et_login_name.setText(loginName);
        initTopAction();
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHAREDPREFERENCES_NAME,MODE_PRIVATE);
        loginName = preferences.getString("loginName", "");
        loginPsd = preferences.getString("loginPsd", "");
        loginId = preferences.getString("loginId", "");
    }

    private void initTopAction() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_action1);
        TextView tvUpDate = (TextView) findViewById(R.id.tv_action2);
        //为“取消”添加点击事件
        tvCancel.setText("取消");
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPsdUpdateActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //为“修改”添加点击事件
        tvUpDate.setText("更新");
        tvUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    User user = new User();
                    user.setPassword(psdNew);
                    user.update(loginId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toast("更新成功！");
                                Intent intent = new Intent(LoginPsdUpdateActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Log.d("MSG:", "**" + "数据更新成功!"+"**");
                            } else {
                                toast("更新失败！");
                                Log.d("MSG:", "**" + e.getMessage() + "**");
                            }
                            finish();
                        }
                    });
                }
            }
        });
    }

    //检查输入的信息是否完整、有效
    private boolean checkInput() {
        psdOld = et_psd_old.getText().toString();
        psdNew = et_psd_new.getText().toString();
        if (TextUtils.isEmpty(psdOld)) {
            toast("请输入新密码！");
            et_psd_old.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(psdNew)) {
            toast("密码不能为空！");
            et_psd_new.requestFocus();
            return false;
        }
        if (!psdOld.equals(loginPsd)) {
            toast("原密码输入错误！");
            et_psd_old.requestFocus();
            return false;
        }
        return true;
    }

    private void toast(String msg) {
        Toast.makeText(LoginPsdUpdateActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
