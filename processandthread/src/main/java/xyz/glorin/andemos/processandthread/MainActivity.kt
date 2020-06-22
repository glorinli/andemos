package xyz.glorin.andemos.processandthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sb = StringBuilder()

        sb.append("Current threads: \n\n")

        Thread.getAllStackTraces().forEach {
            sb.append(it.key.name)
            sb.append("\n")
        }

        tvMain.text = sb.toString()
    }
}
