package com.example.edvin.app.models;

import java.util.Date;

public class MaterialSchedule extends Schedule {

    private Material material;

    public MaterialSchedule(Date date, Material material) {
        super(date);
        this.material = material;
    }

    public MaterialSchedule() {

    }

    @Override
    public String toString() {
        return material + super.toString();
    }

}
