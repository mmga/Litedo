package com.mmga.litedo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmga.litedo.MyApplication;
import com.mmga.litedo.MySimpleCallback;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;
import com.mmga.litedo.Util.DateUtil;
import com.mmga.litedo.Util.SharedPrefsUtil;
import com.mmga.litedo.db.Model.Memo;

import java.util.Collections;
import java.util.List;


public class RecyclerViewHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, MySimpleCallback.ItemTouchHelperAdapter {


    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    private List<Memo> memoList;
    private Memo lastDeletedMemo = null;
    private int lastDeletedMemoPosition;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;



    public RecyclerViewHeaderAdapter() {
    }


    public RecyclerViewHeaderAdapter(List<Memo> memoList) {
        this.memoList = memoList;
    }

    public void setAdapterData() {
        memoList = DBUtil.getAllData();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BODY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            BodyViewHolder vh = new BodyViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return vh;
        } else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_view, parent, false);
            HeaderViewHolder vh = new HeaderViewHolder(view);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            BodyViewHolder bodyViewHolder = (BodyViewHolder) holder;
            //要空出header来所以position-1
            bodyViewHolder.mTextView.setText(memoList.get(position-1).getContent());
            if (SharedPrefsUtil.getValue(MyApplication.getContext(), "settings", "isShowTime", false)) {
                bodyViewHolder.mCreateTime.setVisibility(View.VISIBLE);
                bodyViewHolder.mCreateTime.setText(DateUtil.detailedTime(memoList.get(position-1).getCreateTimeInMillis()));
//            holder.mCreateTime.setText(DateUtil.simpleTime(memoList.get(position).getCreateTimeInMillis()));
            } else {
                bodyViewHolder.mCreateTime.setVisibility(View.GONE);
            }
            holder.itemView.setTag(R.id.tag_first, memoList.get(position-1));
            holder.itemView.setTag(R.id.tag_second, holder);
        }else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onItemClick(v,
                    (Memo) v.getTag(R.id.tag_first), (BodyViewHolder) v.getTag(R.id.tag_second));
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        if (memoList != null) {
            return memoList.size() + 1;
        } else {
            return 1;
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
        lastDeletedMemo = memoList.get(position);
        lastDeletedMemoPosition = position;
        memoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_BODY;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    public void addData(Memo newMemo) {
        memoList.add(0, newMemo);
        notifyItemInserted(0);
    }

    public void undoDelete() {
        memoList.add(lastDeletedMemoPosition, lastDeletedMemo);
        notifyItemInserted(lastDeletedMemoPosition);
    }

    public void updateData(int position, Memo editedMemo) {
        memoList.set(position, editedMemo);
        notifyItemChanged(position);
    }

    public void syncMemo() {
        DBUtil.syncData(memoList);
    }


    public class BodyViewHolder extends RecyclerView.ViewHolder implements MySimpleCallback.ItemTouchHelperViewHolder {

        public TextView mTextView;
        public View recyclerViewItem;
        public ImageView itemEditButton;
        public TextView mCreateTime;
        public ImageView itemRemindButton;



        public BodyViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.fg_view);
            mCreateTime = (TextView) itemView.findViewById(R.id.create_time);
            recyclerViewItem = itemView.findViewById(R.id.recycler_view_item);
            itemEditButton = (ImageView) itemView.findViewById(R.id.item_edit_button);
            itemRemindButton = (ImageView) itemView.findViewById(R.id.item_remind_button);
        }

        @Override
        public void onItemSelected() {
            recyclerViewItem.setScaleY(0.95f);
        }

        @Override
        public void onItemClear() {
            recyclerViewItem.setScaleY(1f);
        }
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }



    //定义接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Memo memo, BodyViewHolder viewHolder);
    }

}
