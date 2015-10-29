package com.mmga.litedo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmga.litedo.MySoundPool;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;
import com.mmga.litedo.Util.LogUtil;
import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {



    private List<Memo> memoList;

    public RecyclerViewAdapter(List<Memo> memoList) {
        this.memoList = memoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextView.setText(memoList.get(position).getContent());
        holder.recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //item的点击事件
            }
        });

    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    //滑动删除
    public void mOnSwiped(RecyclerView.ViewHolder viewHolder) {
        deleteData(viewHolder.getAdapterPosition());
        MySoundPool.playSoundDelete();
    }

    //删除一条内容
    private void deleteData(int position) {
        DBUtil.deleteMemo(memoList.get(position).getId());
        memoList.remove(position);
        notifyItemRemoved(position);
    }

//    拖拽
    public void mOnMove(int fromPos,int toPos) {
        Memo tempMemo = new Memo();
        tempMemo = DataSupport.where("content = ?", memoList.get(fromPos).getContent()).find(Memo.class).get(0);

        LogUtil.d("<<<<<", "id = " + tempMemo.getId() + " + " + memoList.get(toPos).getId());
        DBUtil.exchangeMemo(tempMemo.getId(), memoList.get(toPos).getId());



        if (fromPos < toPos) {
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(memoList, i, i + 1);
            }
        } else {
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(memoList, i, i - 1);
            }
        }
        notifyItemMoved(fromPos, toPos);


//        if (fromPos - toPos > 1) {
//            for (int i = fromPos; i > toPos; i--){
//                DBUtil.exchangeMemo(memoList.get(i).getId(), memoList.get(i - 1).getId());
//            }
//        }else if (toPos - fromPos > 1) {
//            for (int i = fromPos; i < toPos; i++) {
//                DBUtil.exchangeMemo(memoList.get(i).getId(), memoList.get(i + 1).getId());
//            }
//        }else {
//            DBUtil.exchangeMemo(memoList.get(fromPos).getId(),  memoList.get(toPos).getId());
//            Memo prev = memoList.remove(fromPos);
//            memoList.add(toPos > fromPos ? toPos - 1 : toPos, prev);
//            notifyItemMoved(fromPos, toPos);
//        }


    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public View recyclerViewItem;


        public MyViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            recyclerViewItem = itemView.findViewById(R.id.recycler_view_item);
        }
    }


}
