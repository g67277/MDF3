package com.android.nazirshuqair.f1schedule;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nazirshuqair on 10/14/14.
 */
public class ViewFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "ViewFragment.TAG";

    private static final String ARG_LOCATION = "ViewFragment.ARG_LOCATION";
    private static final String ARG_CIRCUIT = "ViewFragment.ARG_CIRCUIT";
    private static final String ARG_DATE = "ViewFragment.ARG_DATE";
    private static final String ARG_LAPS = "ViewFragment.ARG_LAPS";
    private static final String ARG_POSITION = "ViewFragment.ARG_POSITION";

    TextView locationView;
    TextView circuitView;
    TextView dateView;
    TextView lapsView;

    //This is to create a new instance of the fragment
    public static ViewFragment newInstance(String _location, String _circuit, String _date, String _laps, int _position) {
        ViewFragment frag = new ViewFragment();

        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, _location);
        args.putString(ARG_CIRCUIT, _circuit);
        args.putString(ARG_DATE, _date);
        args.putString(ARG_LAPS, _laps);
        args.putInt(ARG_POSITION, _position);
        frag.setArguments(args);


        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.view_fragment, container, false);
        //Connecting the ListView
        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_LOCATION)){
            setDisplay(args.getString(ARG_LOCATION),
                    args.getString(ARG_CIRCUIT),
                    args.getString(ARG_DATE),
                    args.getString(ARG_LAPS));
        }

    }

    public void setDisplay(String _location, String _circuit, String _date, String _laps){

        //Connecting the edittext
        locationView = (TextView) getView().findViewById(R.id.location_text_view);
        circuitView = (TextView) getView().findViewById(R.id.circuit_text_view);
        dateView = (TextView) getView().findViewById(R.id.date_text_view);
        lapsView = (TextView) getView().findViewById(R.id.lapsNum_text_view);

        locationView.setText(_location);
        circuitView.setText(_circuit);
        dateView.setText(_date);
        lapsView.setText(_laps);

    }
}
