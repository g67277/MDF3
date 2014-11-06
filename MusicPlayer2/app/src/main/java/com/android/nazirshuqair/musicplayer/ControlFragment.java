package com.android.nazirshuqair.musicplayer;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

/**
 * Created by nazirshuqair on 11/5/14.
 */

public class ControlFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "ControlFragment.TAG";
    public static final String SHUFFLE_CHECKED = "PlayService.SHUFFLE_CHECKED";
    public static final String REPEAT_CHECKED = "PlayService.REPEAT_CHECKED";

    public CheckBox shuffleCheck;
    public CheckBox repeatCheck;

    public static Button playPauseBtn;

    public static SharedPreferences pref;

    public ControlFragment() {
    }

    public static ControlFragment newInstance() {
        ControlFragment frag = new ControlFragment();
        return frag;
    }

    public interface MasterClickListener {
        public void btnPressed(View v);
    }

    private MasterClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof MasterClickListener) {
            mListener = (MasterClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_control, container, false);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        playPauseBtn = (Button) myFragmentView.findViewById(R.id.play_pause_btn);
        playPauseBtn.setOnClickListener(this);
        Button stopBtn = (Button) myFragmentView.findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(this);
        Button skipBtn = (Button) myFragmentView.findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(this);
        Button backBtn = (Button) myFragmentView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);

        shuffleCheck = (CheckBox) myFragmentView.findViewById(R.id.shuffle_btn);
        repeatCheck = (CheckBox) myFragmentView.findViewById(R.id.repeat_btn);
        shuffleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = pref.edit();

                if (repeatCheck.isChecked()){
                    repeatCheck.setChecked(false);
                }

                editor.putBoolean(SHUFFLE_CHECKED, isChecked);
                editor.putBoolean(REPEAT_CHECKED, false);

                editor.apply();
            }
        });
        repeatCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = pref.edit();

                if (shuffleCheck.isChecked()){
                    shuffleCheck.setChecked(false);
                }

                editor.putBoolean(REPEAT_CHECKED, isChecked);
                editor.putBoolean(SHUFFLE_CHECKED, false);
                editor.apply();
            }
        });
        //Connecting the ListView
        return myFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isShuffled()){
            shuffleCheck.setChecked(true);
        }else if (isRepeating()){
            repeatCheck.setChecked(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);



    }

    @Override
    public void onClick(View view) {
        mListener.btnPressed(view);
    }

    public static boolean isShuffled(){
        return pref.getBoolean(SHUFFLE_CHECKED, false);
    }

    public static boolean isRepeating(){
        return pref.getBoolean(REPEAT_CHECKED, false);
    }

}

