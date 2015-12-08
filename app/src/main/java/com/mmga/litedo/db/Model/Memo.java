package com.mmga.litedo.db.Model;

import org.litepal.crud.DataSupport;


public class Memo extends DataSupport{

    private int id;

    private String content;

    private int count = 1;

    private int position = -1;

    private Long createTimeInMillis;

    private int Star = 0;

    private int Top = 0;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }

    public int getTop() {
        return Top;
    }

    public void setTop(int top) {
        Top = top;
    }

    public Long getCreateTimeInMillis() {
        return createTimeInMillis;
    }

    public void setCreateTimeInMillis(Long createTimeInMillis) {
        this.createTimeInMillis = createTimeInMillis;
    }
}
