package com.hackathon.EmotionAnalysis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.EmotionAnalysis.model.Emotion;
import com.hackathon.EmotionAnalysis.model.EmotionResponse;
import com.hackathon.EmotionAnalysis.model.RatingResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class EmotionController {
    private static final String subscriptionKey = "e9a30b16e27d462db241e94f47e3754a";

    private static final String uriBase =
            "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
    private static List<String> imageWithFacesList = new ArrayList<>();
    private static final String faceAttributes = "age,gender,emotion";
    @GetMapping("/emotion")
    public RatingResponse getEmotion(@RequestParam String productId) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        List<EmotionResponse> responseArr = new ArrayList<>();
        for(String imageWithFaces : imageWithFacesList) {
            try {
                URIBuilder builder = new URIBuilder(uriBase);

                // Request parameters. All of them are optional.
                builder.setParameter("returnFaceId", "true");
                builder.setParameter("returnFaceLandmarks", "false");
                builder.setParameter("returnFaceAttributes", faceAttributes);

                // Prepare the URI for the REST API call.
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);

                // Request headers.
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

                // Request body.
                StringEntity reqEntity = new StringEntity(imageWithFaces);
                request.setEntity(reqEntity);

                // Execute the REST API call and get the response entity.
                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // Format and display the JSON response.
                    System.out.println("REST Response:\n");

                    String jsonString = EntityUtils.toString(entity).trim();
                    System.out.println(jsonString);
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (jsonString.charAt(0) == '[') {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        System.out.println(jsonString);
                        responseArr.addAll(Arrays.asList(objectMapper.readValue(jsonString, EmotionResponse[].class)));
                    } else if (jsonString.charAt(0) == '{') {
                        System.out.println(jsonString);
                        responseArr.addAll(Arrays.asList(objectMapper.readValue(jsonString, EmotionResponse.class)));
                    } else {
                        System.out.println(jsonString);
                        responseArr.add(objectMapper.readValue(jsonString, EmotionResponse.class));
                    }
                }
            } catch (Exception e) {
                // Display error message.
                System.out.println(e.getMessage());
            }
        }
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.setProductId(productId);
        ratingResponse.setRating(processResponse(responseArr));
        return ratingResponse;
    }

    private String processResponse(List<EmotionResponse> responsezList) {
        float sum =0;
        for(EmotionResponse response: responsezList) {
            int rating = getRating(response);
            System.out.println(rating);
            sum += rating;
        }
        System.out.println(sum);
        float avg = sum/5;
        System.out.println(avg);
        return String.valueOf(avg);
    }

    private int getRating(EmotionResponse response) {
        float happy, sad, neutral;
        Emotion e = response.getFaceAttributes().getEmotion();
        happy = e.getHappiness() + e.getSurprise();
        sad = e.getAnger() + e.getContempt() + e.getDisgust() + e.getFear();
        neutral = e.getNeutral();
        System.out.println(e);
        if (happy > 0.5) {
            return Rating.VERY_SATISFIED.getValue();
        } else if (sad > 0.5) {
            return Rating.VERY_UNSATISFIED.getValue();
        } else if (neutral > 0.5) {
            return Rating.NEUTRAL.getValue();
        } else {//return greatest of the three
            if (happy >= sad && happy >= neutral)
                return Rating.SATISFIED.getValue();
            else if (sad >= happy && sad >= neutral)
                return Rating.UNSATISFIED.getValue();
            else
                return Rating.NEUTRAL.getValue();
        }
    }

    public EmotionController() {
        imageWithFacesList.add("{\"url\":\"https://image.shutterstock.com/image-photo/face-angry-furious-male-on-260nw-536355550.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://image.shutterstock.com/z/stock-photo-portrait-of-angry-woman-standing-with-arms-folded-on-gray-background-312789179.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://www.superprof.co.in/images/teachers/teacher-home-drawing-classes-for-all-age-group-happy-faces-makes-happy-drawings.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Boy_Face_from_Venezuela.jpg/256px-Boy_Face_from_Venezuela.jpg\"}");
        //imageWithFacesList.add();

    }
}

enum Rating {
    VERY_UNSATISFIED(1),
    UNSATISFIED(2),
    NEUTRAL(3),
    SATISFIED(4),
    VERY_SATISFIED(5);

    int value;
    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
