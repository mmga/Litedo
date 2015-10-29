package com.mmga.litedo.Aty;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.MySoundPool;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;
import com.mmga.litedo.Util.LogUtil;
import com.mmga.litedo.Widget.CustomDialog;
import com.mmga.litedo.Widget.CustomDialogAty;
import com.mmga.litedo.db.Model.Memo;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity{


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private FloatingActionButton fabAdd;
    private List<Memo> memoList = new ArrayList<>();

    private TextView noItemInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        ItemTouchHelper.SimpleCallback callback =new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                //拖拽
                final int fromPos = source.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                LogUtil.d("<<<<<", "" + fromPos + "+" + toPos);
                mAdapter.mOnMove(fromPos, toPos);
                return true;

                //TODO
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.mOnSwiped(viewHolder);
                if (mAdapter.getItemCount() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    noItemInfo.setVisibility(View.VISIBLE);
                }
            }
        };
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySoundPool.playSoundAdd();
                fabAdd.setVisibility(View.GONE);
//                showDialog();
                Intent i = new Intent(ListActivity.this, CustomDialogAty.class);
                startActivity(i);
            }
        });

        MySoundPool mSoundPool = new MySoundPool(ListActivity.this);


    }

    //弹出dialog
    private void showDialog() {
        Dialog mDialog = new CustomDialog(ListActivity.this);
        //设置dialog的位置
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        mDialog.setCanceledOnTouchOutside(true);
        //对话框消失时刷新数据
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                        延时更新UI
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_UI;
                        handler.sendMessage(message);

                    }
                }, 250);
            }
        });
        mDialog.show();
    }


    //读取数据库，刷新列表
    private void loadData() {
        if (DBUtil.getMemoNum() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            noItemInfo.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noItemInfo.setVisibility(View.GONE);
            memoList = DBUtil.getAllMemo();
            mAdapter = new RecyclerViewAdapter(memoList);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static final int UPDATE_UI = 1;

    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    loadData();
                    fabAdd.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
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
        if (mAdapter!=null) {
            mAdapter.syncMemo();
        }
        super.onPause();
        LogUtil.d("<<<<<","onPause");
    }

    @Override
    protected void onResume() {
        //      延时更新UI
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_UI;
                handler.sendMessage(message);

            }
        }, 250);
        LogUtil.d("<<<<<", "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d("<<<<<","onRestart");
    }
}


