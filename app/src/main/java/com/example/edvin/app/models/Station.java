package com.example.edvin.app.models;


import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Station implements Serializable {

    private String stationName;
    @SerializedName("pos")
    private Position position;
    @SerializedName("postNumber")
    private String zipCode;
    private int ZIPCODE_WITH_SPACE;

    private String area;
    private Collection<Material> availableMaterials = new ArrayList<Material>();
    private CleaningSchedule cleaningSchedule;
    private Collection<MaterialSchedule> materialSchedules = new ArrayList<MaterialSchedule>();

    public Station(String stationName, String zipCode, Position position) {
        this.zipCode = zipCode;
        this.stationName = stationName;
        this.position = position;
    }

    public Station() {

    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStationName() {
        return stationName;
    }

    public String getZipCode() {
        if (zipCode.length() == ZIPCODE_WITH_SPACE) {
            return zipCode;
        } else {
            return zipCode.substring(0,3) + " " + zipCode.substring(3);
        }
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Collection<Material> getAvailableMaterials() {
        return availableMaterials;
    }

    public void setAvailableMaterials(Collection<Material> availableMaterials) {
        this.availableMaterials = availableMaterials;
    }

    public void addAvailableMaterial(Material m) {
        availableMaterials.add(m);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public CleaningSchedule getCleaningSchedule() {
        return cleaningSchedule;
    }

    public void setCleaningSchedule(CleaningSchedule cleaningSchedule) {
        this.cleaningSchedule = cleaningSchedule;
    }

    public Collection<MaterialSchedule> getMaterialSchedules() {
        return materialSchedules;
    }

    public void setMaterialSchedules(Collection<MaterialSchedule> materialSchedules) {
        this.materialSchedules = materialSchedules;
    }

    public String getTitle() {
        String[] arr = stationName.split("/|,");
        return arr[0].trim();
    }

    @Override
    public String toString() {
        return getTitle() + ", " + getZipCode() + " " + area;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Station) {
            if (stationName.equals(((Station) other).getStationName())) {
                return true;
            }
        }
        return false;
    }
}