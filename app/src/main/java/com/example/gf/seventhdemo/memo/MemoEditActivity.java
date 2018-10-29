package com.example.gf.seventhdemo.memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MemoEditActivity extends AppCompatActivity {

    private TopBar topBar;
    private EditText et_memo_title;
    private EditText et_memo_content;
    private String memo_title;
    private String memo_content;
    private String memo_time;
    private String memo_id;


    //利用 Intent 向当前活动传递数据
    public static void actionStart(Context context, String id, String title, String content, String time) {
        Intent intent = new Intent(context, MemoEditActivity.class);
        intent.putExtra("memo_id", id);
        intent.putExtra("memo_time", time);
        intent.putExtra("memo_title", title);
        intent.putExtra("memo_content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        //获得从 MemoActivity 传递过来的信息
        Intent intent = getIntent();
        memo_id = intent.getStringExtra("memo_id");
        memo_time = intent.getStringExtra("memo_time");
        memo_content = intent.getStringExtra("memo_content");
        memo_title = intent.getStringExtra("memo_title");

        topBar = new TopBar(MemoEditActivity.this);
        initTopBar();//设置标题栏

        et_memo_title = (EditText) findViewById(R.id.et_memo_title_edit);
        et_memo_title.setText(memo_title);
        et_memo_content = (EditText) findViewById(R.id.et_memo_content_edit);
        et_memo_content.setText(memo_content);
        et_memo_title.requestFocus();
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MemoEditActivity.this);
                    dialog.setTitle("确认");
                    dialog.setMessage("是否更新？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateMemo();//将数据保存到数据库中
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

    //将数据保存到memo_data文件中
    private void updateMemo() {
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
        long flag = database.update("t_memo", values, "id = ? ", new String[]{memo_id});
        if (flag > 0) {
            Toast.makeText(MemoEditActivity.this, "更新成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MemoEditActivity.this, "更新失败！", Toast.LENGTH_LONG).show();
        }
    }

    //检查输入的信息是否完整、有效
    private boolean checkInput() {
        memo_title = et_memo_title.getText().toString();
        memo_content = et_memo_content.getText().toString();
        if (TextUtils.isEmpty(memo_title)) {
            Toast.makeText(MemoEditActivity.this, "标题不能为空！", Toast.LENGTH_LONG).show();
            et_memo_title.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(memo_content)) {
            Toast.makeText(MemoEditActivity.this, "内容不能为空！", Toast.LENGTH_LONG).show();
            et_memo_content.requestFocus();
            return false;
        }
        return true;
    }
}
