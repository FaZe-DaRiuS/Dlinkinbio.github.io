import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class ProxyController {

    @GetMapping("/proxy")
    public String proxy(@RequestParam String url) {
        StringBuilder result = new StringBuilder();

        try {
            // Conectar a la URL proporcionada por el usuario
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");

            // Leer el contenido de la página objetivo
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Reemplazar enlaces para pasarlos a través del proxy
                line = line.replaceAll("href=\"/", "href=\"/proxy?url=" + targetUrl.getProtocol() + "://" + targetUrl.getHost() + "/");
                line = line.replaceAll("src=\"/", "src=\"/proxy?url=" + targetUrl.getProtocol() + "://" + targetUrl.getHost() + "/");
                result.append(line);
            }
            reader.close();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return result.toString();
    }
}
