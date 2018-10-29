package com.example.gf.seventhdemo.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_name;
    private EditText et_login_psd;
    private ImageView img_psd_visible;
    private TextView tv_register;
    private Button bn_login;
    private String loginName;
    private String loginPassword;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_name = (EditText) findViewById(R.id.et_login_name);
        et_login_psd = (EditText) findViewById(R.id.et_login_password);
        img_psd_visible = (ImageView) findViewById(R.id.img_psd_visible);
        tv_register = (TextView) findViewById(R.id.tv_register);
        bn_login = (Button) findViewById(R.id.bn_login);

        img_psd_visible.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        bn_login.setOnClickListener(this);
    }

    //检查输入的信息是否完整、有效
    private boolean checkInput() {
        loginName = et_login_name.getText().toString();
        loginPassword = et_login_psd.getText().toString();
        if (TextUtils.isEmpty(loginName)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_LONG).show();
            et_login_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(loginPassword)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
            et_login_psd.requestFocus();
            return false;
        }
        return true;
    }

    private void toast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_psd_visible://设置密码的可见性
                if (!passwordVisible) {

                } else {

                }
                passwordVisible = !passwordVisible;
                et_login_psd.postInvalidate();
                break;
            case R.id.tv_register://注册
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bn_login://登录
                if (checkInput()) {
                    BmobQuery bmobQuery = new BmobQuery("User");
                    bmobQuery.addWhereEqualTo("name", loginName);
                    bmobQuery.addWhereEqualTo("password", loginPassword);
                    bmobQuery.findObjectsByTable(new QueryListener<JSONArray>() {
                        @Override
                        public void done(JSONArray jsonArray, BmobException e) {
                            if (e == null) {
                                Log.d("登录", "查询成功：" + jsonArray.toString());
                                if (jsonArray.length() > 0) {
                                    try {
                                        JSONObject object = jsonArray.getJSONObject(0);
                                        //登录成功后，记录下登录状态
                                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("ifLogin", true);
                                        editor.putString("loginName",object.optString("name"));//将getstring改为opstring就不会报错
                                        editor.putString("loginPsd",object.getString("password"));
                                        editor.putString("loginId",object.getString("objectId"));
                                        editor.apply();
                                        //打开主页面
                                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                                else{
                                    toast("用户名或密码不正确！");
                                    et_login_name.requestFocus();
                                }
                            } else {
                                Log.d("登录", "查询失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                }
                break;
        }
    }
}
