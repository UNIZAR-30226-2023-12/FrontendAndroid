package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import eina.unizar.melodiaapp.MySingleton;

/**
 * Clase que codifica la tarea asíncrona de suscribirse a un artista
 */
public class MyTaskSubscribe extends AsyncTask<String, Void, String> {
    /**
     * Realiza la tarea de llamar al backend para realizar la suscripción a un artista
     * @param params idUsuario, contrasenya, idUsrArtista
     *
     * @return Devuelve un código de respuesta HTTP proporcionado por el servidor
     */
    @Override
    public String doInBackground(String... params) {
        String idUsuario = params[0];
        String contrasenya = params[1];
        String idUsrArtista = params[2];
        String result = "";

        try { //SubscribeToArtist(String idUsr, String contrasenya, String idUsrArtista) : int
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/SubscribeToArtist/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"contrasenya\": \"" + contrasenya + "\", \"idUsrArtista\": \"" + idUsrArtista + "\"}";

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
