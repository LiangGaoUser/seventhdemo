package com.example.gf.seventhdemo.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.homepage.HomePageActivity;


public class TopBar {
    private TextView tv_title;
    private Button bn_back;
    private Button bn_menu;
    private  final Activity activity;


    public TopBar(Context context) {
        activity = (Activity) context;
        tv_title = (TextView) activity.findViewById(R.id.tv_topbar_title);
        bn_back = (Button) activity.findViewById(R.id.bn_topbar_back);
        bn_menu = (Button) activity.findViewById(R.id.bn_topbar_menu);

        bn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,HomePageActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public void setTitleName(String name) {
        this.tv_title.setText(name);
    }

    public Button getBn_back() {
        return bn_back;
    }

    public Button getBn_menu() {
        return bn_menu;
    }

    public void setMenuVisible(int visibility) {
        bn_menu.setVisibility(visibility);
    }

    public void setBnBackVisible(int visibility) {
        bn_back.setVisibility(visibility);
    }

}
