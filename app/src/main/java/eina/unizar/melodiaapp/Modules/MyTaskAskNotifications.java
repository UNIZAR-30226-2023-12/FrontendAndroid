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

public class MyTaskAskNotifications extends AsyncTask<String, Void, String> {
    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y que
     * devuelve los ids de las notificaciones de un usuario dado su usr y contraseña
     * @param params
     * @return
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String result = "";

        try {
            URL url = new URL("http://10.0.2.2:8081/GetNotificationsUsr/");
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
                JsonArray jsonArray = jsonObject.getAsJsonArray("idNotificacion");
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