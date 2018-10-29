package com.example.gf.seventhdemo.homepage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.gf.seventhdemo.CalculatorActivity;
import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.TopBar;
import com.example.gf.seventhdemo.map.MapActivity;
import com.example.gf.seventhdemo.memo.MemoActivity;
import com.example.gf.seventhdemo.phonebook.PhoneBookActivity;
import com.example.gf.seventhdemo.settings.SettingsActivity;
import com.example.gf.seventhdemo.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private List<HomePageTab> tabList;      //标签集
    private TopBar topBar;

    //首页上的标签
    private String[] tabName = {"记事本", "联系人", "计算器", "天 气", "地 图", "设 置"};
    private int[] tabIcon = {R.drawable.ic_memo, R.drawable.ic_phonebook,
            R.drawable.ic_calculator, R.drawable.ic_weather,
            R.drawable.ic_map, R.drawable.ic_settings};
    private Class<?>[] tabCls = {MemoActivity.class, PhoneBookActivity.class, CalculatorActivity.class, WeatherActivity.class, MapActivity.class, SettingsActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        topBar = new TopBar(HomePageActivity.this);
        initTopBar();//设置标题栏

        tabList = new ArrayList<HomePageTab>();
        initTabList();      //初始化标签集

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_homepage);
        HomePageAdapter adapter = new HomePageAdapter(tabList);
        //创建一个包含3列的网格布局管理器对象
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        //使用适配器对象adapter为recyclerView加载数据
        recyclerView.setAdapter(adapter);
    }

    private void initTabList() {
        for (int i = 0; i < tabIcon.length; i++) {
            HomePageTab tab = new HomePageTab(tabName[i], tabIcon[i], tabCls[i]);
            tabList.add(tab);
        }
    }

    private void initTopBar() {
        topBar.setTitleName("首  页");
        topBar.setMenuVisible(View.INVISIBLE);
        topBar.setBnBackVisible(View.INVISIBLE);
    }

}
