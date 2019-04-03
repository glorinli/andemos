package com.glorin.pinnedrefreshrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private final List<MultiItemEntity> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        myAdapter = new MyAdapter(dataList);
        myAdapter.bindToRecyclerView(recyclerView);

        smartRefreshLayout.autoRefresh();
    }

    private void loadData() {
        dataList.clear();

        dataList.add(new HeaderItemEntity());
        dataList.add(new DailyKnowedgeItemEntity());
        dataList.add(new HeaderItemEntity());
        dataList.add(new DailyKnowedgeItemEntity());
        dataList.add(new TitleBarItemEntity());

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
}
