package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyTaskCreatePlaylist extends AsyncTask<String, Void, String> {
    @Override
    public String doInBackground(String... params) {
        String nombre = params[0];
        String privacidad = params[1];
        String idUsuario = params[2];
        String contrasenya = params[3];
        String idLista = params[4];
        String tipoLista = params[5];
        String result = "";

        try {
            URL url = new URL("http://10.0.2.2:8081/SetLista/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"nombreLista\": \"" + nombre + "\", \"privada\": \"" + privacidad + "\", \"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\", \"idLista\": \"" + idLista + "\", \"tipoLista\": \"" + tipoLista + "\"}";

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
                String idDevuelto = jsonObject.get("idLista").getAsString();

                return "200, " + idDevuelto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "200";
    }
}
