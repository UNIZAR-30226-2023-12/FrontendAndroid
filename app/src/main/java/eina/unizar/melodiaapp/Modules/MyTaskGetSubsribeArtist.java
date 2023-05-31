package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import eina.unizar.melodiaapp.MySingleton;

/**
 * Clase que implementa una petición GET para obtener los artistas a los que está suscrito un usuario.
 */
public class MyTaskGetSubsribeArtist extends AsyncTask<String, Void, String> {
    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y obtener los artistas a los que está suscrito un usuario.
     * @param params idUsuario, contrasenya
     *
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor y los ids de los artistas a los que está suscrito el usuario.
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String result = "";

        try {
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/GetSubscriptionsUsr/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            int response = conn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
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
                JsonArray jsonArray = jsonObject.getAsJsonArray("artista");
                // Obtengo los ids de todas las listas del array y los concateno en un string
                String idDevuelto = "";
                for (int i = 0; i < jsonArray.size(); i++) {
                    idDevuelto += jsonArray.get(i).getAsString() + ",";
                }

                return "200," + idDevuelto;
            }
            else {
                return "Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "200";
    }
}