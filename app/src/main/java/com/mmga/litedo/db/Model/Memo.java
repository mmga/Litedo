package com.mmga.litedo.db.Model;

import org.litepal.crud.DataSupport;

/**
 * Created by mmga on 2015/10/17.
 */
public class Memo extends DataSupport{

    private int id;

    private String content;

    private int count = 1;

    private int position = -1;


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
}
