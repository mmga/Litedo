package com.mmga.litedo.db;

import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.List;


public class DBUtil{

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

    /**
     * 按照id倒序查找数据
     * @return
     */
    public static List getAllMemo() {
        return DataSupport.where("isDone = ?", "0")
                .order("id desc")
                .find(Memo.class);
    }

    /**
     * 获得应显示的memo数量
     * @return
     */
    public static int getMemoNum() {
        int num = DataSupport.where("isDone = ?", "0").count(Memo.class);
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


    /**
     * 拖拽source到target位置，修改数据库对应项
     * @param sourceId
     * @param targetId
     */
//    public static void insertDataFromTo(int sourceId, int targetId) {
//
//        //id大的往小的拖
//        if (sourceId > targetId) {
//
//            for (int i = targetId; i < sourceId; i++) {
//                exchange(i, sourceId);
//            }
//
//        }
//        //id小的往大的拖
//        else {
//            for (int i = targetId; i > sourceId; i--) {
//                exchange(i, sourceId);
//            }
//        }
//
//    }
//
//    /**
//     * 交换两行数据
//     * @param smallId
//     * @param bigId
//     */
//    private static void exchange(int smallId, int bigId) {
//        Memo smallMemo = DataSupport.find(Memo.class, smallId);
//        Memo bigMemo = DataSupport.find(Memo.class, bigId);
//        bigMemo.update(smallId);
//        smallMemo.update(bigId);
//    }


}
