package xyz.glorin.layoutinflaterdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvInfo).text =
            "LayoutInflater hashCode: ${LayoutInflater.from(this).hashCode()}\n" +
                    "LayoutInflater hashCode2: ${LayoutInflater.from(this).hashCode()}"
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnSencondActivity -> startActivity(Intent(this, SecondActivity::class.java))
            R.id.btnInflateSmallLayout -> inflaterSmallLayout()
            R.id.btnInflateHeavyLayout -> inflaterHeavyLayout()
            R.id.btnLoadHeavyLayoutOnly -> loadHeavyLayoutOnly()
            R.id.btnAsyncLayoutInflater -> testAsyncLayoutInflater()
        }
    }

    private fun testAsyncLayoutInflater() {
        val container = findViewById<FrameLayout>(R.id.container)
        container.removeAllViews()
        val inflater = AsyncLayoutInflater(this)
        inflater.inflate(R.layout.layout_heavy_with_heavy_view, container) { view, _, _ ->
            container.addView(view)
        }
    }

    private fun loadHeavyLayoutOnly() {
        val startTime = SystemClock.elapsedRealtimeNanos()
        resources.run {
            getLayout(R.layout.layout_heavy)
            getLayout(R.layout.layout_heavy1)
            getLayout(R.layout.layout_heavy2)
            getLayout(R.layout.layout_heavy3)
            getLayout(R.layout.layout_heavy4)
            getLayout(R.layout.layout_heavy5)
            getLayout(R.layout.layout_heavy6)
            getLayout(R.layout.layout_heavy7)
            getLayout(R.layout.layout_heavy8)
            getLayout(R.layout.layout_heavy9)
        }
        val cost = SystemClock.elapsedRealtimeNanos() - startTime
        findViewById<TextView>(R.id.tvLoadHeavyLayoutCost).text = "Cost: $cost ns"
    }

    private fun inflaterHeavyLayout() {
        val cost = inflateLayout(R.layout.layout_heavy)
        findViewById<TextView>(R.id.tvInflateHeavyLayoutCost).text = "Cost: $cost ms"
    }

    private fun inflaterSmallLayout() {
        val layoutSmall = R.layout.layout_small
        val cost = inflateLayout(layoutSmall)
        val costFrameLayout = inflateLayout(R.layout.layout_small_frame)
        findViewById<TextView>(R.id.tvInflateSmallLayoutCost).text =
            "Cost: ConstraintLayout: $cost ms, FrameLayout: $costFrameLayout ms"
    }

    private fun inflateLayout(layoutSmall: Int): Long {
        val startTime = SystemClock.uptimeMillis()
        LayoutInflater.from(this).run {
            for (i in 1..1000) {
                inflate(layoutSmall, null, false)
            }
        }
        return SystemClock.uptimeMillis() - startTime
    }
}