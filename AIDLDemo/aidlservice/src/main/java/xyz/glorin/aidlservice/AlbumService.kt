package xyz.glorin.aidlservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import xyz.glorin.aidlcommon.aidl.Album
import xyz.glorin.aidlcommon.aidl.AlbumManager

class AlbumService : Service() {
    private val albumList: MutableList<Album> = mutableListOf()

    override fun onBind(intent: Intent?): IBinder? {
        return AlbumServiceBinder()
    }

    inner class AlbumServiceBinder : AlbumManager.Stub() {
        override fun addAlbum(album: Album?) {
            album?.let { albumList.add(it) }
        }

        override fun getAlbums(): MutableList<Album> {
            return albumList
        }

    }
}