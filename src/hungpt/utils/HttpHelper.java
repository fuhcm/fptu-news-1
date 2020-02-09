package hungpt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpHelper {

    public static String getContent(String href) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        String data = "";

        try {
            url = new URL(href);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
            connection.addRequestProperty("Accept-Charset", "UTF-8");

            is = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = br.readLine()) != null){
                data += line;
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return data;
    }
}
