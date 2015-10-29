package com.mmga.litedo.Util;

import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class DBUtil {

    private static List<Memo> mList = new ArrayList<>();
//    private static Memo mMemo = null;


    /**
     * 增加一个新的任务
     *
     * @param content
     */
    public static void addMemo(String content) {
        Memo memo = new Memo();
        memo.setContent("" + content);
        memo.setCount(1);
        memo.save();
    }

    /**
     * 将isDone标记为1，代表已完成的任务
     *
     * @param memoId
     */
    public static void deleteMemo(int memoId) {
        Memo memoToUpdate = new Memo();
        memoToUpdate.setCount(0);
        memoToUpdate.update(memoId);
    }

    /**
     * 按照id倒序查找数据
     *
     * @return
     */
    public static List getAllMemo() {
        return DataSupport.where("count > ?", "0")
                .order("id desc")
                .find(Memo.class);
    }

    /**
     * 获得应显示的memo数量
     *
     * @return
     */
    public static int getMemoNum() {
        int num = DataSupport.where("count > ?", "0").count(Memo.class);
        return num;
    }

    public static void exchangeMemo(int fromId, int toId) {

        exchange(fromId, toId);
    }

    private static void exchange(int fromId, int toId) {
        Memo fromMemo = DataSupport.find(Memo.class, fromId);
        Memo toMemo = DataSupport.find(Memo.class, toId);
        fromMemo.update(toId);
        toMemo.update(fromId);
    }


    public static void syncData(List<Memo> memoList) {

        if (memoList == null) {
            return;
        }else{
            LogUtil.d("<<<<<", "" + memoList.size());
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
