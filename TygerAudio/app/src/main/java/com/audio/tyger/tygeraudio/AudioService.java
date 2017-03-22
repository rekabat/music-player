package com.audio.tyger.tygeraudio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by troy on 3/20/17.
 */

public class AudioService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{

    private final IBinder audioBind = new AudioBinder();

    private MediaPlayer mplayer;

    private LinkedList<String> playlistFinished;
    private String currentTrack;
    private LinkedList<String> playlist;

    private int curAudioPosition_ms;

    public class AudioBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return audioBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mplayer.stop();
        mplayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }

    private void initMediaPlayer() {
        mplayer = new MediaPlayer();

        // continue playback when going to sleep
        mplayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);

        // set audio type to music
        mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // listen for
        mplayer.setOnPreparedListener(this);
        mplayer.setOnCompletionListener(this);
        mplayer.setOnErrorListener(this);
    }

    public void setPlaylist(LinkedList<String> playlistFinished, String currentTrack, LinkedList<String> playlist) {
        this.playlistFinished = playlistFinished;
        this.currentTrack = currentTrack;
        this.playlist = playlist;
    }

    public void playCurrentTrack() {
        // reset player
        mplayer.reset();

        // play song
        try {
            mplayer.setDataSource(currentTrack);
        } catch (Exception e) {
            Log.e("AudioService", "Error setting data source", e);
        }

        mplayer.prepareAsync();
    }

    public boolean playNextTrack() {
        // TODO: loop if error on track

        boolean success = advanceTrack();
        if (!success) return false;

        playCurrentTrack();

        return true;
    }


    private boolean backTrack() {
        if (playlistFinished.size() == 0) return false;

        // place old track in queue
        playlist.addFirst(currentTrack);

        // get prev track
        currentTrack = playlistFinished.getLast();
        playlistFinished.removeLast();

        // set time to 0
        curAudioPosition_ms = 0;

        return true;
    }

    private boolean advanceTrack() {
        if (playlist.size() == 0) {
            returnToFirstTrack();
            return false;
        }

        // place old track on stack
        playlistFinished.addLast(currentTrack);

        // get next track
        currentTrack = playlist.getFirst();
        playlist.removeFirst();

        // set time to 0
        curAudioPosition_ms = 0;

        return true;
    }

    private void returnToFirstTrack() {
        // add the current track to finished
        playlistFinished.addLast(currentTrack);

        // set current song to the very first song (in the finished list)
        currentTrack = playlistFinished.getFirst();
        playlistFinished.removeFirst();

        // replace the playlist with the finished list, then clear the finished list
        playlist = playlistFinished;
        playlistFinished = new LinkedList<>();
    }


    // controls
    public void play() {
        if (mplayer.isPlaying()) return;
        mplayer.start();
    }

    public void stop() {
        if (!mplayer.isPlaying()) return;
        mplayer.stop();
    }

    public void pause() {
        if (!mplayer.isPlaying()) return;
        mplayer.pause();
        curAudioPosition_ms = mplayer.getCurrentPosition();
    }

    public void resume() {
        if (mplayer.isPlaying()) return;
        mplayer.seekTo(curAudioPosition_ms);
        play();
    }

    public void rewind(int seconds) {
        int newTime = mplayer.getCurrentPosition() - seconds*1000;
        if (newTime < 0) newTime = 0;
        mplayer.seekTo(newTime);
    }

    public void fastforward(int seconds) {
        int newTime = mplayer.getCurrentPosition() + seconds*1000;
//        if (newTime > songLength)
//            next();
//        else
            mplayer.seekTo(newTime);
    }

    public void prev() {
        backTrack();
        if (mplayer.isPlaying())
            playCurrentTrack();
    }

    public void next() {
        boolean success = advanceTrack();
        if (mplayer.isPlaying()) {
            if (success)
                playCurrentTrack();
            else {
                stop();
            }
        }
    }
}
