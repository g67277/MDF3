package com.android.nazirshuqair.savetheview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.nazirshuqair.savetheview.object.TheView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazirshuqair on 11/18/14.
 */
public class MyMapFragment extends MapFragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener {

    public static final String TAG = "MyMapFragment.TAG";

    GoogleMap mMap = getMap();

    public List<TheView> coordinatesList = new ArrayList<TheView>();

    public void updateDisplay(List<TheView> _coordList, boolean _refresh){

         coordinatesList = _coordList;

        if (mMap != null){
            for (TheView theView : _coordList){
                Log.i("TESTING", theView.getOlocationName() + " " + theView.getoLatitude());

                mMap.addMarker(new MarkerOptions().position(new LatLng(theView.getoLatitude(), theView.getoLongitude())).title(theView.getOlocationName()));
            }
        }


        Log.i("TESTING", "THIS WORKED!!");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMap = getMap();
        //mMap.addMarker(new MarkerOptions().position(new LatLng(28.590647,-81.304510)).title("MDVBS Faculty Offices"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(28.591748,-81.305910)).title("Crispers"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(28.595842,-81.304188)).title("Full Sail Live"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(28.596591,-81.301302)).title("Advising"));

        for (TheView theView : coordinatesList){
            Log.i("TESTING", theView.getOlocationName() + " " + theView.getoLatitude());

            mMap.addMarker(new MarkerOptions().position(new LatLng(theView.getoLatitude(), theView.getoLongitude())).title(theView.getOlocationName()));
        }

        mMap.setInfoWindowAdapter(new MarkerAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.593770, -81.303797), 16));
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Marker Clicked")
                .setMessage("You clicked on: " + marker.getTitle())
                .setPositiveButton("Close", null)
                .setNegativeButton("Remove", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                    }
                })
                .show();
    }

    @Override
    public void onMapClick(final LatLng location) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Map Clicked")
                .setMessage("Add new marker here?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMap.addMarker(new MarkerOptions().position(location).title("New Marker"));
                    }
                })
                .show();
    }

    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

        TextView mText;

        public MarkerAdapter() {
            mText = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }

}
