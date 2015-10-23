package com.mmga.litedo.Aty;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.Window;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.CustomDialog;
import com.mmga.litedo.MySoundPool;
import com.mmga.litedo.R;
import com.mmga.litedo.db.DBUtil;
import com.mmga.litedo.db.Model.Memo;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private int i = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;

    private FloatingActionButton fabAdd,fabSave;
    private List<Memo> memoList = new ArrayList<>();

    private MySoundPool mSoundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        ItemTouchHelper.SimpleCallback callback =new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //拖拽
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = viewHolder.getAdapterPosition();
                return true;

                //TODO
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.mOnSwiped(viewHolder);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void init() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySoundPool.playSoundAdd();
                fabAdd.setVisibility(View.GONE);
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

//
//                        loadData();
//                        fabAdd.setVisibility(View.VISIBLE);
                    }
                });
                mDialog.show();
            }
        });

        mSoundPool = new MySoundPool(ListActivity.this);


        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    //保存内容
    private void saveData(String string) {
        DBUtil.addMemo(string);
    }


    //读取数据库，刷新列表
    private void loadData() {
        memoList = DBUtil.getAllMemo(Memo.class);
        mAdapter = new RecyclerViewAdapter(memoList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
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
}


