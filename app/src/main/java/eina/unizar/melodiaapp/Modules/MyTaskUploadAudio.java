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

public class MyTaskUploadAudio extends AsyncTask<String, Void, String> {
    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y que
     * sube un audio y devuelve el código de respuesta http
     *
     * @param params
     * @return
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String nombre = params[2];
        String audio = params[3];
        String esCancion = params[4];
        String duracion = params[5];
        String genero = params[6];
        String calidad = params[7];
        String nombreArtista = params[8];
        String result = "";

        try {
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/SetSong/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\" , \"nombre\": \"" + nombre + "\" , \"audio\": \"" + audio + "\" , \"esPodcast\": \"" + esCancion + "\" , \"longitud\": \"" + duracion + "\" , \"genero\": \"" + genero + "\" , \"calidad\": \"" + calidad + "\" , \"artista\": \"" + nombreArtista + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            return String.valueOf(conn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "200";
    }
}