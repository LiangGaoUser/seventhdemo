package com.example.gf.seventhdemo.JavaBean;

public class Memo {
    String createTime;
    String content;
    String title;
    String id;

    public Memo(String title, String content, String createTime, String id) {
        this.createTime = createTime;
        this.content = content;
        this.title = title;
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreate_time(String create_time) {
        this.createTime = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
