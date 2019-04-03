package com.glorin.pinnedrefreshrecyclerview.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NewsItemEntity implements MultiItemEntity {
    private final String content;

    public NewsItemEntity(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return Constants.TYPE_NEWS;
    }

    public String getContent() {
        return content;
    }
}
