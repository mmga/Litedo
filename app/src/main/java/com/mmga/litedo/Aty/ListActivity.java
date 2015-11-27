package com.mmga.litedo.Aty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.MyApplication;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DensityUtil;
import com.mmga.litedo.Util.LogUtil;
import com.mmga.litedo.db.Model.Memo;

public class ListActivity extends AppCompatActivity implements RecyclerViewAdapter.OnRecyclerViewItemClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private FloatingActionButton fabAdd;

    private TextView noItemInfo;

    private ImageView mItemMenu;
    private TextView mItemText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        ItemTouchHelper.SimpleCallback callback = simpleCallback;
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    private void init() {
        noItemInfo = (TextView) findViewById(R.id.no_item_info);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivity.this, TextInputAty.class);
                startActivityForResult(i, 1);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            //拖拽
            final int fromPos = source.getAdapterPosition();
            final int toPos = target.getAdapterPosition();
            LogUtil.d("<<<<<", "" + fromPos + "+" + toPos);
            mAdapter.mOnMove(fromPos, toPos);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.mOnSwiped(viewHolder);
            showUndoSnackbar();
            if (mAdapter.getItemCount() == 0) {
                mRecyclerView.setVisibility(View.GONE);
                noItemInfo.setVisibility(View.VISIBLE);
            }
        }


    };


    private void showUndoSnackbar() {
        final Snackbar snackbar = Snackbar.make(mRecyclerView, "Confirm Deletion?", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.setVisibility(View.VISIBLE);
                noItemInfo.setVisibility(View.GONE);
                mAdapter.undoDelete();
            }
        });

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.setActionTextColor(getResources().getColor(R.color.colorWhite));
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                fabAdd.setClickable(false);
            }

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                fabAdd.setClickable(true);
            }
        });
        snackbar.show();
    }

    //读取数据库，刷新列表
    private void loadData() {
        mAdapter.setAdapterData();
        if (mAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            noItemInfo.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noItemInfo.setVisibility(View.GONE);
        }
    }


    private void setAnimation() {

        Animation animation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.slide_in_right);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.2f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mRecyclerView.setLayoutAnimation(controller);
        mRecyclerView.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutMenuItem:
                Intent i = new Intent(ListActivity.this, AboutActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        if (mAdapter != null) {
            mAdapter.syncMemo();
        }
        fabAdd.setVisibility(View.GONE);
        super.onPause();
        LogUtil.d("ListActivity", "onPause");
    }

    @Override
    protected void onResume() {
        fabAdd.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    protected void onStart() {

        loadData();
        super.onStart();
    }

    //点击item弹出菜单
    @Override
    public void onItemClick(final View view, final String data, final RecyclerViewAdapter.MyViewHolder holder) {
        mItemText = (TextView) view.findViewById(R.id.fg_view);
        mItemMenu = (ImageView) view.findViewById(R.id.item_menu);
        if (mItemMenu.getVisibility() == View.GONE) {
            //dp转px
            mItemText.animate().translationX(-DensityUtil.dip2px(ListActivity.this, 24)).start();
            mItemMenu.setVisibility(View.VISIBLE);
            mItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListActivity.this, TextInputAty.class);
                    intent.putExtra("data", data);
                    intent.putExtra("position", holder.getAdapterPosition());
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                }
            });
        } else {
            mItemText.animate().translationX(0).start();
            mItemMenu.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == TextInputAty.RESULT_CODE_NEW) {
            Memo memo = new Memo();
            memo.setContent(data.getStringExtra("content"));
            mAdapter.addData(memo);
            mRecyclerView.smoothScrollToPosition(0);
        } else if (requestCode == 1 && resultCode == TextInputAty.RESULT_CODE_EDIT) {
            Memo memo = new Memo();
            memo.setContent(data.getStringExtra("content"));
            int position = data.getIntExtra("position", 0);
            mAdapter.updateData(position, memo);
        }
    }
}


