package com.mmga.litedo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmga.litedo.R;
import com.mmga.litedo.db.Model.Memo;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private List<Memo> memoList;

    private String[] guideList = {"下拉新建", "左划删除", "单击编辑"};


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
                int memoId = memoList.get(position).getId();
                deleteData(position,memoId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return memoList.size();
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


    //删除一条内容
    private void deleteData(int position, int memoId) {

        //将要删除的项的isDone值改为1
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("isDone", "1");
//        DataSupport.update(Memo.class, contentValues, index);

        Memo memoToUpdate = new Memo();
        memoToUpdate.setIsDone(1);
        memoToUpdate.update(memoId);

        memoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, memoList.size());
    }
}
