package com.example.gf.seventhdemo.memo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.TopBar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoAddActivity extends AppCompatActivity {

    private TopBar topBar;
    private EditText et_memo_title;
    private EditText et_memo_content;
    private String memo_title;
    private String memo_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);

        topBar = new TopBar(MemoAddActivity.this);
        initTopBar();//设置标题栏

        et_memo_title = (EditText) findViewById(R.id.et_memo_title);
        et_memo_content = (EditText) findViewById(R.id.et_memo_content);
    }

    private void initTopBar() {
        topBar.setTitleName("");
        Button bn_memo_save = topBar.getBn_menu();
        bn_memo_save.setBackgroundResource(R.drawable.bn_memo_save_selector);
        //“保存”按钮的点击事件
        bn_memo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    //显示“确认保存”的对话框
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MemoAddActivity.this);
                    dialog.setTitle("确认");
                    dialog.setMessage("是否保存？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveMemo();//将数据保存到数据库中
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
        memo_title = et_memo_title.getText().toString();
        memo_content = et_memo_content.getText().toString();
        if (TextUtils.isEmpty(memo_title)) {
            Toast.makeText(MemoAddActivity.this, "标题不能为空！", Toast.LENGTH_LONG).show();
            et_memo_title.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(memo_content)) {
            Toast.makeText(MemoAddActivity.this, "内容不能为空！", Toast.LENGTH_LONG).show();
            et_memo_content.requestFocus();
            return false;
        }
        return true;
    }

    //将数据保存到memo_data文件中
    private void saveMemo() {
        //获取当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年mm月dd日hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        //格式化需保存的数据
        String memo_createTime = simpleDateFormat.format(date);

        SQLiteDatabase database = MemoActivity.database;
        ContentValues values = new ContentValues();
        values.put("memo_createtime", memo_createTime);
        values.put("memo_content", memo_content);
        values.put("memo_title", memo_title);
        long flag = database.insert("t_memo", null, values);
        if (flag > 0) {
            Toast.makeText(MemoAddActivity.this, "保存成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MemoAddActivity.this, "保存失败！", Toast.LENGTH_LONG).show();
        }
    }
}

