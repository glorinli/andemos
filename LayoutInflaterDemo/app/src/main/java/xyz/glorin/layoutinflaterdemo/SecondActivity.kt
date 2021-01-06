package xyz.glorin.layoutinflaterdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)

        findViewById<TextView>(R.id.tvInfo).text =
            "LayoutInflater hashCode: ${LayoutInflater.from(this).hashCode()}"
    }
}