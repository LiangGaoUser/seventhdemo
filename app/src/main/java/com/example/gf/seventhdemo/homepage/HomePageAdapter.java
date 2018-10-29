package com.example.gf.seventhdemo.homepage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gf.seventhdemo.R;

import java.util.List;


public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyViewHolder> {

    private List<HomePageTab> tabList;  //标签集

    // 内部类：用于描述RecyclerView子项的布局
    class MyViewHolder extends RecyclerView.ViewHolder {
        View tabView;   //标签布局
        TextView tabName;
        ImageView tabIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tabView = itemView;
            tabName = (TextView) itemView.findViewById(R.id.tv_homepage_tabname);
            tabIcon = (ImageView) itemView.findViewById(R.id.img_homepage_tabicon);
        }
    }

    // 构造方法
    public HomePageAdapter(List<HomePageTab> tabList) {
        this.tabList = tabList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_homepage_tab, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        //为标签布局添加点击事件
        viewHolder.tabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                HomePageTab tab = tabList.get(position);
//                Toast.makeText(v.getContext(), "点击了：" + tab.getName(), Toast.LENGTH_LONG).show();
                //使用显式Intent启动活动
                showNewActivity(v.getContext(), tab.getCls());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        // 通过参数 position 获得 RecyclerView 当前子项的实例
        HomePageTab tab = tabList.get(position);
        myViewHolder.tabName.setText(tab.getName());
        myViewHolder.tabIcon.setImageResource(tab.getIconId());
    }

    @Override
    public int getItemCount() {
        return tabList.size();
    }

    private void showNewActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        ((HomePageActivity) context).finish();
    }
}
