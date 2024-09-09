package com.ori.origami;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioPlay {

    private long writeTime = 0;
    private long writeAppTime = 0;
    private final AudioTrack audioTrack;

    /**
     * @param sampleRate sample
     * @param channelConfig {@link AudioFormat#CHANNEL_IN_STEREO}
     * @param audioFormat {@link AudioFormat#ENCODING_PCM_8BIT} {@link AudioFormat#ENCODING_PCM_16BIT}
     */
    public AudioPlay(int sampleRate, int channelConfig, int audioFormat) {
        int bufferSize;
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        if (minBufferSize < 0) {
            bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        }else {
            bufferSize = minBufferSize;
        }
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize,
                AudioTrack.MODE_STREAM);
    }

    public void startPlay() {
        audioTrack.play();
    }

    public void stopPlay() {
        if(audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }

    public void close(){
        if(audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }

    public void write(byte[] buffer, int offsetInBytes, int sizeInBytes) {
        audioTrack.write(buffer, offsetInBytes, sizeInBytes);
    }

    public void resetTime(){
        writeTime = 0;
        writeAppTime = 0;
    }

    public boolean writeRealTime(byte[] buffer, int offsetInBytes, int sizeInBytes, long timeStamp) {
        long abs = Math.abs((System.currentTimeMillis() - writeAppTime) - (timeStamp - writeTime));
        Log.e("PCM", String.format("ABS: %s", abs));
        if (writeTime != 0 && abs > 1500) {
            return false;
        }else {
            writeTime = timeStamp;
            writeAppTime = System.currentTimeMillis();
        }
        audioTrack.write(buffer, offsetInBytes, sizeInBytes);
        return true;
    }

}
