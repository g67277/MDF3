package com.android.nazirshuqair.f1schedule;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by nazirshuqair on 11/11/14.
 */
public class EditFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "EditFragment.TAG";

    EditText locationEdit;
    EditText circuitEdit;
    EditText dateEdit;
    EditText lapsEdit;


    //This is to create a new instance of the fragment
    public static EditFragment newInstance() {
        EditFragment frag = new EditFragment();
        return frag;
    }

    public interface MasterClickListener {
        public void pushData(String _inputLocation, String _inputCircuit, String _inputDate, String _inputLaps);
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
        View myFragmentView = inflater.inflate(R.layout.edit_fragment, container, false);
        //Connecting the edittext
        locationEdit = (EditText) myFragmentView.findViewById(R.id.location_text_edit);
        circuitEdit = (EditText) myFragmentView.findViewById(R.id.circuit_text_edit);
        dateEdit = (EditText) myFragmentView.findViewById(R.id.date_text_edit);
        lapsEdit = (EditText) myFragmentView.findViewById(R.id.lapsNum_text_edit);

        return myFragmentView;
    }

    public void saveItem(){

        mListener.pushData(locationEdit.getText().toString(), circuitEdit.getText().toString(),
                dateEdit.getText().toString(), lapsEdit.getText().toString());
    }

    public void clearForm(){
        locationEdit.setText("");
        circuitEdit.setText("");
        dateEdit.setText("");
        lapsEdit.setText("");
    }
}
