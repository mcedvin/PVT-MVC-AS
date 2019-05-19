package com.example.edvin.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Material implements Serializable {

    @SerializedName("materialType")
    private String name;

    public Material(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
