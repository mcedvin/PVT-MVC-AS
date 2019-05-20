package com.example.edvin.app.map;

import com.example.edvin.app.models.Material;
import com.example.edvin.app.models.Schedule;
import java.util.Date;

public class MaterialSchedule extends Schedule {

    private Material material;

    public MaterialSchedule(Date date, Material material){
        super(date);
        this.material = material;
    }
    public MaterialSchedule(){

    }
}
