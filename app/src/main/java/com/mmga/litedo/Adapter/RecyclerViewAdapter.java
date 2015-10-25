package com.mmga.litedo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmga.litedo.MySoundPool;
import com.mmga.litedo.R;
import com.mmga.litedo.db.DBUtil;
import com.mmga.litedo.db.Model.Memo;

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
                //item 点击事件
//                deleteData(position);
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
        int memoId = memoList.get(position).getId();
        DBUtil.deleteMemo(memoId);
        memoList.remove(position);
//        memoList.add(memoList.size()-1,memoList.remove(position));
        notifyItemRemoved(position);
//        notifyItemInserted(memoList.size()-1);
    }

//    拖拽
    public void mOnMove(int fromPos,int toPos) {
        Memo prev =memoList.remove(fromPos);
        memoList.add(toPos > fromPos ? toPos - 1 : toPos, prev);
        notifyItemMoved(fromPos, toPos);
    }

    //交换在数据库中的位置
    public void exchangeData(int finalFromPos, int finalToPos) {
        DBUtil.insertDataFromTo(memoList.get(finalFromPos).getId(),memoList.get(finalToPos).getId());

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
