package com.example.gf.seventhdemo.homepage;

public class HomePageTab {
    private String name;    //标签的名称
    private int iconId;     //标签的图片
    private Class<?> cls;   //标签对应的活动


    public HomePageTab(String name, int imageId, Class<?> cls) {
        this.name = name;
        this.iconId = imageId;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int imageId) {
        this.iconId = imageId;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
