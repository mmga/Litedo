package com.mmga.litedo.Aty;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.MySimpleCallback;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DensityUtil;
import com.mmga.litedo.Util.LogUtil;
import com.mmga.litedo.Util.StatusBarCompat;
import com.mmga.litedo.db.Model.Memo;

public class ListActivity extends AppCompatActivity implements RecyclerViewAdapter.OnRecyclerViewItemClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private FloatingActionButton fabAdd;

    private TextView noItemInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));

        init();

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

        ItemTouchHelper.Callback callback = new MySimpleCallback(mAdapter) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                showUndoSnackbar();
                if (mAdapter.getItemCount() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    noItemInfo.setVisibility(View.VISIBLE);
                }

            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }


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


//    private void setAnimation() {
//
//        Animation animation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.slide_in_right);
//        LayoutAnimationController controller = new LayoutAnimationController(animation);
//        controller.setDelay(0.2f);
//        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        mRecyclerView.setLayoutAnimation(controller);
//        mRecyclerView.startAnimation(animation);
//    }

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
        super.onResume();
        fabAdd.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        loadData();
        super.onStart();
    }


    TextView itemText;
    RelativeLayout itemMenu;
    ImageView itemEditButton;

    //点击item弹出菜单
    @Override
    public void onItemClick(final View view, final String data, final RecyclerViewAdapter.MyViewHolder holder) {
        itemText = (TextView) view.findViewById(R.id.fg_view);
        itemMenu = (RelativeLayout) view.findViewById(R.id.item_menu);
        itemEditButton = (ImageView) view.findViewById(R.id.item_edit_button);
        if (itemMenu.getVisibility() == View.GONE) {
            showItemMenu();
            itemEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideItemMenu();
                    Intent intent = new Intent(ListActivity.this, TextInputAty.class);
                    intent.putExtra("data", data);
                    intent.putExtra("position", holder.getAdapterPosition());
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                }
            });
        } else {
            hideItemMenu();
        }
    }

    private void showItemMenu() {
        itemMenu.setVisibility(View.VISIBLE);
        int itemMenuWidth = DensityUtil.dip2px(ListActivity.this, 52);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(itemMenu, "translationX", itemMenuWidth, 0);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(itemText, "translationX", 0, -itemMenuWidth);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(200);
        set.playTogether(anim1, anim2);
        set.start();
    }

    private void hideItemMenu() {
        int itemMenuWidth = DensityUtil.dip2px(ListActivity.this, 52);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(itemMenu, "translationX", 0, itemMenuWidth);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(itemText, "translationX", -itemMenuWidth, 0);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(200);
        set.playTogether(anim1, anim2);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                itemMenu.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == TextInputAty.RESULT_CODE_NEW) {
            noItemInfo.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
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


