package gay.nns.client.util.account;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BrowserUtil {

    public static String postExternal(final String url, final String post, final boolean json) {
        try {
            final HttpsURLConnection connection = getHttpsURLConnection(url, post, json);

            final int responseCode = connection.getResponseCode();

            final InputStream stream = responseCode / 100 == 2 || responseCode / 100 == 3 ? connection.getInputStream() : connection.getErrorStream();

            if (stream == null) {
                System.err.println(responseCode + ": " + url);
                return null;
            }

            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String lineBuffer;
            final StringBuilder response = new StringBuilder();
            while ((lineBuffer = reader.readLine()) != null) {
                response.append(lineBuffer);
            }

            reader.close();

            return response.toString();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HttpsURLConnection getHttpsURLConnection(String url, String post, boolean json) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        final byte[] out = post.getBytes(StandardCharsets.UTF_8);
        final int length = out.length;
        connection.setFixedLengthStreamingMode(length);
        connection.addRequestProperty("Content-Type", json ? "application/json" : "application/x-www-form-urlencoded; charset=UTF-8");
        connection.addRequestProperty("Accept", "application/json");
        connection.connect();
        try (final OutputStream os = connection.getOutputStream()) {
            os.write(out);
        }
        return connection;
    }

    public static String getBearerResponse(final String url, final String bearer) {
        try {
            final HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Authorization", "Bearer " + bearer);

            if (connection.getResponseCode() == 200) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String lineBuffer;
                final StringBuilder response = new StringBuilder();
                while ((lineBuffer = reader.readLine()) != null) {
                    response.append(lineBuffer);
                }

                return response.toString();
            } else {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String lineBuffer;
                final StringBuilder response = new StringBuilder();
                while ((lineBuffer = reader.readLine()) != null) {
                    response.append(lineBuffer);
                }

                return response.toString();
            }
        } catch (final Exception e) {
            return null;
        }
    }
}