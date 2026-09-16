package com.surfsync.whatsapp.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;

@Service
public class OpenWaService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${openwa.base-url}")
    private String openWaBaseUrl;

    @Value("${openwa.api-key}")
    private String openWaApiKey;

    @Value("${openwa.session-id}")
    private String sessionId;

    public OpenWaService() {

        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).version(java.net.http.HttpClient.Version.HTTP_1_1).build();

        this.restClient = RestClient.builder().requestFactory(new org.springframework.http.client.JdkClientHttpRequestFactory(httpClient)).build();

        this.objectMapper = new ObjectMapper();
    }

    // ---------------------------------------------------------
    // SESSION STATUS
    // ---------------------------------------------------------

    public String getSessionStatus() {

        return restClient.get().uri(openWaBaseUrl + "/api/sessions/" + sessionId).header("X-API-Key", openWaApiKey).header("Connection", "close").retrieve().body(String.class);
    }

    // ---------------------------------------------------------
    // CREATE SESSION
    // ---------------------------------------------------------

    public String createSession(String name) {

        String url = openWaBaseUrl + "/api/sessions";

        try {
            String requestBody = objectMapper.writeValueAsString(Map.of("name", name));

            return restClient.post().uri(url).header("X-API-Key", openWaApiKey).header("Content-Type", "application/json").header("Connection", "close").body(requestBody).retrieve().body(String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create OpenWA session: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // SEND TEXT BY PHONE
    // ---------------------------------------------------------

    public String sendTextMessage(String phone, String message) {

        String chatId = phone + "@c.us";

        return sendTextMessageByChatId(chatId, message);
    }

    // ---------------------------------------------------------
    // SEND TEXT BY CHAT ID
    // ---------------------------------------------------------

    public String sendTextMessageByChatId(String chatId, String message) {

        String url = openWaBaseUrl + "/api/sessions/" + sessionId + "/messages/send-text";

        try {
            String requestBody = objectMapper.writeValueAsString(Map.of("chatId", chatId, "text", message));

            return restClient.post().uri(url).header("X-API-Key", openWaApiKey).header("Content-Type", "application/json").header("Connection", "close").body(requestBody).retrieve().body(String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send WhatsApp text: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // SEND MEDIA
    // mediaType = image / video / audio / document
    // ---------------------------------------------------------

    public String sendMedia(String chatId, String mediaType, String url, String caption, String filename, String mimetype) {

        String endpoint = switch (mediaType.toLowerCase()) {
            case "image" -> "send-image";
            case "video" -> "send-video";
            case "audio" -> "send-audio";
            case "document" -> "send-document";
            default -> throw new IllegalArgumentException("Unsupported media type: " + mediaType);
        };

        String apiUrl = openWaBaseUrl + "/api/sessions/" + sessionId + "/messages/" + endpoint;

        try {

            Map<String, Object> body;

            switch (mediaType.toLowerCase()) {

                case "image", "video" ->
                        body = Map.of("chatId", chatId, "url", url, "caption", caption == null ? "" : caption);

                case "audio" ->
                        body = Map.of("chatId", chatId, "url", url, "mimetype", mimetype == null ? "audio/ogg" : mimetype);

                case "document" ->
                        body = Map.of("chatId", chatId, "url", url, "filename", filename == null ? "document" : filename, "mimetype", mimetype == null ? "application/octet-stream" : mimetype);

                default -> throw new IllegalArgumentException("Unsupported media type: " + mediaType);
            }

            String requestBody = objectMapper.writeValueAsString(body);

            return restClient.post().uri(apiUrl).header("X-API-Key", openWaApiKey).header("Content-Type", "application/json").header("Connection", "close").body(requestBody).retrieve().body(String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send WhatsApp media: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // GET CHATS / MESSAGE HISTORY
    // ---------------------------------------------------------

    public String getChats() {

        String url = openWaBaseUrl + "/api/sessions/" + sessionId + "/messages?limit=100&offset=0";

        return restClient.get().uri(url).header("X-API-Key", openWaApiKey).header("Connection", "close").retrieve().body(String.class);
    }
}