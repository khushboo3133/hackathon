package com.hackathon.EmotionAnalysis.model;

public class Emotion {
               float contempt;
                float surprise;
               float happiness;
               float neutral;
               float sadness;
               float disgust;
               float anger;
               float fear;

    public float getContempt() {
        return contempt;
    }

    public void setContempt(float contempt) {
        this.contempt = contempt;
    }

    public float getSurprise() {
        return surprise;
    }

    public void setSurprise(float surprise) {
        this.surprise = surprise;
    }

    public float getHappiness() {
        return happiness;
    }

    public void setHappiness(float happiness) {
        this.happiness = happiness;
    }

    public float getNeutral() {
        return neutral;
    }

    public void setNeutral(float neutral) {
        this.neutral = neutral;
    }

    public float getSadness() {
        return sadness;
    }

    public void setSadness(float sadness) {
        this.sadness = sadness;
    }

    public float getDisgust() {
        return disgust;
    }

    public void setDisgust(float disgust) {
        this.disgust = disgust;
    }

    public float getAnger() {
        return anger;
    }

    public void setAnger(float anger) {
        this.anger = anger;
    }

    public float getFear() {
        return fear;
    }

    public void setFear(float fear) {
        this.fear = fear;
    }
    public Emotion(){}

    public Emotion(float contempt, float surprise, float happiness, float neutral, float sadness, float disgust, float anger, float fear) {
        this.contempt = contempt;
        this.surprise = surprise;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.disgust = disgust;
        this.anger = anger;
        this.fear = fear;
    }
}
