package com.mmga.litedo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmga.litedo.ItemTouchHelperCallback;
import com.mmga.litedo.PinHelper;
import com.mmga.litedo.R;
import com.mmga.litedo.adapter.RecyclerViewAdapter;
import com.mmga.litedo.db.Model.Memo;
import com.mmga.litedo.util.DensityUtil;
import com.mmga.litedo.util.SharedPrefsUtil;
import com.mmga.litedo.util.StatusBarCompat;
import com.mmga.litedo.widget.CustomFab;
import com.mmga.litedo.widget.CustomPtrHeader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ListActivity extends AppCompatActivity implements RecyclerViewAdapter.OnRecyclerViewItemClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private CustomFab fabAdd;
    private TextView noItemInfo;
    private long mCreateTime;
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomPtrHeader header;
    private boolean isLastDeletePin;
    private ItemTouchHelper.Callback itemTouchHelperCallback;
    private ImageView itemEditButton;
    private ImageView itemPinButton;
    private RecyclerViewAdapter.MyViewHolder currentOpenedHolder;
    private RecyclerViewAdapter.MyViewHolder lastOpenedHolder;
    private List<RecyclerViewAdapter.MyViewHolder> openedMenuStack = new ArrayList<>();
    //item menu的宽度，按钮数量*按钮宽度+divider+左右边距
    private static final int ITEM_MENU_WIDTH = 2 * 44 + 2 + 4;
    private int pullToAddState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));

        init();

    }


    private void init() {
        noItemInfo = (TextView) findViewById(R.id.no_item_info);
        ptrFrameLayout = (PtrFrameLayout) findViewById(R.id.ptr_frame);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAdd = (CustomFab) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityForNew();
            }
        });

        configPTR();

        itemTouchHelperCallback = new ItemTouchHelperCallback(mAdapter) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                super.onSwiped(viewHolder, direction);
                if (viewHolder.getLayoutPosition() < PinHelper.getPinNumber()) {
                    PinHelper.minusPinNum();
                    isLastDeletePin = true;
                } else {
                    isLastDeletePin = false;
                }
                Log.d("mmga", "pinNumber = " + PinHelper.getPinNumber());
                showUndoSnackbar();
                if (mAdapter.getItemCount() == 0) {
                    setRecyclerViewVisible(false);
                }
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void configPTR() {
        pullToAddState = SharedPrefsUtil.getValue(this, "settings", "pullToAddState", SettingsActivity.PULL_TO_DO_NOTHING);
        //如果下拉新建功能开启，添加自定义header
        if (pullToAddState == SettingsActivity.PULL_TO_ADD) {
            header = new CustomPtrHeader(this, mAdapter);
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
        }

        ptrFrameLayout.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //在无swipe，无drag且第一个item完全可见时启用，或者没有item时
                if (mLayoutManager instanceof LinearLayoutManager) {
                    if (((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                        if (mAdapter.canPullDown()) {
                            return true;
                        }
                    } else if (mAdapter.getItemCount() == 0) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                //下拉到位后松手时
                if (pullToAddState == SettingsActivity.PULL_TO_ADD) {
                    frame.refreshComplete();
                    openActivityForNew();
                }
            }
        });
    }

    private void showUndoSnackbar() {
        final Snackbar snackbar = Snackbar.make(mRecyclerView, "Confirm Deletion?", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewVisible(true);
                if (isLastDeletePin) {
                    PinHelper.plusPinNum();
                }
                mAdapter.undoDelete();
            }
        });

        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite));
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

    private void loadData() {
        mAdapter.setAdapterData();
        if (mAdapter.getItemCount() == 0) {
            setRecyclerViewVisible(false);
        } else {
            setRecyclerViewVisible(true);
        }
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
                Intent i = new Intent(ListActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        if (mAdapter != null) {
            //将adapter中的数据同步到数据库，其他时候的操作都是对mAdapter里的list进行操作，只有pause的时候同步到数据库
            mAdapter.syncMemo();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mmga", "pinNumber = " + PinHelper.getPinNumber());
        fabAdd.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void setRecyclerViewVisible(Boolean isVisible) {
        if (isVisible) {
            mRecyclerView.setVisibility(View.VISIBLE);
            noItemInfo.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            noItemInfo.setVisibility(View.VISIBLE);
        }
    }


    //每当点开一个item的menu时，关闭其他的menu。肯定有改进办法，现在这方法太丑了。。
    //// TODO: 2015/12/18
    private void closeOtherMenu() {
        for (RecyclerViewAdapter.MyViewHolder holder : openedMenuStack) {
            if (holder != currentOpenedHolder) {
                hideItemMenu(holder);
            }
        }
    }

    //点击item弹出菜单
    @Override
    public void onItemClick(final View view, final Memo memo, final RecyclerViewAdapter.MyViewHolder holder) {
        TextView itemText = (TextView) view.findViewById(R.id.fg_view);
        LinearLayout itemMenu = (LinearLayout) view.findViewById(R.id.item_menu);
        itemEditButton = (ImageView) view.findViewById(R.id.item_edit_button);
        itemPinButton = (ImageView) view.findViewById(R.id.item_pin_button);

        if (itemMenu.getVisibility() == View.GONE) {
            showItemMenu(holder);
            //用来记录当前打开的这个item menu,关闭其他的。
            openedMenuStack.add(holder);
            currentOpenedHolder = holder;
            closeOtherMenu();
            if (lastOpenedHolder != null) {
                Log.d("mmga", "lastOpenedHolder");
                openedMenuStack.remove(lastOpenedHolder);
            }
            lastOpenedHolder = holder;
            //编辑按钮点击事件
            itemEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimatorSet set = hideItemMenu(holder);
                    if (set == null) {
                        return;
                    }
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            openActivityForEdit(memo, holder.getAdapterPosition());
                        }
                    });

                }
            });
            itemPinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimatorSet set = hideItemMenu(holder);
                    if (set == null) {
                        return;
                    }
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fabAdd.show();
                            if (memo.getTop() == Memo.TOP_NORMAL) {
                                memo.setTop(Memo.TOP_PIN);
                                holder.pinStateImage.setVisibility(View.VISIBLE);
                                mAdapter.moveData(memo, holder.getAdapterPosition(), 0);
                                PinHelper.plusPinNum();
                                Log.d("mmga", "pinNumber = " + PinHelper.getPinNumber());
                            } else {
                                memo.setTop(Memo.TOP_NORMAL);
                                holder.pinStateImage.setVisibility(View.GONE);
                                mAdapter.moveData(memo, holder.getAdapterPosition(), PinHelper.getPinNumber() - 1);
                                PinHelper.minusPinNum();
                                Log.d("mmga", "pinNumber = " + PinHelper.getPinNumber());
                            }
                        }
                    });
                }
            });
        } else {
            hideItemMenu(holder);
            fabAdd.show();
        }
    }

    private void openActivityForNew() {
        fabAdd.hide();
        Intent i = new Intent(ListActivity.this, TextInputActivity.class);
        startActivityForResult(i, 1);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

    private void openActivityForEdit(Memo memo, int position) {
        fabAdd.hide();
        Intent intent = new Intent(ListActivity.this, TextInputActivity.class);
        intent.putExtra("data", memo.getContent());
        intent.putExtra("position", position);
        mCreateTime = memo.getCreateTimeInMillis();
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }


    private void showItemMenu(RecyclerViewAdapter.MyViewHolder holder) {

        if (holder.itemMenu.getVisibility() == View.VISIBLE) {
            return;
        }
        holder.itemMenu.setVisibility(View.VISIBLE);
        int itemMenuWidth = DensityUtil.dip2px(ListActivity.this, ITEM_MENU_WIDTH);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(holder.itemMenu, "translationX", itemMenuWidth, 0);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(holder.platform, "translationX", 0, -itemMenuWidth);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(200);
        set.playTogether(anim1, anim2);
        set.start();
        fabAdd.hide();
    }

    private AnimatorSet hideItemMenu(final RecyclerViewAdapter.MyViewHolder holder) {
        if (holder.itemMenu.getVisibility() == View.GONE) {
            return null;
        }
        int itemMenuWidth = DensityUtil.dip2px(ListActivity.this, ITEM_MENU_WIDTH);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(holder.itemMenu, "translationX", 0, itemMenuWidth);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(holder.platform, "translationX", -itemMenuWidth, 0);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(100);
        set.playTogether(anim1, anim2);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                holder.itemMenu.setVisibility(View.GONE);
            }
        });
        return set;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == TextInputActivity.RESULT_CODE_NEW) {
            noItemInfo.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Memo memo = new Memo();
            memo.setContent(data.getStringExtra("content"));
            memo.setCreateTimeInMillis(data.getLongExtra("time", 0));
            mAdapter.addData(memo, PinHelper.getPinNumber());
            mRecyclerView.smoothScrollToPosition(0);
        } else if (requestCode == 1 && resultCode == TextInputActivity.RESULT_CODE_EDIT) {
            Memo memo = new Memo();
            int position = data.getIntExtra("position", 0);
            if (data.getStringExtra("content").equals("")) {
                mAdapter.deleteData(position);
            } else {
                memo.setContent(data.getStringExtra("content"));
                memo.setCreateTimeInMillis(mCreateTime);
                memo.setTop(position < PinHelper.getPinNumber() ? Memo.TOP_PIN : Memo.TOP_NORMAL);
                mAdapter.updateData(position, memo);
            }

        }
    }

}


