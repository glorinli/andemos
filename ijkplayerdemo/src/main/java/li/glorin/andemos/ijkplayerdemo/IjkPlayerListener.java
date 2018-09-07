package li.glorin.andemos.ijkplayerdemo;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class IjkPlayerListener implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnErrorListener {
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        // Noops
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }
}
