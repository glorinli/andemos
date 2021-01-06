package xyz.glorin.layoutinflaterdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

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
        }
    }

    private fun loadHeavyLayoutOnly() {
        val startTime = SystemClock.uptimeMillis()
        resources.run {
            for (i in 1..1000) {
                getLayout(R.layout.layout_heavy)
            }
        }
        val cost = SystemClock.uptimeMillis() - startTime
        findViewById<TextView>(R.id.tvLoadHeavyLayoutCost).text = "Cost: $cost ms"
    }

    private fun inflaterHeavyLayout() {
        val startTime = SystemClock.uptimeMillis()
        LayoutInflater.from(this).run {
            for (i in 1..1000) {
                inflate(R.layout.layout_heavy, null, false)
            }
        }
        val cost = SystemClock.uptimeMillis() - startTime
        findViewById<TextView>(R.id.tvInflateHeavyLayoutCost).text = "Cost: $cost ms"
    }

    private fun inflaterSmallLayout() {
        val startTime = SystemClock.uptimeMillis()
        LayoutInflater.from(this).run {
            for (i in 1..1000) {
                inflate(R.layout.layout_small, null, false)
            }
        }
        val cost = SystemClock.uptimeMillis() - startTime
        findViewById<TextView>(R.id.tvInflateSmallLayoutCost).text = "Cost: $cost ms"
    }
}