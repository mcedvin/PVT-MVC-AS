package com.example.edvin.app.models;

import java.util.Date;

abstract public class Schedule {

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
}