package com.android.nazirshuqair.f1schedule;

import java.io.Serializable;

/**
 * Created by nazirshuqair on 11/11/14.
 */
public class Race implements Serializable {

    //required to be able to save serializable objects
    private static final long serialVersionUID = -7791154359356162736L;

    public String raceLocation;
    public String raceCircuitName;
    public String raceDate;
    public String raceLapNum;



    public String getRaceLapNum() {
        return raceLapNum;
    }


    public String getRaceLocation() {
        return raceLocation;
    }


    public String getRaceCircuitName() {
        return raceCircuitName;
    }


    public String getRaceDate() {
        return raceDate;
    }


    public Race(){
        raceLocation = "";
        raceCircuitName = "";
        raceDate = "";
        raceLapNum = "";
    }

    public Race(String location, String circuit, String date, String lap) {
        this.raceLocation = location;
        this.raceCircuitName = circuit;
        this.raceDate = date;
        this.raceLapNum = lap;
    }

    public void setData(Race data) {
        raceLocation = data.getRaceLocation();
        raceCircuitName = data.getRaceCircuitName();
        raceDate = data.getRaceDate();
        raceLapNum = data.raceLapNum;
    }

}
