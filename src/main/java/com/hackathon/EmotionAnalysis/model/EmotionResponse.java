package com.hackathon.EmotionAnalysis.model;

import com.hackathon.EmotionAnalysis.model.FaceAttributes;
import com.hackathon.EmotionAnalysis.model.FaceRectangle;

public class EmotionResponse {

    String faceId;
    FaceAttributes faceAttributes;
    FaceRectangle faceRectangle;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public FaceAttributes getFaceAttributes() {
        return faceAttributes;
    }

    public void setFaceAttributes(FaceAttributes faceAttributes) {
        this.faceAttributes = faceAttributes;
    }

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public EmotionResponse(String faceId, FaceAttributes faceAttributes, FaceRectangle faceRectangle) {
        this.faceId = faceId;
        this.faceAttributes = faceAttributes;
        this.faceRectangle = faceRectangle;
    }
    public EmotionResponse(){

    }
}
