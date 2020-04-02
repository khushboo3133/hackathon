package com.hackathon.EmotionAnalysis.model;

public class Exposure {
    int value;
    String exposureLevel;

    public Exposure(){}
    public Exposure(int value, String exposureLevel) {
        this.value = value;
        this.exposureLevel = exposureLevel;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getExposureLevel() {
        return exposureLevel;
    }

    public void setExposureLevel(String exposureLevel) {
        this.exposureLevel = exposureLevel;
    }
}
