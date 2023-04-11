package eina.unizar.melodiaapp.Modules;


import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyTask extends AsyncTask<String, Void, String> {
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
                return "200";
            }
            else{
                return "Error";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "-1";
    }

    @Override
    protected void onPostExecute(String result) {
        // Aquí puedes procesar la respuesta
    }
}
