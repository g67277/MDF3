package com.android.nazirshuqair.f1schedule;

import java.io.Serializable;

/**
 * Created by nazirshuqair on 11/12/14.
 */
public class Race implements Serializable {

    private static final long serialVersionUID = -7791154359356162736L;

    public String raceLocation;
    public String raceCircuitName;
    public String raceDate;
    public String raceLapNum;



    public String getRaceLapNum() {
        return raceLapNum;
    }

    public void setRaceLapNum(String raceLapNum) {
        this.raceLapNum = raceLapNum;
    }

    public String getRaceLocation() {
        return raceLocation;
    }

    public void setRaceLocation(String raceLocation) {
        this.raceLocation = raceLocation;
    }

    public String getRaceCircuitName() {
        return raceCircuitName;
    }

    public void setRaceCircuitName(String raceCircuitName) {
        this.raceCircuitName = raceCircuitName;
    }

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
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
