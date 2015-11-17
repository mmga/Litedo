package com.mmga.litedo.Util;

import android.content.ContentValues;

import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class DBUtil {

    private static List<Memo> mList = new ArrayList<>();

    /**
     * 增加一个新的任务
     *
     * @param content
     */
    public static void addData(String content) {
        Memo memo = new Memo();
        memo.setContent("" + content);
        memo.setCount(1);
        memo.save();
    }

    public static void updateData(String content,String oldContent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        DataSupport.updateAll(Memo.class, contentValues,"content = ?", oldContent);
        LogUtil.d("updateData",content + " + " +oldContent);
    }


    /**
     * 按照id倒序查找数据
     *
     * @return
     */
    public static List getAllData() {
        return DataSupport.where("count > ?", "0")
                .order("id desc")
                .find(Memo.class);
    }

    /**
     * 获得应显示的memo数量
     *
     * @return
     */
    public static int getDataNum() {
        int num = DataSupport.where("count > ?", "0").count(Memo.class);
        return num;
    }

    /**
     * 同步list的数据至数据库
     * @param memoList
     */
    public static void syncData(List<Memo> memoList) {

        if (memoList == null) {
            return;
        }else{
            for (int i = memoList.size() - 1; i >= 0; i--) {
                Memo mMemo = new Memo();
                mMemo.setContent(memoList.get(i).getContent());
                mList.add(mMemo);
            }
            DataSupport.deleteAll(Memo.class);
            DataSupport.saveAll(mList);
            mList.clear();
        }
    }

}
