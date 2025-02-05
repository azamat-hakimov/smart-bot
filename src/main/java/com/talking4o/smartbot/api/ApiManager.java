package com.talking4o.smartbot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;


public class ApiManager {

    private final String accessKey;
    private final String baseUrl;

    public ApiManager(String accessKey, String baseUrl) {
        this.accessKey = accessKey;
        this.baseUrl = baseUrl;
    }

    private Request makeRequest(String message){

        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://" + baseUrl + "/gpt4";

        String jsonMessage = "{"
                + "\"messages\": [{"
                + "\"role\": \"user\","
                + "\"content\": \"" + message + "\""
                + "}],"
                + "\"web_access\": false"
                + "}";

        RequestBody body = RequestBody.create(mediaType, jsonMessage);

        return new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("x-rapidapi-key", accessKey)
                .addHeader("x-rapidapi-host",baseUrl)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    public String getResponse(String message) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(makeRequest(message)).execute();
        if (response.isSuccessful() && response.body() != null){
            String jsonResponse = response.body().string();

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            return jsonNode.get("result").asText();
        }
        return "Couldn't received your request!";
    }
}
