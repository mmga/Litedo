package com.mmga.litedo.Aty;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mmga.litedo.Adapter.RecyclerViewAdapter;
import com.mmga.litedo.R;
import com.mmga.litedo.db.Model.Memo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int i = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;

    private boolean isFirstIn = true;

    private List<Memo> memoList = new ArrayList<>();

    private String[] guideText = {"下拉新建", "左划删除", "单击编辑"};
    List<Memo> guideList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        initData();
        loadData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                initData(i);
                loadData();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void initData(int i) {
        Memo memo = new Memo();
        memo.setContent("" + i);
        memo.save();
    }


    //读取数据库，刷新列表
    private void loadData() {
        memoList = DataSupport.where("isDone = ?", "0").find(Memo.class);


//        if (isFirstIn) {
//            for (int i = 0; i < guideText.length; i++) {
//                guideList.get(i).setContent(guideText[i]);
//            }
//            mAdapter = new RecyclerViewAdapter(guideList);
//        }


        mAdapter = new RecyclerViewAdapter(memoList);
        mRecyclerView.setAdapter(mAdapter);
    }





}


