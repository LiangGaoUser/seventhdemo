package com.example.gf.seventhdemo.phonebook;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gf.seventhdemo.JavaBean.Linkman;
import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.TopBar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private List<Linkman> linkmanList;  //联系人列表
    private TopBar topBar;
    private RecyclerView recyclerView;
    private PhoneBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        recyclerView = (RecyclerView) findViewById(R.id.rv_phonebook);
        topBar = new TopBar(PhoneBookActivity.this);

        LitePal.getDatabase();//获得SQLiteDatabase对象
        initTopBar();//设置标题栏
        initTopAction();//设置删除操作
        showView();//显示信息

        //为“添加”按钮点击点击事件
        FloatingActionButton fbn_memo_add = (FloatingActionButton) findViewById(R.id.fbn_phonebook_add);
        fbn_memo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneBookActivity.this, PhoneBookAddActivity.class);
                startActivity(intent);//启动MemoAddActivity
            }
        });
    }

    private void initTopBar() {
        topBar.setTitleName("联系人");
        Button bn = topBar.getBn_menu();
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });
    }

    private void initLinkmanList() {
        linkmanList = new ArrayList<Linkman>();
        linkmanList = LitePal.findAll(Linkman.class);
    }

    private void showView() {
        initLinkmanList(); //初始化联系人信息
        adapter = new PhoneBookAdapter(linkmanList, PhoneBookActivity.this);
        if (linkmanList.size() == 0) {
            findViewById(R.id.ll_phonebook_empty).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            //定义布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //使用适配器对象adapter为recyclerView加载数据
            recyclerView.setAdapter(adapter);
        }
    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.phonebook_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示弹出菜单
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_phonebook_del:
                if (linkmanList.size() > 0) {
                    adapter.showCheckbox(true);
                    findViewById(R.id.phonebook_top_bar).setVisibility(View.GONE);
                    findViewById(R.id.phonebook_del_action).setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "没有联系人信息，无法删除！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_phonebook_sny:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    readContacts();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        findViewById(R.id.ll_phonebook_empty).setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.phonebook_top_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.phonebook_del_action).setVisibility(View.GONE);
        showView();
    }

    private void initTopAction() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_action1);
        TextView tvDel = (TextView) findViewById(R.id.tv_action2);
        //为“删除”添加点击事件
        tvCancel.setText("取消");
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showCheckbox(false);//显示删除复选框
                findViewById(R.id.phonebook_top_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.phonebook_del_action).setVisibility(View.GONE);
            }
        });
        //为“删除”添加点击事件
        tvDel.setText("删除");
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Linkman> delList = adapter.getDelList();
                if (delList.size() > 0) {
                    //显示“确认删除”的对话框
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PhoneBookActivity.this);
                    dialog.setTitle("确认");
                    dialog.setMessage("是否删除？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int is_succeed;
                            for (Linkman linkman : delList) {
                                int linkmanId = linkman.getId();
                                is_succeed = LitePal.delete(Linkman.class, linkmanId);
                                if (is_succeed > 0) {
                                    linkmanList.remove(linkman);
                                }
                            }
                            adapter.showCheckbox(false);
                            findViewById(R.id.phonebook_top_bar).setVisibility(View.VISIBLE);
                            findViewById(R.id.phonebook_del_action).setVisibility(View.GONE);
                            if (linkmanList.size() == 0) {
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_phonebook_empty);
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
            }
        });
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String linkmanName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Linkman linkman = new Linkman();
                    linkman.setName(linkmanName);
                    linkman.setPhoneNumber(phoneNumber);
                    boolean flag = linkman.save();
                    if (flag) {
                        linkmanList.add(linkman);
                        if (linkmanList.size() == 1) {
                            findViewById(R.id.ll_phonebook_empty).setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            //定义布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            //使用适配器对象adapter为recyclerView加载数据
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "你拒绝了该权限！", Toast.LENGTH_LONG).show();
                }
        }
    }
}
