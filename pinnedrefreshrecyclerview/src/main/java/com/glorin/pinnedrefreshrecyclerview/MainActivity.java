package com.glorin.pinnedrefreshrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.glorin.pinnedrefreshrecyclerview.adapter.MyAdapter;
import com.glorin.pinnedrefreshrecyclerview.model.DailyKnowedgeItemEntity;
import com.glorin.pinnedrefreshrecyclerview.model.HeaderItemEntity;
import com.glorin.pinnedrefreshrecyclerview.model.NewsItemEntity;
import com.glorin.pinnedrefreshrecyclerview.model.TitleBarItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glorin
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private View flPinnedTitleBar;

    private MyAdapter myAdapter;
    private final List<MultiItemEntity> dataList = new ArrayList<>();
    private final List<MultiItemEntity> dataListExceptNews = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private boolean isPinnedMode = false;
    private int firstNewsPosition = -1;
    private long checkPinnedAfterThisTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flPinnedTitleBar = findViewById(R.id.flPinnedTitleBar);

        initData();

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager == null || SystemClock.uptimeMillis() < checkPinnedAfterThisTime) {
                    return;
                }

                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition >= firstNewsPosition && !isPinnedMode) {
                    // Enter pinned mode
                    isPinnedMode = true;

                    dataList.removeAll(dataListExceptNews);
                    myAdapter.notifyItemRangeRemoved(0, dataListExceptNews.size());

                    Log.i(TAG, "Enter pinned mode, firstVisibleItemPosition: " + firstVisibleItemPosition);
                }
            }
        });

        myAdapter = new MyAdapter(dataList);
        myAdapter.bindToRecyclerView(recyclerView);

        smartRefreshLayout.autoRefresh();
    }

    private void initData() {
        dataListExceptNews.add(new HeaderItemEntity());
        dataListExceptNews.add(new DailyKnowedgeItemEntity());
        dataListExceptNews.add(new HeaderItemEntity());
        dataListExceptNews.add(new DailyKnowedgeItemEntity());
        dataListExceptNews.add(new TitleBarItemEntity());
    }

    private void loadData() {
        dataList.clear();

        if (!isPinnedMode) {
            dataList.addAll(dataListExceptNews);
            firstNewsPosition = dataListExceptNews.size();
        } else {
            firstNewsPosition = 0;
        }

        dataList.add(new NewsItemEntity("富强"));
        dataList.add(new NewsItemEntity("民主"));
        dataList.add(new NewsItemEntity("文明"));
        dataList.add(new NewsItemEntity("和谐"));
        dataList.add(new NewsItemEntity("自由"));
        dataList.add(new NewsItemEntity("平等"));
        dataList.add(new NewsItemEntity("公正"));
        dataList.add(new NewsItemEntity("法治"));
        dataList.add(new NewsItemEntity("爱国"));
        dataList.add(new NewsItemEntity("敬业"));
        dataList.add(new NewsItemEntity("诚信"));
        dataList.add(new NewsItemEntity("友善"));

        myAdapter.notifyDataSetChanged();

        smartRefreshLayout.finishRefresh();
    }

    public void onClick(View view) {
        if (isPinnedMode) {
            checkPinnedAfterThisTime = SystemClock.uptimeMillis() + 1000L;

            dataList.addAll(0, dataListExceptNews);
            firstNewsPosition = dataListExceptNews.size();

            myAdapter.notifyItemRangeInserted(0, dataListExceptNews.size());

            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                }
            }, 200L);

            isPinnedMode = false;
        }
    }
}
