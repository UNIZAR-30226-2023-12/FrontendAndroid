import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/*
 * Haciendo uso de las distintas funciones que ofrece el backend en su API llamar con urls
 * distintas al constructor para hacer uso de las mismas.
 */
public class POSTRequest {
    // URL como atributo
    private String url;

    // Constructor
    public POSTRequest(String url) {
        this.url = url;
    }

    // Metodo para realizar la petición POST
    public JSONObject sendPost(JSONObject data) {
        JSONObject jsonResponse = null;
        
        try {
            // Creo la instancia de la URL
            URL urlConnect = new URL(this.url);

            // Abrimos la conexión
            HttpURLConnection connection = (HttpURLConnection) urlConnect.openConnection();

            // Establecemos el método POST
            connection.setRequestMethod("POST");

            // Habilitamos el envío de datos
            connection.setDoOutput(true);

            // Enviamos los datos en formato JSON
            DATAOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data.toString());
            wr.flush();
            wr.close();

            // Recibimos la respuesta como un objeto JSON
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