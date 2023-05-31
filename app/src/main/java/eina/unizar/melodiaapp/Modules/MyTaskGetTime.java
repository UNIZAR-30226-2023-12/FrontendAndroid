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
 * Clase que implementa una petición POST para obtener los segundos reproducidos totales de un día en concreto.
 */
public class MyTaskGetTime extends AsyncTask<String, Void, String> {

    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y obtener los segundos reproducidos totales de un día en concreto.
     * @param params idUsuario, contrasenya, dia
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor y los segundos reproducidos
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String dia = params[2];
        String result = "";

        try { //GetTotRepTime(String idUsr, String contrasenya, String dia) : int
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/GetTotRepTime/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\", \"dia\": \"" + dia + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            int response = conn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                // Obtengo los segundos
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
                String link = jsonObject.get("second").getAsString();
                return "200," + link;
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