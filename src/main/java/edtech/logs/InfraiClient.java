package edtech.logs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class InfraiClient {
    private final HttpClient http;
    private final String baseUrl;
    private final String apiKey;

    public InfraiClient(String baseUrl, String apiKey) {
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public String request(String method, String path, String body) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json");
        HttpRequest request = builder.method(method, HttpRequest.BodyPublishers.ofString(body == null ? "" : body)).build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        String envelope = response.body();
        if (!envelope.contains("\"ok\":true")) {
            throw new IOException("Infrai request rejected: " + envelope);
        }
        return envelope;
    }

    public String ingest(String entries) throws IOException, InterruptedException {
        return request("POST", "/v1/logs/ingest", "{\"entries\":" + entries + "}");
    }

    public String search(String query) throws IOException, InterruptedException {
        return request("GET", "/v1/logs/search?q=" + query, null);
    }

    public String listKeys() throws IOException, InterruptedException {
        return request("GET", "/v1/account/keys/list", null);
    }
}
