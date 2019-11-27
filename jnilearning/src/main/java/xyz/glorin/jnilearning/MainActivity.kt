package xyz.glorin.jnilearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLabel.text = "10 + 2 = ${NativeUtil.add(10, 2)}\n" +
                "10 - 2 = ${NativeUtil.sub(10, 2)}\n" +
                "10 * 2 = ${NativeUtil.multi(10, 2)}\n" +
                "10 / 2 = ${NativeUtil.div(10, 2)}"
    }
}
