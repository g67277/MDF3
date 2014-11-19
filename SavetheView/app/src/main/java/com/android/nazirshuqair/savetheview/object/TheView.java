package com.android.nazirshuqair.savetheview.object;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by nazirshuqair on 11/18/14.
 */
public class TheView implements Serializable {

    private static final long serialVersionUID = -7791154359356162736L;

    public String olocationName;
    public String oImgDate;
    public String oImgUri;
    public double oLatitude;
    public double oLongitude;

    public void setoLongitude(double oLongitude) {
        this.oLongitude = oLongitude;
    }

    public void setoLatitude(double oLatitude) {
        this.oLatitude = oLatitude;
    }


    public double getoLongitude() {
        return oLongitude;
    }

    public double getoLatitude() {
        return oLatitude;
    }

    public String getOlocationName() {
        return olocationName;
    }

    public String getoImgDate() {
        return oImgDate;
    }

    public String getoImgUri() {
        return oImgUri;
    }

    public TheView(){
        olocationName = "";
        oImgDate = "";
        oImgUri = "";
        oLatitude = 0;
        oLongitude = 0;
    }

    public TheView(String _location, String _imgUri, String _imgDate, double _latitude, double _longtitude) {
        this.olocationName = _location;
        this.oImgUri = _imgUri;
        this.oImgDate = _imgDate;
        this.oLatitude = _latitude;
        this.oLongitude = _longtitude;
    }

    public void setData(TheView data) {
        olocationName = data.getOlocationName();
        oImgUri = data.getoImgUri();
        oImgDate = data.getoImgDate();
        oLatitude = data.getoLatitude();
        oLongitude = data.getoLongitude();
    }




}
