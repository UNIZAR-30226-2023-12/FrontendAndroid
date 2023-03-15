package eina.unizar.melodiaapp.Modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/*
 * Haciendo uso de las distintas funciones que ofrece el backend en su API llamar con urls
 * distintas al constructor para hacer uso de las mismas.
 */

public class GETRequest {
    // URL como atributo
    private String url;

    // Constructor
    public GETRequest(String url) {
        this.url = url;
    }

    // Metodo para realizar la petición GET
    public JSONObject sendGET() {
        JSONObject jsonResponse = null;
        try {
            URL urlConnect = new URL(this.url);

            // Abrimos la conexión
            HttpURLConnection connection = (HttpURLConnection) urlConnect.openConnection();

            // Establecemos el método GET
            connection.setRequestMethod("GET");

            // Envíamos la petición
            connection.connect();

            // Recibimos la respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Cerramos la conexión
            connection.disconnect();

            // Convertimos la respuesta a un objeto JSON
            jsonResponse = new JSONObject(response.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}