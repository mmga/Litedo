package com.mmga.litedo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmga.litedo.ItemTouchHelperCallback;
import com.mmga.litedo.MyApplication;
import com.mmga.litedo.R;
import com.mmga.litedo.db.DBUtil;
import com.mmga.litedo.db.Model.Memo;
import com.mmga.litedo.util.DateUtil;
import com.mmga.litedo.util.SharedPrefsUtil;

import java.util.Collections;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener, ItemTouchHelperCallback.ItemTouchHelperAdapter {


    //定义接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Memo memo, MyViewHolder viewHolder);
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    private List<Memo> memoList;
    private Memo lastDeletedMemo = null;
    private int lastDeletedMemoPosition;
    private boolean canPullDown = true;

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
        if (SharedPrefsUtil.getValue(MyApplication.getContext(), "settings", "isShowTime", false)) {
            holder.mCreateTime.setVisibility(View.VISIBLE);
            holder.mCreateTime.setText(DateUtil.detailedTime(memoList.get(position).getCreateTimeInMillis()));
//            holder.mCreateTime.setText(DateUtil.simpleTime(memoList.get(position).getCreateTimeInMillis()));
        } else {
            holder.mCreateTime.setVisibility(View.GONE);
        }
        holder.pinStateImage.setVisibility(memoList.get(position).getTop() == Memo.TOP_PIN ? View.VISIBLE : View.GONE);
        holder.itemView.setTag(R.id.tag_first, memoList.get(position));
        holder.itemView.setTag(R.id.tag_second, holder);
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onItemClick(v,
                    (Memo) v.getTag(R.id.tag_first), (MyViewHolder) v.getTag(R.id.tag_second));
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


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(memoList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        deleteData(position);
    }


    public void addData(Memo newMemo,int position) {
        memoList.add(position, newMemo);
        notifyItemInserted(position);
    }

    public void deleteData(int position) {
        lastDeletedMemo = memoList.get(position);
        lastDeletedMemoPosition = position;
        memoList.remove(position);
        notifyItemRemoved(position);
    }

    public void undoDelete() {
        memoList.add(lastDeletedMemoPosition, lastDeletedMemo);
        notifyItemInserted(lastDeletedMemoPosition);
    }

    public void updateData(int position, Memo editedMemo) {
        memoList.set(position, editedMemo);
        notifyItemChanged(position);
    }

    public void moveData(Memo memo, int fromPosition,int toPosition) {
        memoList.remove(fromPosition);
        memoList.add(toPosition, memo);
        notifyItemMoved(fromPosition, toPosition);
    }


    public void syncMemo() {
        DBUtil.syncData(memoList);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperCallback.ItemTouchHelperViewHolder {

        public TextView mTextView;
        public View recyclerViewItem;
        public ImageView itemEditButton;
        public TextView mCreateTime;
        public ImageView itemPinButton;
        public ImageView pinStateImage;
        public RelativeLayout platform;
        public LinearLayout itemMenu;


        public MyViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.fg_view);
            mCreateTime = (TextView) itemView.findViewById(R.id.create_time);
            recyclerViewItem = itemView.findViewById(R.id.recycler_view_item);
            itemEditButton = (ImageView) itemView.findViewById(R.id.item_edit_button);
            itemPinButton = (ImageView) itemView.findViewById(R.id.item_pin_button);
            pinStateImage = (ImageView) itemView.findViewById(R.id.pin_state);
            platform = (RelativeLayout) itemView.findViewById(R.id.platform);
            itemMenu = (LinearLayout) itemView.findViewById(R.id.item_menu);
        }

        @Override
        public void onItemSelected() {
            recyclerViewItem.setScaleY(0.95f);
            canPullDown = false;
        }

        @Override
        public void onItemClear() {
            recyclerViewItem.setScaleY(1f);
            canPullDown = true;
        }

    }

    public boolean canPullDown() {
        return canPullDown;
    }
}
