package com.example.nfer.mediaplayer;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.nfer.mediaplayer.MainActivity;

import java.io.IOException;

public class Mp3Player extends Service {
    public MediaPlayer mPlayer = null;


    public int onStartCommand(Intent intent, int flags, int startID){
        Log.i("In onStart", "Here I am");
        final Bundle extra = intent.getExtras();
        String requestType = extra.getString("request");

        if (requestType.equals("create")) {
            String uriString = extra.getString("uri");
            Uri uri = Uri.parse(uriString);
            createMedia(uri);
        }
        else if (requestType.equals("play"))    playRequest();
        else if (requestType.equals("pause"))  pauseRequest();
        else if (requestType.equals("stop"))    stopRequest();
        else if (requestType.equals("exit"))    exitRequest();
        return START_STICKY;
    }


    void createMedia (Uri uri){
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            Log.i("In createRequest", "Here I am");
            //grab uri string from intent and convert it to a uri
            //set up media player
            //Snippets taken from http://programmerguru.com/android-tutorial/android-mediaplayer-example-play-from-uri/

            try {
                mPlayer.setDataSource(getApplicationContext(), uri);
            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mPlayer.prepareAsync();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            }
    }

    void playRequest() {
        mPlayer.start();
        Log.i("In play request", "here I am");
    }

    void pauseRequest(){
        mPlayer.pause();
    }

    void stopRequest(){
        mPlayer.seekTo(0);
        mPlayer.pause();
    }

    public void exitRequest(){
        super.onDestroy();
        if (mPlayer != null){
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
        stopSelf();
    }
    //public void onPrepared(MediaPlayer mPlayer){


    //}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
