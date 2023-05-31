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
 * Clase que implementa una petición POST para rechazar la solicitud de artista.
 */
public class MyTaskRejectArtist extends AsyncTask<String, Void, String> {
    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y rechazar la solicitud de artista.
     * @param params idUsuario, contrasenya, idNotificacion
     *
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor.
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String idNotificacion = params[2];
        String result = "";

        try {
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/RejectArtista/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\" , \"idNotificacion\": \"" + idNotificacion + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            int response = conn.getResponseCode();

            return String.valueOf(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }
}