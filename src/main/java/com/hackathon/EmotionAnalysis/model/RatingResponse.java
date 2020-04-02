package com.hackathon.EmotionAnalysis.model;

public class RatingResponse {
    String productId;
    String rating;

    public RatingResponse(){}
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public RatingResponse(String productId, String rating) {
        this.productId = productId;
        this.rating = rating;
    }
}
