package xyz.glorin.aidlcommon.aidl;

import xyz.glorin.aidlcommon.aidl.Album;

interface AlbumManager {
    List<Album> getAlbums();
    void addAlbum(in Album album);
}