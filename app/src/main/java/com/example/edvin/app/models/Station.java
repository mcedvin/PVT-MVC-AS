package com.example.edvin.app.models;


import com.example.edvin.app.map.MaterialSchedule;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Station implements Serializable {

    private String stationName;
    @SerializedName("pos")
    private Position position;
    private Collection<Material> availableMaterials = new ArrayList<Material>();
    private CleaningSchedule cleaningSchedule;
    private Collection<MaterialSchedule> materialSchedules = new ArrayList<MaterialSchedule>();

    public Station(String stationName, Position position) {
        this.stationName = stationName;
        this.position = position;
    }

    public Station() {

    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Collection<Material> getAvailableMaterials() {
        return availableMaterials;
    }

    public void setAvailableMaterials(Collection<Material> availableMaterials){
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
}