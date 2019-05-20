package com.example.edvin.app.models;

import java.io.Serializable;

public class Position implements Serializable {

    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position() {

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            return x == ((Position) obj).x && y == ((Position) obj).y;
        }
        return false;
    }

    public String toString() {
        return "" + x + " " + y;
    }
}