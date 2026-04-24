package com.bajaj.quiz;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class QuizLeaderboardApp {

    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    private static final String REG_NO = "2024CS101";
    private static final int POLLS = 10;
    private static final int DELAY_MS = 5000; // 5 seconds

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException {
        Set<String> seenKeys = new HashSet<>();
        Map<String, Integer> participantScores = new HashMap<>();

        // Poll the API 10 times
        for (int poll = 0; poll < POLLS; poll++) {
            String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + poll;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode jsonNode = objectMapper.readTree(response.body());
                JsonNode events = jsonNode.get("events");
                if (events != null && events.isArray()) {
                    for (JsonNode event : events) {
                        String roundId = event.get("roundId").asText();
                        String participant = event.get("participant").asText();
                        int score = event.get("score").asInt();
                        String key = roundId + "_" + participant;

                        if (!seenKeys.contains(key)) {
                            seenKeys.add(key);
                            participantScores.put(participant, participantScores.getOrDefault(participant, 0) + score);
                        }
                    }
                }
            } else {
                System.err.println("Failed to poll for poll=" + poll + ", status: " + response.statusCode());
            }

            // Delay 5 seconds between polls
            if (poll < POLLS - 1) {
                Thread.sleep(DELAY_MS);
            }
        }

        // Generate leaderboard
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>(participantScores.entrySet());
        leaderboard.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); // descending

        List<Map<String, Object>> leaderboardJson = new ArrayList<>();
        int totalScore = 0;
        for (Map.Entry<String, Integer> entry : leaderboard) {
            Map<String, Object> item = new HashMap<>();
            item.put("participant", entry.getKey());
            item.put("totalScore", entry.getValue());
            leaderboardJson.add(item);
            totalScore += entry.getValue();
        }

        // Submit leaderboard
        Map<String, Object> submitData = new HashMap<>();
        submitData.put("regNo", REG_NO);
        submitData.put("leaderboard", leaderboardJson);

        String submitJson = objectMapper.writeValueAsString(submitData);
        HttpRequest submitRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/quiz/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(submitJson))
                .build();

        HttpResponse<String> submitResponse = httpClient.send(submitRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Submit Response: " + submitResponse.body());
    }
}