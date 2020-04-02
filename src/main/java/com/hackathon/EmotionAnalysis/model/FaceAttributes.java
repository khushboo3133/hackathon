package com.hackathon.EmotionAnalysis.model;

public class FaceAttributes {

    Emotion emotion;
    String gender;
    int age;

    public FaceAttributes() {

    }
    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FaceAttributes(Emotion emotion, String gender, int age) {
        this.emotion = emotion;
        this.gender = gender;
        this.age = age;
    }
}
