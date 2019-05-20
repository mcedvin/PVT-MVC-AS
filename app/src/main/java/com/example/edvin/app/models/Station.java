package com.example.edvin.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Station implements Serializable {

    private Collection<Material> availableMaterials = new ArrayList<Material>();
    private String name;
    private Position position;

    private Date lastEmpty;

    public Station(String stationName, Position position){
        this.name = stationName;
        this.position = position;
    }
    public Station() {

    }
    public String getName(){
        return name;
    }
    public Position getPosition(){
        return position;
    }
    public Date getLastEmpty(){
        return lastEmpty;
    }
    public void setLastEmpty(){
        lastEmpty = new Date();
    }

    public String toString(){
        return name + " på position: " + position;
    }
}
