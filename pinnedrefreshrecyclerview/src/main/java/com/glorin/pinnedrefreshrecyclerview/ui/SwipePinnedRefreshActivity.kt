package com.glorin.pinnedrefreshrecyclerview.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ListViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.glorin.pinnedrefreshrecyclerview.R

const val STICKY_ITEM_POSITION = 10
const val TAG = "SwipePinnedRefresh"

class SwipePinnedRefreshActivity : AppCompatActivity() {
    private var isPinnedMode = false
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_swipe_approach)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        listView = findViewById(R.id.listView)
        listView.adapter = DemoAdapter(this)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout.setOnChildScrollUpCallback(object : SwipeRefreshLayout.OnChildScrollUpCallback {
            override fun canChildScrollUp(parent: SwipeRefreshLayout, child: View?): Boolean {
                if (isPinnedMode) {

                    if (listView.firstVisiblePosition < STICKY_ITEM_POSITION) {
                        return false
                    } else if (listView.firstVisiblePosition == STICKY_ITEM_POSITION) {
                        return listView.getChildAt(0).top < 0
                    }
                    return true
                }

                return ListViewCompat.canScrollList(listView, -1)
            }

        })

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            private var scrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                this.scrollState = scrollState
            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (isPinnedMode) {
                    var shouldStopScroll = false
                    if (firstVisibleItem < STICKY_ITEM_POSITION) {
                        shouldStopScroll = true
                    } else if (firstVisibleItem == STICKY_ITEM_POSITION) {
                        val top = listView.getChildAt(0).top
                        shouldStopScroll = top >= 0
                    }

                    if (shouldStopScroll) {
                        listView.smoothScrollBy(0, 0)
                        listView.setSelection(STICKY_ITEM_POSITION)
                    }
                } else {
                    // Check should enter pinned mode
                    if (firstVisibleItem >= STICKY_ITEM_POSITION) {
                        isPinnedMode = true
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (isPinnedMode) {
            isPinnedMode = false

            listView.setSelection(0)
            return
        }

        super.onBackPressed()
    }
}

open class DemoAdapter(private val context: Context) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = (convertView
                ?: LayoutInflater.from(context).inflate(R.layout.item_news, parent, false))

        val tv = itemView.findViewById<TextView>(R.id.tvNewsContent)

        tv.text = "I am item ${getItem(position)}"

        itemView.setBackgroundColor(if (position == getStickyPosition()) Color.WHITE else Color.GREEN)

        onSetItemView(position, itemView, tv)

        return itemView
    }

    open fun onSetItemView(position: Int, itemView: View, tv: TextView) {}

    open fun getStickyPosition() = STICKY_ITEM_POSITION

    override fun getItem(position: Int): Any = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = 100

}