package com.android.nazirshuqair.savetheview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.android.nazirshuqair.savetheview.fragments.CreateFragment;
import com.android.nazirshuqair.savetheview.fragments.ViewFragment;
import com.android.nazirshuqair.savetheview.object.TheView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nazirshuqair on 11/18/14.
 */
public class CreateViewActivity extends Activity implements CreateFragment.MasterClickListener, LocationListener {

    private static final int REQUEST_TAKE_PICTURE = 0x01001;
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    TheView mTheView;
    Uri camImgUri;
    double imgLatitude;
    double imgLongitude;
    LocationManager locManager;
    String imageName;
    boolean gpsEnabled;
    boolean coordsExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createview);

        //enables the back button in the action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //changes the action bar color
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff113cd8")));
        locManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        Intent intent = getIntent();

        if (intent.getBooleanExtra("create", true)){
            if(savedInstanceState == null) {
                if (intent.getBooleanExtra("selected", false)){
                    coordsExist = true;
                    imgLatitude = intent.getDoubleExtra("lat", 0);
                    imgLongitude = intent.getDoubleExtra("lng", 0);
                }else {
                    coordsExist = false;
                }
                CreateFragment frag = CreateFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.activity_createview, frag, CreateFragment.TAG).commit();
            }
        }else {

            if(savedInstanceState == null) {

                ViewFragment frag = ViewFragment.newInstance(intent.getStringExtra("locationName"),
                        intent.getStringExtra("imgUri"),
                        intent.getStringExtra("imgDate"));

                getFragmentManager().beginTransaction().replace(R.id.activity_createview, frag, ViewFragment.TAG).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();

        if (intent.getBooleanExtra("create", true)) {
            MenuItem saveItem = menu.add("Save");
            saveItem.setShowAsAction(1);

            saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    CreateFragment frag = (CreateFragment) getFragmentManager().findFragmentByTag(CreateFragment.TAG);

                    frag.saveItem();

                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    //Write the object to storage
    private void writeToFile(TheView _data, String _filename) throws IOException {

        File mydir = this.getDir("mydir", Context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, _filename); //Getting a file within the dir.
        FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
        ObjectOutputStream oos = new ObjectOutputStream(out);

        if (mTheView == null){
            mTheView = new TheView();
        }
        mTheView.setData(_data);
        oos.writeObject(mTheView);
        oos.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (camImgUri != null){
            CreateFragment frag = (CreateFragment) getFragmentManager().findFragmentByTag(CreateFragment.TAG);
            if (frag !=null){
                frag.updateDisplay(camImgUri, imageName);
            }
            addImageToGallery(camImgUri);
        }
    }

    @Override
    public void pushData(String _locationName) {

        TheView theView = new TheView(_locationName, camImgUri.toString(), imageName, imgLatitude, imgLongitude);
        try {
            writeToFile(theView, imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void takePic() {

        if (!coordsExist){
            enableGps();
            locManager.removeUpdates(this);
        }
        if (gpsEnabled  || coordsExist) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camImgUri = getImageUri();
            if (camImgUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, camImgUri);
            }
            startActivityForResult(intent, REQUEST_TAKE_PICTURE);
        }

    }

    private void addImageToGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        sendBroadcast(scanIntent);
    }

    private Uri getImageUri() {

        imageName = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date(System.currentTimeMillis()));

        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File appDir = new File(imageDir, "CameraExample");
        appDir.mkdirs();

        File image = new File(appDir, imageName + ".jpg");
        try {
            image.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(image);
    }

    //-----------------------------Location Code-------------------------------------

    private void enableGps(){
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null){
                imgLatitude = loc.getLatitude();
                imgLongitude = loc.getLongitude();
            }
            gpsEnabled = true;
        }else {
            gpsEnabled = false;
            new AlertDialog.Builder(this)
                    .setTitle("GPS Unavailable")
                    .setMessage("Please enabled GPS in the system settings to take a picture")
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

        imgLatitude = location.getLatitude();
        imgLongitude = location.getLongitude();
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
