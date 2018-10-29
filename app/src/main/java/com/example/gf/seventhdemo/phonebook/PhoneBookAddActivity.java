package com.example.gf.seventhdemo.phonebook;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gf.seventhdemo.JavaBean.Linkman;
import com.example.gf.seventhdemo.R;

public class PhoneBookAddActivity extends AppCompatActivity {

    private EditText et_linkman_name;
    private EditText et_phone_number;
    private String linkmanName;
    private String linkmanPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_add);

        et_linkman_name = (EditText)findViewById(R.id.et_linkman_name);
        et_phone_number = (EditText)findViewById(R.id.et_phone_number);
        initTopAction();
    }

    private void initTopAction() {
        TextView tvSelectAll = (TextView) findViewById(R.id.tv_action1);
        TextView tvDel = (TextView) findViewById(R.id.tv_action2);
        //为“取消”添加点击事件
        tvSelectAll.setText("取消");
        tvSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //为“保存”添加点击事件
        tvDel.setText("保存");
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    //显示“确认保存”的对话框
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PhoneBookAddActivity.this);
                    dialog.setTitle("确认");
                    dialog.setMessage("是否保存？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveInfo();//将数据保存到数据库中
                            finish();//销毁当前活动
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    //检查输入的信息是否完整、有效
    private boolean checkInput() {
        linkmanName = et_linkman_name.getText().toString();
        linkmanPhoneNumber = et_phone_number.getText().toString();
        if (TextUtils.isEmpty(linkmanName)) {
            Toast.makeText(PhoneBookAddActivity.this, "姓名不能为空！", Toast.LENGTH_LONG).show();
            et_linkman_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(linkmanPhoneNumber)) {
            Toast.makeText(PhoneBookAddActivity.this, "电话号码不能为空！", Toast.LENGTH_LONG).show();
            et_phone_number.requestFocus();
            return false;
        }
        return true;
    }

    private void saveInfo(){
        Linkman linkman = new Linkman();
        linkman.setName(linkmanName);
        linkman.setPhoneNumber(linkmanPhoneNumber);
        boolean flag=linkman.save();
        if (flag) {
            Toast.makeText(PhoneBookAddActivity.this, "保存成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PhoneBookAddActivity.this, "保存失败！", Toast.LENGTH_LONG).show();
        }
    }
}
