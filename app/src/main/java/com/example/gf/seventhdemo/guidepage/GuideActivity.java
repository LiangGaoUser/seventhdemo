package com.example.gf.seventhdemo.guidepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.Constants;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private int[] data = {R.drawable.guide01, R.drawable.guide02, R.drawable.guide03};
    private ImageView[] dotViews;  //小圆点
    private ArrayList<ImageView> imageViews;  //引导页
    private Button bn_welcome_start;   //“点击进入”按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp);
        bn_welcome_start = (Button) findViewById(R.id.bn_welcome_start);

        //初始化引导页
        imageViews = new ArrayList<ImageView>();
        ViewPager.LayoutParams vpParams = new ViewPager.LayoutParams();
        for (int i = 0; i < data.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(data[i]);  //为imageView添加图片资源
            imageView.setLayoutParams(vpParams);  //设置子布局imageView在父布局的参数
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);  //图片适配
            imageViews.add(imageView);
        }
        //初始化小圆点
        LinearLayout ll_dots = (LinearLayout) findViewById(R.id.ll_guide_dots);//小圆点的父布局
        dotViews = new ImageView[imageViews.size()];
        for (int i = 0; i < dotViews.length; i++) {
            ImageView aDotView = new ImageView(this);
            aDotView.setImageResource(R.drawable.guide_dot_selector);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {  //默认启动时，选中第一个小圆点
                aDotView.setSelected(true);
            } else {
                aDotView.setSelected(false);
            }
            if (i > 0) {
                llParams.leftMargin = 20;  //设置小圆点之间的间距
            }
            aDotView.setLayoutParams(llParams);
            dotViews[i] = aDotView;
            ll_dots.addView(aDotView);
        }

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(imageViews);
        viewPager.setAdapter(vpAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int position) {
        //设置底部小圆点的状态
        for (int i = 0; i < dotViews.length; i++) {
            if (i == position) {
                dotViews[i].setSelected(true);
            } else {
                dotViews[i].setSelected(false);
            }
            //当引导页滑动到最后一页时，显示“点击进入”按钮
            if (position == dotViews.length - 1) {
                bn_welcome_start.setVisibility(View.VISIBLE);
                //为按钮添加点击事件
                bn_welcome_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(GuideActivity.this,"进入欢迎页面",Toast.LENGTH_LONG).show();
                        setNoGuide();  //设置下次启动不用再次引导
                        Intent intent = new Intent(GuideActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                bn_welcome_start.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void setNoGuide() {
        SharedPreferences preferences = getSharedPreferences(
                Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);// 存入数据
        editor.apply();// 提交修改
    }
}
