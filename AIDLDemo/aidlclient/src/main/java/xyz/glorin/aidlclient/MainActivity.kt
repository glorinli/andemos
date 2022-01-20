package xyz.glorin.aidlclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import kotlinx.android.synthetic.main.activity_main.*
import xyz.glorin.aidlcommon.aidl.Album
import xyz.glorin.aidlcommon.aidl.AlbumManager
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    var conn: ServiceConnection? = null
    var albumManager: AlbumManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateConnState()

        btnBindUnbindService.setOnClickListener {
            if (conn == null) {
                val serviceConnection = object : ServiceConnection {
                    override fun onServiceDisconnected(name: ComponentName?) {
                        conn = null
                        updateConnState()
                    }

                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        updateConnState()
                        service?.let {
                            albumManager = AlbumManager.Stub.asInterface(service)
                        }
                    }

                }
                val intent = Intent()
                intent.component =
                    ComponentName("xyz.glorin.aidlservice", "xyz.glorin.aidlservice.AlbumService")
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

                conn = serviceConnection
            } else {
                conn?.let {
                    unbindService(it)
                }
                conn = null
                updateConnState()
            }
        }

        btnAddAlbum.setOnClickListener {
            albumManager?.addAlbum(createRandomAlbum())
        }

        btnGetAlbums.setOnClickListener {
            val sb = StringBuilder("Albums:\n")
            albumManager?.albums?.let {
                for (album in it) {
                    sb.append("$album\n")
                }
            }

            tvAlbums.text = sb.toString()
        }
    }

    private fun createRandomAlbum(): Album? {
        return Album("Title " + System.currentTimeMillis(), 3)
    }

    private fun updateConnState() {
        tvServiceState.text = "Service bound: ${if (conn == null) "false" else "true"}"
    }
}
