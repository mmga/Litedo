package com.mmga.litedo.Aty;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.MySimpleCallback;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DensityUtil;
import com.mmga.litedo.Util.LogUtil;
import com.mmga.litedo.Util.SharedPrefsUtil;
import com.mmga.litedo.Util.StatusBarCompat;
import com.mmga.litedo.db.Model.Memo;
import com.mmga.litedo.widget.CustomPtrHeader;
import com.mmga.litedo.widget.HeaderExtendsView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class ListActivity extends AppCompatActivity implements RecyclerViewAdapter.OnRecyclerViewItemClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private FloatingActionButton fabAdd;
        private TextView noItemInfo;
    private long mCreateTime;
    PtrFrameLayout ptrFrameLayout;
    RecyclerView.LayoutManager mLayoutManager;
    private HeaderExtendsView headerExtendsView;
    CustomPtrHeader header;

    private boolean mIsSwiping, mIsDraging;
    public static boolean mCanPullDown;
    private int pullToAddState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));

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

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityForNew();
            }
        });

        configPTR();

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



    //配置下拉刷新，只在无swipe，无drag且第一个item完全可见时启用
    private void configPTR() {
        pullToAddState = SharedPrefsUtil.getValue(this, "settings", "pullToAddState", SettingsActivity.PULL_TO_DO_NOTHING);
        if (pullToAddState == SettingsActivity.PULL_TO_ADD) {
            header = new CustomPtrHeader(this, mAdapter);
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
        }
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mLayoutManager instanceof LinearLayoutManager) {
                    if (((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                        if (mAdapter.canPullDown()) {
                            return true;
                        }
                    }else if (mAdapter.getItemCount() == 0) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                Log.d("mmga", "onUIRefreshBegin");
                if (pullToAddState == SettingsActivity.PULL_TO_ADD) {
                    final Memo memo = new Memo();
                    memo.setCreateTimeInMillis(System.currentTimeMillis());
                    mAdapter.addData(memo);
                    mRecyclerView.getItemAnimator().isRunning(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                        @Override
                        public void onAnimationsFinished() {
                            openActivityForEdit(memo,0);
                            frame.refreshComplete();
                        }
                    });
//                    openActivityForNew();
                }

            }
        });
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
    RelativeLayout platform;
    LinearLayout itemMenu;
    ImageView itemEditButton, itemRemindButton;

    //点击item弹出菜单
    @Override
    public void onItemClick(final View view, final Memo memo, final RecyclerViewAdapter.MyViewHolder holder) {
        itemText = (TextView) view.findViewById(R.id.fg_view);
        platform = (RelativeLayout) view.findViewById(R.id.platform);
        itemMenu = (LinearLayout) view.findViewById(R.id.item_menu);
        itemEditButton = (ImageView) view.findViewById(R.id.item_edit_button);
        itemRemindButton = (ImageView) view.findViewById(R.id.item_remind_button);

        if (itemMenu.getVisibility() == View.GONE) {
            showItemMenu();
            itemEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimatorSet set = hideItemMenu();
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            openActivityForEdit(memo, holder.getAdapterPosition());
                        }
                    });

                }
            });
            itemRemindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSetAlarmDialog();
                }
            });
        } else {
            hideItemMenu();
        }
    }

    //打开输入框，new
    private void openActivityForNew() {
        Intent i = new Intent(ListActivity.this, TextInputAty.class);
        startActivityForResult(i, 1);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

    //打开输入框，edit
    private void openActivityForEdit(Memo memo, int position) {
        Intent intent = new Intent(ListActivity.this, TextInputAty.class);
        intent.putExtra("data", memo.getContent());
        intent.putExtra("position", position);
        mCreateTime = memo.getCreateTimeInMillis();
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

    private void openSetAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("setTime");
        builder.show();
    }

    private void showItemMenu() {
        itemMenu.setVisibility(View.VISIBLE);
        int itemMenuWidth = DensityUtil.dip2px(ListActivity.this, 94);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(itemMenu, "translationX", itemMenuWidth, 0);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(platform, "translationX", 0, -itemMenuWidth);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(200);
        set.playTogether(anim1, anim2);
        set.start();
    }

    private AnimatorSet hideItemMenu() {
        int itemMenuWidth = DensityUtil.dip2px(ListActivity.this, 94);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(itemMenu, "translationX", 0, itemMenuWidth);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(platform, "translationX", -itemMenuWidth, 0);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(100);
        set.playTogether(anim1, anim2);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                itemMenu.setVisibility(View.GONE);
            }
        });
        return set;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == TextInputAty.RESULT_CODE_NEW) {
            noItemInfo.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Memo memo = new Memo();
            memo.setContent(data.getStringExtra("content"));
            memo.setCreateTimeInMillis(data.getLongExtra("time", 0));
            mAdapter.addData(memo);
            mRecyclerView.smoothScrollToPosition(0);
        } else if (requestCode == 1 && resultCode == TextInputAty.RESULT_CODE_EDIT) {
            Memo memo = new Memo();
            int position = data.getIntExtra("position", 0);
            if (data.getStringExtra("content").equals("")) {
                mAdapter.deleteData(position);
            } else {
                memo.setContent(data.getStringExtra("content"));
                memo.setCreateTimeInMillis(mCreateTime);
                mAdapter.updateData(position, memo);
            }

        }
    }

}


