package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import eina.unizar.melodiaapp.MySingleton;

/**
 * Clase que implementa una petición POST para obtener el tiempo de reproducción de una canción.
 */
public class MyTaskGetSongSeconds extends AsyncTask<String, Void, String> {

    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y
     * obtener el tiempo de reproducción de una canción.
     * @param params idAudio
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor y el tiempo de reproducción de la canción.
     */
    @Override
    public String doInBackground(String... params) {
        String idAudio = params[0];
        String dia = "0";
        String result = "";

        try { //SetSongLista(String idUsr, String contrasenya, String idLista, String idAudio): int
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/GetSongSeconds/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idAudio\": \"" + idAudio + "\" , \"dia\": \"" + dia + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            int response = conn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                // Obtengo el rating
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    sb.append(responseLine.trim());
                }
                br.close();
                result = sb.toString();

                // Parseamos el JSON
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
                String mins = jsonObject.get("second").getAsString();
                return "200," + mins;
            }
            else {
                return "Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }
}