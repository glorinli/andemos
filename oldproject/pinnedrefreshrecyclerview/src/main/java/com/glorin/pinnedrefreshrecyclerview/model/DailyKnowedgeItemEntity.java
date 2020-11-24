package com.glorin.pinnedrefreshrecyclerview.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DailyKnowedgeItemEntity implements MultiItemEntity {
    @Override
    public int getItemType() {
        return Constants.TYPE_DAILY_KNOWLEDGE;
    }
}
