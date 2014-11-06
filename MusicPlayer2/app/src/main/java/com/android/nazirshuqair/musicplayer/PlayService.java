package com.android.nazirshuqair.musicplayer;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PlayService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;
    public static final String PREF_IS_RUNNING = "PlayService.PREF_IS_RUNNING";

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
        setRunning(false);
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        player.reset();

        String path = null;

        if (ControlFragment.isShuffled()){
            int currentIndex = musicIndex;
            randomInt();
            if (musicIndex == currentIndex){
                randomInt();
            }
            path = musicList.get(musicIndex);
        }else if(ControlFragment.isRepeating()){
            path = musicList.get(musicIndex);
        }else {
            if (musicIndex < musicList.size() - 1) {

                path = musicList.get(++musicIndex);
            } else {
                musicIndex = 0;
                path = musicList.get(musicIndex);
            }
        }
        playAudio(path);
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

        setRunning(true);

        musicIndex = index;

        musicList = songList;

        if(player == null) {
            // Easy way: mPlayer = MediaPlayer.create(this, R.raw.something_elated);
            // Easy way doesn't require a call to prepare or prepareAsync. Only works for resources.

            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnPreparedListener(this);

            try {
                player.setDataSource(this, Uri.parse(musicUri));
            } catch(IOException e) {
                e.printStackTrace();

                player.release();
                player = null;
            }
        }

        player.prepareAsync();


    }

    private void playAudio(String filename) {

        Uri musicUri = Uri.parse(filename);
        try {
            player.setDataSource(getApplicationContext(), musicUri);
            player.prepare();
            player.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showNotification(String title) {


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

    private void setRunning(boolean running) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(PREF_IS_RUNNING, running);
        editor.apply();
    }

    public static boolean isRunning(Context ctx) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        return pref.getBoolean(PREF_IS_RUNNING, false);
    }

    public void stopPlayer(){
        player.stop();
        player.release();
        player = null;
        setRunning(false);
    }

    public void pausePlayer(){
        player.stop();
        setRunning(false);
    }

    public void fBControles(int index){
        musicIndex = index;
        player.reset();
        playAudio(musicList.get(index));
    }

    public void randomInt(){
        Random r = new Random();
        musicIndex = r.nextInt(musicList.size());
    }


}