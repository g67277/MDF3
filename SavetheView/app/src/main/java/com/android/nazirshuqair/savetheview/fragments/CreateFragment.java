package com.android.nazirshuqair.savetheview.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nazirshuqair.savetheview.R;

/**
 * Created by nazirshuqair on 11/18/14.
 */
public class CreateFragment extends Fragment {


    //Tag to identify the fragment
    public static final String TAG = "CreateFragment.TAG";

    EditText locationEdit;
    ImageView camImg;
    TextView dateView;

    Uri imgUri;
    String imgDate;


    //This is to create a new instance of the fragment
    public static CreateFragment newInstance(){
        CreateFragment frag = new CreateFragment();
        return frag;
    }

    public interface MasterClickListener{
        public void pushData(String _locationName);
        public void takePic();
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
        View myFragmentView = inflater.inflate(R.layout.fragment_create, container, false);

        locationEdit = (EditText) myFragmentView.findViewById(R.id.location_edit);
        dateView = (TextView) myFragmentView.findViewById(R.id.date_edit);
        camImg = (ImageView) myFragmentView.findViewById(R.id.cam_img);

        camImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.takePic();
            }
        });

        return myFragmentView;
    }

    public void saveItem(){

        mListener.pushData(locationEdit.getText().toString());
    }

    public void updateDisplay(Uri _camImgUri, String _date){
        camImg.setImageBitmap(BitmapFactory.decodeFile(_camImgUri.getPath()));
        dateView.setText(_date);
    }


}
