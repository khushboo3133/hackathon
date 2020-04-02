package com.hackathon.EmotionAnalysis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Noise {
    @JsonIgnore
    String noiseLevel;
    float value;

    public Noise(String noiseLevel, float value) {
        this.noiseLevel = noiseLevel;
        this.value = value;
    }
    public Noise() {

    }


}
