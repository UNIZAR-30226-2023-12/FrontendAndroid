package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase que codifica la tarea asíncrona de registro
 */
public class MyTaskRegister extends AsyncTask<String, Void, String> {
    /**
     * Realiza la tarea de llamar al backend para realizar el registro de un usuario
     * @param params
     * @return
     */
    @Override
    public String doInBackground(String... params) {
        String username = params[0];
        String email = params[1];
        String password = params[2];
        String idUltimoAudio = "-1";

        String result = "";

        try {
            URL url = new URL("http://10.0.2.2:8081/SetUser/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + username + "\", \"email\": \"" + email + "\", \"contrasenya\": \"" + password + "\", \"tipoUsuario\": \"normalUser\", \"alias\": \"" + username + "\"}";

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(jsonInputString);
            }

            int response = conn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                return "200";
            }
            else{
                return "Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
