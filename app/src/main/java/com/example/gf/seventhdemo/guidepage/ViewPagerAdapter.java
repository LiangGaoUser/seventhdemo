package com.example.gf.seventhdemo.guidepage;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<ImageView> data; //需在ViewPager中显示的ImageView

    public ViewPagerAdapter(List<ImageView> data) {
        this.data = data;
    }

    //获取当前的imageView的数量
    @Override
    public int getCount() {
        return data.size();
    }

    //判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    //从ViewGroup中移除当前imageView
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(data.get(position));
    }

    //初始化position位置的imageView
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = data.get(position);
        container.addView(imageView);
        return imageView;
    }
}
