package com.glorin.pinnedrefreshrecyclerview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.glorin.pinnedrefreshrecyclerview.ui.HideItemsPinnedRefreshActivity
import com.glorin.pinnedrefreshrecyclerview.ui.SwipePinnedRefreshActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnHideItemApproach).setOnClickListener(this)
        findViewById<View>(R.id.btnSwipeRefreshApproach).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnHideItemApproach -> startActivity(Intent(this@MainActivity, HideItemsPinnedRefreshActivity::class.java))
            R.id.btnSwipeRefreshApproach -> startActivity(Intent(this@MainActivity, SwipePinnedRefreshActivity::class.java))
        }
    }
}