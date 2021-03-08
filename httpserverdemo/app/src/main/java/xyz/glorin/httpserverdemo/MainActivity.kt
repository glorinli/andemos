package xyz.glorin.httpserverdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import xyz.glorin.httpserverdemo.server.AndroidWebServer
import xyz.glorin.httpserverdemo.server.NetworkUtil

class MainActivity : AppCompatActivity() {
    private val server: AndroidWebServer = AndroidWebServer()
    private lateinit var btnToggleServer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.ipv4Address).text = NetworkUtil.getIpV4Address()?.let {
            "http://$it:8478"
        } ?: "N/A"
        findViewById<TextView>(R.id.ipv6Address).text = NetworkUtil.getIpV6Address()?.let {
            "http://[$it]:8478"
        } ?: "N/A"

        btnToggleServer = findViewById(R.id.toggleServer)
        btnToggleServer.setOnClickListener {
            toggleServer()
        }
        updateButton()
    }

    private fun toggleServer() {
        if (server.wasStarted()) {
            server.stop()
        } else {
            server.start()
        }

        updateButton()
    }

    private fun updateButton() {
        btnToggleServer.setText(if (server.wasStarted()) R.string.stop_server else R.string.start_server)
    }

    override fun onDestroy() {
        super.onDestroy()

        server.stop()
    }
}