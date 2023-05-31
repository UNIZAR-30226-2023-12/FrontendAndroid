package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import eina.unizar.melodiaapp.MySingleton;

/**
 * Clase que codifica la tarea asíncrona de añadir el segundo en el que se ha dejado de escuchar una canción
 */
public class MyTaskSetLastSecondHeared extends AsyncTask<String, Void, String> {

    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y añadir el segundo en el que se ha dejado de escuchar una canción
     * @param params idUsuario, contrasenya, idAudio, second
     *
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor.
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String idAudio = params[2];
        String second = params[3];
        String result = "";

        try { //SetLastSecondHeared(String idUsr, String contrasenya, String idAudio, int second) : int
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/SetLastSecondHeared/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\", \"idAudio\": \"" + idAudio + "\", \"second\": \"" + second + "\"}";

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
        return "Error";
    }
}
