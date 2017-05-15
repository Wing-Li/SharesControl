package com.lyl.sharescontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lyl.sharescontrol.MyApp;
import com.lyl.sharescontrol.R;
import com.lyl.sharescontrol.net.NetWork;
import com.lyl.sharescontrol.net.entry.ShareEntry;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private SharePriceAdapter mAdapter;
    private ArrayList<ShareEntry> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mData = new ArrayList<>();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        mData.clear();
        ArrayList<ShareEntry> queryList = MyApp.liteOrm.query(ShareEntry.class);
        for (ShareEntry entry : queryList) {
            if (!entry.isDelete()) {
                mData.add(entry);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAdapter = new SharePriceAdapter(MainActivity.this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(mAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateNowPrice();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetPriceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateNowPrice() {
        for (int i = 0; i < mData.size(); i++) {

            Call<String> stringCall = NetWork.getShareApi().getShare(mData.get(i).getCode());
            final int finalI = i;
            stringCall.clone().enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String body = response.body();
                        int start = body.indexOf("=");
                        String content = body.substring(start + 2, body.length() - 3);
                        String[] codeArray = content.split(",");

                        mData.get(finalI).setNowPrice(codeArray[3]);
                        mAdapter.notifyItemChanged(finalI);
                    }
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), R.string.net_code_error, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
