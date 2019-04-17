package com.glorin.pinnedrefreshrecyclerview.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class PinnedSwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        SwipeRefreshLayout(context, attrs) {
    override fun canChildScrollUp(): Boolean {
        return super.canChildScrollUp()
    }
}