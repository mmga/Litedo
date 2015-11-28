package com.mmga.litedo.Util;

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

    /**
     * 按照id倒序查找数据
     *
     * @return
     */
    public static List<Memo> getAllData() {
        return DataSupport.where("count > ?", "0")
                .order("id desc")
                .find(Memo.class);
    }

    /**
     * 同步list的数据至数据库
     * @param memoList
     */
    public static void syncData(List<Memo> memoList) {

        if (memoList != null) {
            for (int i = memoList.size() - 1; i >= 0; i--) {
                Memo mMemo = new Memo();
                mMemo.setContent(memoList.get(i).getContent());
                mMemo.setPosition(i);
                mList.add(mMemo);
            }
            DataSupport.deleteAll(Memo.class);
            DataSupport.saveAll(mList);
            mList.clear();
        }
    }

}
