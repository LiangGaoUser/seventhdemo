package com.example.gf.seventhdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.gf.seventhdemo.common.TopBar;

public class CalculatorActivity extends AppCompatActivity {

    private TopBar topBar;
    private Button bn_topbar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        topBar = new TopBar(CalculatorActivity.this);
        initTopBar();//设置标题栏
    }

    private void initTopBar() {
        topBar.setTitleName("计算器");
        topBar.setMenuVisible(View.INVISIBLE);
    }
}
