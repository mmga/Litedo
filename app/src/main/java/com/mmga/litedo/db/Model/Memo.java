package com.mmga.litedo.db.Model;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by mmga on 2015/10/17.
 */
public class Memo extends DataSupport{

    private int id;

    private String content;

    private Date deadline;

    private int isDone = 0;




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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
