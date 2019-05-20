package com.example.edvin.app.models;

import java.io.Serializable;
import java.util.Date;

abstract public class Schedule implements Serializable {

    private Date date;

    public Schedule(Date date){
        this.date = date;
    }
    public Schedule(){

    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return date.toString();
    }
}