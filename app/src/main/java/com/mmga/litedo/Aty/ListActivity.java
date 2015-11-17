package com.mmga.litedo.Aty;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.R;
import com.mmga.litedo.Util.DBUtil;
import com.mmga.litedo.Util.DensityUtil;
import com.mmga.litedo.Util.LogUtil;
import com.mmga.litedo.Widget.CustomDialogAty;
import com.mmga.litedo.db.Model.Memo;

import java.util.List;

public class ListActivity extends AppCompatActivity implements RecyclerViewAdapter.OnRecyclerViewItemClickListener{


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


        ItemTouchHelper.SimpleCallback callback =new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
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
                Intent i = new Intent(ListActivity.this, CustomDialogAty.class);
                startActivity(i);
            }
        });


    }


    //读取数据库，刷新列表
    private void loadData() {
        if (DBUtil.getDataNum() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            noItemInfo.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noItemInfo.setVisibility(View.GONE);

            List<Memo> memoList = DBUtil.getAllData();
            mAdapter = new RecyclerViewAdapter(memoList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);

        }
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
        fabAdd.setVisibility(View.GONE);
        super.onPause();
        LogUtil.d("ListActivity","onPause");
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
        LogUtil.d("ListActivity", "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    //点击item弹出菜单
    @Override
    public void onItemClick(final View view, final String data, final RecyclerViewAdapter.MyViewHolder holder) {
        mItemText = (TextView) view.findViewById(R.id.fg_view);
        mItemMenu = (ImageView) view.findViewById(R.id.item_menu);
        if (mItemMenu.getVisibility() == View.GONE) {
            //dp转px
            mItemText.animate().translationX(-DensityUtil.dip2px(ListActivity.this,24)).start();
            mItemMenu.setVisibility(View.VISIBLE);
            mItemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListActivity.this, CustomDialogAty.class);
                    intent.putExtra("data", data);
                    intent.putExtra("position", holder.getAdapterPosition());
                    startActivity(intent);
                }
            });

        } else {
            mItemText.animate().translationX(0).start();
            mItemMenu.setVisibility(View.GONE);
        }

    }


}


