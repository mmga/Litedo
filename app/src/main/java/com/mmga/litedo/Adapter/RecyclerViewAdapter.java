package com.mmga.litedo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;
import com.mmga.litedo.db.Model.Memo;

import java.util.Collections;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener{




    //定义接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view,String data,MyViewHolder viewHolder);
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    private List<Memo> memoList;
    private Memo lastDeletedMemo = null;
    private int lastDeletedMemoPosition;

    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(List<Memo> memoList) {
        this.memoList = memoList;
    }

    public void setAdapterData() {
        memoList = DBUtil.getAllData();
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextView.setText(memoList.get(position).getContent());
        holder.itemView.setTag(R.id.tag_first, memoList.get(position).getContent());
        holder.itemView.setTag(R.id.tag_second, holder);
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onItemClick(v,
                    (String) v.getTag(R.id.tag_first), (MyViewHolder) v.getTag(R.id.tag_second));
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        if (memoList != null) {
            return memoList.size();
        } else {
            return 0;
        }

    }

    //滑动删除
    public void mOnSwiped(RecyclerView.ViewHolder viewHolder) {
        deleteData(viewHolder.getAdapterPosition());
    }

    public void addData(Memo newMemo) {
        memoList.add(0, newMemo);
        notifyItemInserted(0);
    }

    private void deleteData(int position) {
        lastDeletedMemo = memoList.get(position);
        lastDeletedMemoPosition = position;
        memoList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void undoDelete() {
        memoList.add(lastDeletedMemoPosition, lastDeletedMemo);
        notifyItemInserted(lastDeletedMemoPosition);
    }

    public void updateData(int position,Memo editedMemo) {
        memoList.set(position, editedMemo);
        notifyItemChanged(position);
    }



    //    拖拽
    public void mOnMove(int fromPos,int toPos) {

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


    }


    public void syncMemo() {
            DBUtil.syncData(memoList);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public View recyclerViewItem;
        public ImageView itemMenu;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.fg_view);
            recyclerViewItem = itemView.findViewById(R.id.recycler_view_item);
            itemMenu = (ImageView) itemView.findViewById(R.id.item_menu);
        }
    }



}
