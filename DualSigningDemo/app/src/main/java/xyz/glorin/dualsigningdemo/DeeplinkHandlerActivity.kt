package xyz.glorin.dualsigningdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DeeplinkHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let {
            handleUri(it)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let {
            handleUri(it)
        }
    }

    private fun handleUri(uri: Uri) {
        Log.d(TAG, "handleUri: $uri")
        uri.getQueryParameter("name")?.let {
            Toast.makeText(this, "$it says hello to you", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    companion object {
        private const val TAG = "Deeplink_TAG"
    }
}