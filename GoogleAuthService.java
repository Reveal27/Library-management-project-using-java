package com.library.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GoogleAuthService {
    private static final String CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");

    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private String authCode;
    private CountDownLatch latch;

    public Map<String, String> authenticate() {

        if (CLIENT_ID == null || CLIENT_SECRET == null) {
            throw new IllegalStateException(
                    "Google OAuth credentials not set. " +
                            "Please define GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET as environment variables."
            );
        }

        try {
            setupLocalServer();

            String state = "secure_random_state";
            String loginUrl = AUTH_URL +
                    "?client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8) +
                    "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                    "&response_type=code" +
                    "&scope=email%20profile" +
                    "&state=" + state;

            if (Desktop.isDesktopSupported() &&
                    Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(loginUrl));
            } else {
                System.out.println("Open this URL manually: " + loginUrl);
            }

            System.out.println("Waiting for Google login...");
            if (latch.await(60, TimeUnit.SECONDS) && authCode != null) {
                String accessToken = getAccessToken(authCode);
                if (accessToken != null) {
                    return getUserInfo(accessToken);
                }
            } else {
                System.err.println("Login timed out.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setupLocalServer() throws IOException {
        latch = new CountDownLatch(1);
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        server.createContext("/callback", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = queryToMap(query);

                String response = "Login successful! You can close this window.";
                if (params.containsKey("code")) {
                    authCode = params.get("code");
                } else if (params.containsKey("error")) {
                    response = "Login failed: " + params.get("error");
                }

                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                latch.countDown();
                server.stop(1);
            }
        });

        server.setExecutor(null);
        server.start();
    }

    private String getAccessToken(String code) {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded"
            );

            String params =
                    "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                            "&client_id=" + URLEncoder.encode(CLIENT_ID, StandardCharsets.UTF_8) +
                            "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, StandardCharsets.UTF_8) +
                            "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) +
                            "&grant_type=authorization_code";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
                return json.has("access_token")
                        ? json.get("access_token").getAsString()
                        : null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Map<String, String> getUserInfo(String accessToken) {
        try {
            URL url = new URL(USER_INFO_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Authorization", "Bearer " + accessToken
            );

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);

            Map<String, String> userInfo = new HashMap<>();
            if (json.has("email")) userInfo.put("email", json.get("email").getAsString());
            if (json.has("name")) userInfo.put("name", json.get("name").getAsString());
            if (json.has("id")) userInfo.put("id", json.get("id").getAsString());

            return userInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null) return result;

        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            result.put(entry[0], entry.length > 1 ? entry[1] : "");
        }
        return result;
    }
}
