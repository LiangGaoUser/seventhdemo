package com.example.gf.seventhdemo.homepage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.example.gf.seventhdemo.phonebook.PhoneBookAddActivity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_psd;
    private EditText et_psd_cfm;
    private String name;
    private String password;
    private String passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = (EditText)findViewById(R.id.et_register_name);
        et_psd = (EditText)findViewById(R.id.et_register_psd);
        et_psd_cfm =(EditText)findViewById(R.id.et_register_psd_cfm);
        initTopAction();
    }

    private void initTopAction() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_action1);
        TextView tvDel = (TextView) findViewById(R.id.tv_action2);
        //为“取消”添加点击事件
        tvCancel.setText("取消");
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //为“保存”添加点击事件
        tvDel.setText("注册");
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    User user = new User();
                    user.setName(name);
                    user.setPassword(password);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String userID, BmobException e) {
                            if(e==null){
                                toast("注册成功！");
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                Log.d("MSG:","**"+"添加数据成功，返回objectId为："+ userID+"**");
                            }else{
                                toast("注册失败！");
                                Log.d("MSG:","**"+e.getMessage()+"**");
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
        name = et_name.getText().toString();
        password = et_psd.getText().toString();
        passwordConfirm = et_psd_cfm.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请输入用户名！");
            et_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            toast("密码不能为空！");
            et_psd.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            toast("请输入确认密码！");
            et_psd_cfm.requestFocus();
            return false;
        }
        if(!password.equals(passwordConfirm)){
            toast("两次输入的密码不一致！");
            et_psd.requestFocus();
            return false;
        }
        return true;
    }

    private void toast(String msg){
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
