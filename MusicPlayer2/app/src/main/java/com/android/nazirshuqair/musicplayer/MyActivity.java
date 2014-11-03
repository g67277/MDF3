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

public class MyActivity extends Activity implements OnClickListener, ServiceConnection {

    private static final String PLAY_STATE = "MyActivity.PLAY_STATE";

    ArrayList<String> songArray = new ArrayList<String>();
    boolean playing = false;
    int currentSong = 1;
    TextView musicLabel;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        songArray.add(0, "android.resource://" + getPackageName() + "/raw/david_guetta_dangerous");
        songArray.add(1, "android.resource://" + getPackageName() + "/raw/john_newman_love_me_again");
        songArray.add(2, "android.resource://" + getPackageName() + "/raw/michael_buble_feeling_good");
        songArray.add(3, "android.resource://" + getPackageName() + "/raw/the_heavy_what_makes_a_good_man");

        findViewById(R.id.play_btn).setOnClickListener(this);
        findViewById(R.id.stop_btn).setOnClickListener(this);
        findViewById(R.id.skip_btn).setOnClickListener(this);
        findViewById(R.id.pause_btn).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        musicLabel = (TextView) findViewById(R.id.music_label);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PlayService.class);
        bundle = new Bundle();

        if(v.getId() == R.id.play_btn) {
            if (!playing) {
                startService(intent);
                bindService(intent, this, Context.BIND_AUTO_CREATE);
                bundle.putBoolean(PLAY_STATE, true);
                playing = true;
            }
        } else if(v.getId() == R.id.pause_btn){
            if (playing) {
                unbindService(this);
                stopService(intent);
                bundle.putBoolean(PLAY_STATE, false);
                playing = false;
            }
        }else if(v.getId() == R.id.stop_btn) {
            if (playing) {
                unbindService(this);
                stopService(intent);
                bundle.putBoolean(PLAY_STATE, false);
                playing = false;
                currentSong = 1;
                musicLabel.setText("Nothing Playing...");
            }
        } else if(v.getId() == R.id.skip_btn){
            if (playing) {
                unbindService(this);
                stopService(intent);
                currentSong++;
                if (currentSong >= songArray.size() + 1) {
                    currentSong = 1;
                }
                startService(intent);
                bindService(intent, this, Context.BIND_AUTO_CREATE);
            }else {
                currentSong++;
                if (currentSong >= songArray.size() + 1) {
                    currentSong = 1;
                }
            }
        }else if(v.getId() == R.id.back_btn) {
            if (playing) {
                unbindService(this);
                stopService(intent);
                currentSong--;
                if (currentSong <= 0) {
                    currentSong = songArray.size();
                }
                startService(intent);
                bindService(intent, this, Context.BIND_AUTO_CREATE);
            }else {
                currentSong--;
                if (currentSong < 0) {
                    currentSong = songArray.size();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        PlayService.BoundServiceBinder binder = (PlayService.BoundServiceBinder) service;
        PlayService myService = binder.getService();

        switch (currentSong){
            case 1:
                myService.showToast("David Guetta - Dangerous");
                musicLabel.setText("David Guetta - Dangerous");
                break;
            case 2:
                myService.showToast("John Newman - Love Me Again");
                musicLabel.setText("John Newman - Love Me Again");
                break;
            case 3:
                myService.showToast("Michael Buble - Feeling Good");
                musicLabel.setText("Michael Buble - Feeling Good");
                break;
            case 4:
                myService.showToast("The Heavy - What Makes a Good Man");
                musicLabel.setText("The Heavy - What Makes a Good Man");
                break;

        }

        myService.playSong(songArray.get(currentSong - 1), songArray, currentSong - 1);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}