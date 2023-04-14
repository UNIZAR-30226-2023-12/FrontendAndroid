package eina.unizar.melodiaapp.Modules;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Clase que codifica la tarea asíncrona para la validación de usuarios en el login
 */
public class MyTaskLogin extends AsyncTask<String, Void, String> {
    /**
     * Función que se ejecuta en segundo plano. Realiza la petición al servidor para validar el usuario
     * @param params
     * @return
     */
    @Override
    public String doInBackground(String... params) {
        String id = params[0];
        id = "idUsuario:" + id;
        String contra = params[1];
        String result = "";

        try {
            URL url = new URL("http://10.0.2.2:8081/ValidateUser/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsuario\": \"" + id + "\", \"contrasenya\": \"" + contra + "\"}";

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
                String idUsuario = jsonObject.get("idUsuario").getAsString();

                return "200," + idUsuario;
            }
            else{
                return "Error";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "-1";
    }
}
