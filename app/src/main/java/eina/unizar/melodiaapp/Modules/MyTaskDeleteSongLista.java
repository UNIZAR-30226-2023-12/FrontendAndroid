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
 * Clase que implementa una petición GET para eliminar una canción de una lista de reproducción.
 */
public class MyTaskDeleteSongLista extends AsyncTask<String, Void, String> {

    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y eliminar la canción de la lista de reproducción.
     * @param params idUsuario, contrasenya, idLista, idAudio
     *
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor.
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String idLista = params[2];
        String idAudio = params[3];
        String result = "";

        try {
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/RemoveSongLista/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\" , \"idLista\": \"" + idLista + "\" , \"idAudio\": \"" + idAudio + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            int response = conn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                return "200";
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