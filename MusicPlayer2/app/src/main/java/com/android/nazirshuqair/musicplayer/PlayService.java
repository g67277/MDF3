package com.android.nazirshuqair.musicplayer;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;


public class PlayService extends Service implements MediaPlayer.OnPreparedListener{

    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;

    MediaPlayer player;
    int musicIndex = 0;

    ArrayList<String> musicList;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //int songToPlay = intent.getIntExtra("MUSIC_NUM", 0);
        //Uri testing = Uri.parse(songArray.get(songToPlay));

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        player.stop();
        player.release();
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
    }



    public class BoundServiceBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BoundServiceBinder();
    }

    public void playSong(String musicUri, ArrayList<String> songList, int index){

        musicIndex = index;

        musicList = songList;

        if(player == null) {
            // Easy way: mPlayer = MediaPlayer.create(this, R.raw.something_elated);
            // Easy way doesn't require a call to prepare or prepareAsync. Only works for resources.

            player = new MediaPlayer();

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnPreparedListener(this);

            try {
                player.setDataSource(this, Uri.parse(musicUri));
            } catch(IOException e) {
                e.printStackTrace();

                player.release();
                player = null;
            }

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.stop();
                    mp.release();

                    String path = musicList.get(musicIndex);
                    playAudio(path);

                }
            });

        }

        player.prepareAsync();
    }

    private void playAudio(String filename) {
        try {
            player.setDataSource(filename);
            player.prepare();
            player.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showToast(String title) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Playing...");
        builder.setContentText(title);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setContentIntent(lunchHome());

        startForeground(FOREGROUND_NOTIFICATION, builder.build());

    }

    public PendingIntent lunchHome(){


        Intent intent = new Intent(this, MyActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, REQUEST_NOTIFY_LAUNCH, intent, 0);


        return pIntent;
    }

}