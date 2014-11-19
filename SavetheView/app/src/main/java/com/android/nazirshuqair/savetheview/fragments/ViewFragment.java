package com.android.nazirshuqair.savetheview.fragments;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nazirshuqair.savetheview.R;

/**
 * Created by nazirshuqair on 11/18/14.
 */
public class ViewFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "ViewFragment.Tag";

    private static final String ARG_LOCATION = "ViewFragment.ARG_LOCATION";
    private static final String ARG_IMG = "ViewFragment.ARG_IMG";
    private static final String ARG_DATE = "ViewFragment.ARG_DATE";

    TextView locationView;
    TextView imgDateView;
    ImageView camImg;

    //This is to create a new instance of the fragment
    public static ViewFragment newInstance(String _locationName, String _camImgUri, String _entryDate) {
        ViewFragment frag = new ViewFragment();

        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, _locationName);
        args.putString(ARG_DATE, _entryDate);
        args.putString(ARG_IMG, _camImgUri);

        frag.setArguments(args);


        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_view, container, false);
        //Connecting the ListView
        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_LOCATION)){
            setDisplay(args.getString(ARG_LOCATION),
                    Uri.parse(args.getString(ARG_IMG)),
                    args.getString(ARG_DATE)
                    );
        }

    }

    public void setDisplay(String _locationName, Uri _camImgUri, String _entryDate){

        //Connecting the edittext
        locationView = (TextView) getView().findViewById(R.id.location_view);
        imgDateView = (TextView) getView().findViewById(R.id.date_view);
        camImg = (ImageView) getView().findViewById(R.id.cam_img_view);

        locationView.setText(_locationName);
        imgDateView.setText(_entryDate);
        camImg.setImageBitmap(BitmapFactory.decodeFile(_camImgUri.getPath()));

    }
}
