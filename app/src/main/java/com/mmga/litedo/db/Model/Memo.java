package com.mmga.litedo.db.Model;

import org.litepal.crud.DataSupport;

/**
 * Created by mmga on 2015/10/17.
 */
public class Memo extends DataSupport{

    private int id;

    private String content;

    private int count = 1;




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
