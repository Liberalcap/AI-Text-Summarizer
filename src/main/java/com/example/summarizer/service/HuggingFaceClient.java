package com.example.summarizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class HuggingFaceClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${huggingface.api.key}")
    private String apiKey;

    public HuggingFaceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api-inference.huggingface.co/models/google/pegasus-xsum")
                .build();
    }

    public String summarize(String inputText) {
        Map<String, Object> requestBody = Map.of("inputs", inputText);

        try {
            String response = webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse JSON response
            JsonNode root = objectMapper.readTree(response);

            if (root.isArray() && root.size() > 0 && root.get(0).has("summary_text")) {
                return root.get(0).get("summary_text").asText();
            } else if (root.has("error")) {
                return "HuggingFace error: " + root.get("error").asText();
            } else {
                return "Unexpected response: " + response;
            }

        } catch (Exception e) {
            return "Error while summarizing: " + e.getMessage();
        }
    }
}
