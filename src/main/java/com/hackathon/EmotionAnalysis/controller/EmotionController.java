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

    private String processResponse(List<EmotionResponse> responszList) {
        float sum =0;
        for(EmotionResponse response: responszList) {
            int rating = getRating(response);
            System.out.println(rating);
            sum += rating;
        }
        System.out.println(sum);
        float avg = sum/responszList.size();
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
        if (happy > 0.3){
            return Rating.VERY_SATISFIED.getValue();
        } else if (sad > 0.3) {
            return Rating.VERY_UNSATISFIED.getValue();
        } else if (happy> 0.05 && happy >= sad ) {
            return Rating.SATISFIED.getValue();
        } else if (sad > 0.05 && sad >= happy)
            return Rating.UNSATISFIED.getValue();
        else {
            return Rating.NEUTRAL.getValue();
        }
    }

    public EmotionController() {
        imageWithFacesList.add("{\"url\":\"https://static1.bigstockphoto.com/6/2/2/large2/226601029.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://image.shutterstock.com/image-photo/face-angry-furious-male-on-260nw-536355550.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Boy_Face_from_Venezuela.jpg/256px-Boy_Face_from_Venezuela.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://image.shutterstock.com/image-photo/face-angry-furious-male-on-260nw-536355550.jpg\"}");
        imageWithFacesList.add("{\"url\":\"http://parentpumpradio.com/wp-content/uploads/2016/12/7-9.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://static1.bigstockphoto.com/6/2/2/large2/226601029.jpg\"}");
        //imageWithFacesList.add("{\"url\":\"data:image/jpeg;base64,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\"}");

        imageWithFacesList.add("{\"url\":\"https://en.pimg.jp/030/925/420/1/30925420.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://image1.masterfile.com/getImage/ODc3LTA2ODM1NzQyZW4uMDAwMDAwMDA=ADq1WG/877-06835742en_Masterfile.jpg\"}");
        imageWithFacesList.add("{\"url\":\"https://image.shutterstock.com/image-photo/redhead-irish-man-beard-holding-600w-1633898608.jpg\"}");
        //imageWithFacesList.add("{\"url\":\"https://image.shutterstock.com/image-photo/male-hypermarket-supermarket-employee-touching-600w-1360002551.jpg\"}");
        //imageWithFacesList.add("{\"url\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQKobXgbOXtf6e8Nk-C0U-0d0Pi4FlEKxkt3cgszRrmnVQHM5GF&usqp=CAU\"}");
        //imageWithFacesList.add("{\"url\":\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTllBm2t9tByPK_ueAX6mSqTP-5jCjAVu1mISbhMFArHJ4fuGRw&s\"}");
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
