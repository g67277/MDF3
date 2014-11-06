package com.android.nazirshuqair.musicplayer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by nazirshuqair on 11/5/14.
 */
public class ArtFragment extends Fragment {

    public static final String TAG = "ArtFragment.TAG";

    ImageView songArt;
    TextView songLabel;

    public static ArtFragment newInstance() {
        ArtFragment frag = new ArtFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_art, container, false);

        songArt = (ImageView) myFragmentView.findViewById(R.id.song_art);
        songLabel = (TextView) myFragmentView.findViewById(R.id.art_label);

        //Connecting the ListView
        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

    }

    public void updateDisplay(int _currentSong){

        switch (_currentSong){
            case 1:
                songArt.setImageResource(R.drawable.dangerous);
                songLabel.setText("David Guetta - Dangerous");
                break;
            case 2:
                songArt.setImageResource(R.drawable.lovemeagain);
                songLabel.setText("John Newman - Love Me Again");
                break;
            case 3:
                songArt.setImageResource(R.drawable.feelinggood);
                songLabel.setText("Michael Buble - Feeling Good");
                break;
            case 4:
                songArt.setImageResource(R.drawable.whatmakesagoodman);
                songLabel.setText("The Heavy - What Makes a Good Man");
                break;
        }

        songArt.setScaleType(ImageView.ScaleType.FIT_XY);

    }

}
