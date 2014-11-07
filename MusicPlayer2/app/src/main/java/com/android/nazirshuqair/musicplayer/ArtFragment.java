package com.android.nazirshuqair.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nazirshuqair on 11/5/14.
 */
public class ArtFragment extends Fragment {

    public static final String TAG = "ArtFragment.TAG";
    private static final String ARG_INDEX = "ArtFragment.ARG_INDEX";

    ImageView songArt;
    TextView songLabel;

    public static ArtFragment newInstance() {
        ArtFragment frag = new ArtFragment();
        return frag;
    }

    public static ArtFragment newInstance(int _currentSong){
        ArtFragment frag = new ArtFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, _currentSong);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_art, container, false);

        //Connecting the ListView
        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_INDEX)){
            updateDisplay(args.getInt(ARG_INDEX));
        }
    }

    public void updateDisplay(int _currentSong){

        songArt = (ImageView) getView().findViewById(R.id.song_art);
        songLabel = (TextView) getView().findViewById(R.id.art_label);

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
