package com.mmga.litedo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmga.litedo.Model.Memo;
import com.mmga.litedo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mmga on 2015/10/17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private List<Memo> memoList;
    protected ArrayList<Integer> ids;

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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(memoList.get(position).getContent());
        ids.add(position, memoList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        if (memoList != null) {
            return memoList.size();
        }
        return 0;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.text_view);

        }
    }

}
