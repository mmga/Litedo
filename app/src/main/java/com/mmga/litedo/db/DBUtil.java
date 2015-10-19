package com.mmga.litedo.db;

import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.List;


public class DBUtil {

    public static void addMemo(String content){
        Memo memo = new Memo();
        memo.setContent("" + content);
        memo.save();
    }


    public static void deleteMemo(int memoId){
        Memo memoToUpdate = new Memo();
        memoToUpdate.setIsDone(1);
        memoToUpdate.update(memoId);
    }


    public static void updateMemo(){
        //TODO
    }

    public static List getAllMemo(Class modelClass) {
        return DataSupport.where("isDone = ?", "0").find(Memo.class);

        // TODO
    }
}
