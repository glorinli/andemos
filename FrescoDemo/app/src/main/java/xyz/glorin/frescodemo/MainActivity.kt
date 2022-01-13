package xyz.glorin.frescodemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.view.SimpleDraweeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<SimpleDraweeView>(R.id.draweeView).setImageURI(IMAGE_URL)
    }

    companion object {
        private const val IMAGE_URL = "https://frescolib.org/static/logo.png"
    }
}