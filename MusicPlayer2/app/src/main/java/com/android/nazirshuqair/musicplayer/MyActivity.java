package com.android.nazirshuqair.musicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MyActivity extends Activity implements ServiceConnection, ControlFragment.MasterClickListener {

    private static final String PLAY_STATE = "MyActivity.PLAY_STATE";

    ArrayList<String> songArray = new ArrayList<String>();
    boolean playing = false;
    public static int currentSong = 1;
    TextView musicLabel;
    Bundle bundle;
    Intent intent;
    PlayService myService;
    ArtFragment frag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if(savedInstanceState == null) {
            ControlFragment frag = ControlFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.container2, frag, ControlFragment.TAG).commit();

            frag2 = ArtFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.container1, frag2, ArtFragment.TAG).commit();

        }

        playing = PlayService.isRunning(this);

        songArray.add(0, "android.resource://" + getPackageName() + "/raw/one");
        songArray.add(1, "android.resource://" + getPackageName() + "/raw/david_guetta_dangerous");
        songArray.add(2, "android.resource://" + getPackageName() + "/raw/john_newman_love_me_again");
        songArray.add(3, "android.resource://" + getPackageName() + "/raw/two");

    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = new Intent(this, PlayService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        //Intent intent = new Intent(this, SimpleService.class);
        //bindService(intent, this, Context.BIND_AUTO_CREATE);

    }



    @Override
    protected void onStop() {
        super.onStop();

        //unbindService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        playing = PlayService.isRunning(this);

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        PlayService.BoundServiceBinder binder = (PlayService.BoundServiceBinder) service;
        myService = binder.getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        unbindService(this);

    }

    public void updateNotification(){
        switch (currentSong){
            case 1:
                myService.showNotification("David Guetta - Dangerous");
                break;
            case 2:
                myService.showNotification("John Newman - Love Me Again");
                break;
            case 3:
                myService.showNotification("Michael Buble - Feeling Good");
                break;
            case 4:
                myService.showNotification("The Heavy - What Makes a Good Man");
                break;

        }
    }

    @Override
    public void btnPressed(View v) {
        View view1 = v;


        boolean isPlaying = PlayService.isRunning(this);

        if(v.getId() == R.id.play_pause_btn) {
            if (!isPlaying) {
                myService.playSong(songArray.get(currentSong - 1), songArray, currentSong - 1);
                ControlFragment.playPauseBtn.setText("Pause");
                updateNotification();
            }else {
                ControlFragment.playPauseBtn.setText("Play");
                myService.pausePlayer();
            }
        }else if(v.getId() == R.id.stop_btn) {
            if (isPlaying) {
                myService.stopPlayer();
                currentSong = 1;
                ControlFragment.playPauseBtn.setText("Play");
            }
        } else if(v.getId() == R.id.skip_btn){
            if (isPlaying) {

                if (ControlFragment.isShuffled()){
                    int currentIndex = currentSong;
                    randomInt();
                    if (currentIndex == currentSong){
                        randomInt();
                    }
                }else if (ControlFragment.isRepeating()){
                }else {
                    currentSong = myService.musicIndex + 1;
                    currentSong++;
                    if (currentSong >= songArray.size() + 1) {
                        currentSong = 1;
                    }
                }
                myService.fBControles(currentSong - 1);
                updateNotification();

            }else {
                currentSong++;
                if (currentSong >= songArray.size() + 1) {
                    currentSong = 1;
                }
            }
        }else if(v.getId() == R.id.back_btn) {
            if (isPlaying) {
                currentSong = myService.musicIndex + 1;
                currentSong--;
                if (currentSong <= 0) {
                    currentSong = songArray.size();
                }
                myService.fBControles(currentSong - 1);
                updateNotification();
            }else {
                currentSong--;
                if (currentSong < 0) {
                    currentSong = songArray.size();
                }
            }
        }


        if (frag2 == null){
            frag2 = ArtFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.container1, frag2, ArtFragment.TAG).commit();
            frag2.updateDisplay(currentSong);
        }
    }

    public void randomInt(){
        Random r = new Random();
        currentSong = r.nextInt(songArray.size()) + 1;
    }

}