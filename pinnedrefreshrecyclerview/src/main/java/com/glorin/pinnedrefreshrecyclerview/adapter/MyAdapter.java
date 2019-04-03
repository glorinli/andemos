package com.glorin.pinnedrefreshrecyclerview.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.glorin.pinnedrefreshrecyclerview.R;
import com.glorin.pinnedrefreshrecyclerview.model.Constants;
import com.glorin.pinnedrefreshrecyclerview.model.NewsItemEntity;

import java.util.List;

/**
 * @author Glorin
 */
public class MyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyAdapter(List<MultiItemEntity> data) {
        super(data);

        addItemType(Constants.TYPE_HEADER, R.layout.item_header);
        addItemType(Constants.TYPE_DAILY_KNOWLEDGE, R.layout.item_daily_knowledge);
        addItemType(Constants.TYPE_TITLE_BAR, R.layout.item_title_bar);
        addItemType(Constants.TYPE_NEWS, R.layout.item_news);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case Constants.TYPE_NEWS:
                helper.setText(R.id.tvNewsContent, ((NewsItemEntity) item).getContent());
            default:
        }
    }
}
