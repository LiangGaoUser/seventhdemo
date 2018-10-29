package com.example.gf.seventhdemo.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gf.seventhdemo.JavaBean.Memo;
import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.BaseDBHelper;
import com.example.gf.seventhdemo.common.TopBar;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    public static SQLiteDatabase database;//数据库对象
    private List<Memo> memoList;  //记事本列表
    private TopBar topBar;  //标题栏
    private RecyclerView recyclerView;
    private MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        topBar = new TopBar(MemoActivity.this);
        initTopBar();//设置标题栏
        initTopAction();//设置删除操作
        database = getDatabase();//获得数据库对象
        showContent();  //显示记事本列表

        //为“添加”按钮点击点击事件
        FloatingActionButton fbn_memo_add = (FloatingActionButton) findViewById(R.id.fbn_memo_add);
        fbn_memo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoActivity.this, MemoAddActivity.class);
                startActivity(intent);//启动MemoAddActivity
            }
        });

    }

    //从数据库表t_memo读取数据
    private void loadMemo() {
        memoList = new ArrayList<Memo>();
        Memo aMemo;
        Cursor cursor = database.query("t_memo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String memo_id = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
                String memo_createTime = cursor.getString(cursor.getColumnIndex("memo_createtime"));
                String memo_content = cursor.getString(cursor.getColumnIndex("memo_content"));
                String memo_title = cursor.getString(cursor.getColumnIndex("memo_title"));
                aMemo = new Memo(memo_title, memo_content, memo_createTime, memo_id);
                Log.i("msg","***"+aMemo.getId());
                memoList.add(aMemo);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initTopBar() {
        topBar.setTitleName("记事本");
        Button bn_memo_del = topBar.getBn_menu();
        bn_memo_del.setBackgroundResource(R.drawable.bn_memo_del_selector);
        //“删除”按钮的点击事件
        bn_memo_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memoList.size() > 0) {
                    adapter.showCheckbox(true);//显示删除复选框
                    findViewById(R.id.memo_top_bar).setVisibility(View.GONE);
                    findViewById(R.id.memo_del_action).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showContent() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_meno);
        loadMemo();//从数据库表t_memo中获取数据
        if (memoList.size() <= 0) {//若表t_memo为空，则显示记事本为空
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_memo_empty);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new MemoAdapter(memoList,MemoActivity.this);
            //定义布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //使用适配器对象adapter为recyclerView加载数据
            recyclerView.setAdapter(adapter);
        }
    }

    private void initTopAction() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_action1);
        TextView tvDel = (TextView) findViewById(R.id.tv_action2);
        //为“取消”添加点击事件
        tvCancel.setText("取消");
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showCheckbox(false);//显示删除复选框
                findViewById(R.id.memo_top_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.memo_del_action).setVisibility(View.GONE);
            }
        });
        //为“删除”添加点击事件
        tvDel.setText("删除");
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示“确认保存”的对话框
                AlertDialog.Builder dialog = new AlertDialog.Builder(MemoActivity.this);
                dialog.setTitle("确认");
                dialog.setMessage("是否删除？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Memo> delList = adapter.getDelList();
                        if (delList.size() > 0) {
                            int is_succeed;
                            for (Memo aMemo : delList) {
                                String memo_id = aMemo.getId();
                                is_succeed = database.delete("t_memo", "id = ? ", new String[]{memo_id});
                                if (is_succeed > 0) {
                                    memoList.remove(aMemo);
                                }
                            }
                        }
                        adapter.showCheckbox(false);
                        findViewById(R.id.memo_top_bar).setVisibility(View.VISIBLE);
                        findViewById(R.id.memo_del_action).setVisibility(View.GONE);
                        if (memoList.size() == 0) {
                            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_memo_empty);
                            linearLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
    }

    //获得数据库对象
    private SQLiteDatabase getDatabase() {
        BaseDBHelper dbHelper = new BaseDBHelper(MemoActivity.this, "demo_db", null, 1);
        return dbHelper.getReadableDatabase();
    }

    //活动由停止状态变为运行状态之前调用此方法
    @Override
    protected void onRestart() {
        super.onRestart();
        //初始化布局
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_memo_empty);
        linearLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);
        findViewById(R.id.memo_top_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.memo_del_action).setVisibility(View.GONE);
        showContent();
    }
}
