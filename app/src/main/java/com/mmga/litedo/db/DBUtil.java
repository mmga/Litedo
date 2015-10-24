package com.mmga.litedo.db;

import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.List;


public class DBUtil {

    /**
     * 增加一个新的任务
     * @param content
     */
    public static void addMemo(String content){
        Memo memo = new Memo();
        memo.setContent("" + content);
        memo.setIsDone(0);
        memo.save();
    }

    /**
     * 将isDone标记为1，代表已完成的任务
     * @param memoId
     */
    public static void deleteMemo(int memoId){
        Memo memoToUpdate = new Memo();
        memoToUpdate.setIsDone(1);
        memoToUpdate.update(memoId);
    }


    public static void updateMemo(){
        //TODO
    }

    /**
     * 按照id倒序查找数据
     * @return
     */
    public static List getAllMemo() {
        return DataSupport.where("isDone = ?", "0")
                .order("id desc")
                .find(Memo.class);
    }

    public static int getMemoNum() {
        int num = DataSupport.where("isDone = ?", "0").count(Memo.class);
        return num;
    }
}
