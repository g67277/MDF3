package com.android.nazirshuqair.savetheview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.nazirshuqair.savetheview.object.TheView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazirshuqair on 11/18/14.
 */
public class MyMapFragment extends MapFragment implements GoogleMap.OnInfoWindowClickListener, LocationListener, GoogleMap.OnMapLongClickListener {

    public static final String TAG = "MyMapFragment.TAG";
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    GoogleMap mMap;
    LocationManager locManager;
    double cLatitude;
    double cLongitude;
    double picLat;
    double picLong;
    boolean gpsEnabled;

    public List<TheView> coordinatesList = new ArrayList<TheView>();

    public void updateDisplay(List<TheView> _coordList, boolean _refresh){

         coordinatesList = _coordList;

        if (mMap != null){
            for (TheView theView : _coordList){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(theView.getoLatitude(), theView.getoLongitude()))
                        .title(theView.getOlocationName())
                        .snippet(theView.getoImgDate()));

                        //.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeFile(Uri.parse(theView.getoImgUri()).getPath()))));
            }
        }


        Log.i("TESTING", "THIS WORKED!!");
    }

    public interface MapMasterClickListener{

        public void retriveData(String _imgName) throws IOException, ClassNotFoundException;
        public void pushSelectedLocation(LatLng _latLng);
    }

    MapMasterClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof MapMasterClickListener) {
            mListener = (MapMasterClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        mMap = getMap();
        enableGps();



        for (TheView theView : coordinatesList){

            //This is to include a picture in the marker
            /*ExifInterface exif = null;
            try {
                exif = new ExifInterface(Uri.parse(theView.getoImgUri()).getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] imageData=exif.getThumbnail();
            Bitmap thumbnail= BitmapFactory.decodeByteArray(imageData,0,imageData.length);*/

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(theView.getoLatitude(), theView.getoLongitude()))
                    .title(theView.getOlocationName())
                    .snippet(theView.getoImgDate()));

            picLat = theView.getoLatitude();
            picLong = theView.getoLongitude();
                    //.icon(BitmapDescriptorFactory.fromBitmap(thumbnail)));
        }

        mMap.setInfoWindowAdapter(new MarkerAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(this);
        //mMap.setOnMapClickListener(this);
        if (gpsEnabled) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cLatitude, cLongitude), 16));
        }else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(picLat, picLong), 16));
        }

        locManager.removeUpdates(this);
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {

        try {
            mListener.retriveData(marker.getSnippet());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        mListener.pushSelectedLocation(latLng);
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

    //-----------------------------Location Code-------------------------------------

    private void enableGps(){
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null){
                cLatitude = loc.getLatitude();
                cLongitude = loc.getLongitude();
            }
            gpsEnabled = true;
        }else {
            gpsEnabled = false;
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Unavailable")
                    .setMessage("Please enabled GPS in the system settings")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        cLatitude = location.getLatitude();
        cLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
