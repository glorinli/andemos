package xyz.dogold.andemos.av.videoextract;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * VideoExtractHelper
 * Created by glorin on 14/12/2017.
 */

public class VideoExtractHelper {
    private static final String TAG = "VideoExtractHelper";

    public static List<VideoFrameInfo> getVideoFrameInfo(String path) {
        MediaExtractor extractor = new MediaExtractor();

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        try {
            extractor.setDataSource(path);
        } catch (IOException e) {
            Log.e(TAG, "Fail to set data source", e);
            return null;
        }


        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);

            if (mime.contains("video")) {
                extractor.selectTrack(i);
            }
        }

        ByteBuffer inputBuffer = ByteBuffer.allocate(1024 * 1024);

        List<VideoFrameInfo> result = new ArrayList<>();

        while (extractor.readSampleData(inputBuffer, 0) >= 0) {
            long ptsUs = extractor.getSampleTime();
            final int sampleFlags = extractor.getSampleFlags();

            if ((sampleFlags & (MediaCodec.BUFFER_FLAG_CODEC_CONFIG | MediaCodec.BUFFER_FLAG_END_OF_STREAM)) == 0) {
                // This is a frame
                VideoFrameInfo info = new VideoFrameInfo();
                info.ptsUs = ptsUs;
                info.keyFrame = (sampleFlags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
                result.add(info);
            }

            extractor.advance();
        }

        extractor.release();

        return result;
    }
}
