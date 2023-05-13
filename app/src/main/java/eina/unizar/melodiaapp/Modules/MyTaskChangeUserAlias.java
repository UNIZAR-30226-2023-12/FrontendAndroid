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

public class MyTaskChangeUserAlias extends AsyncTask<String, Void, String> {
    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y cambiar el alias
     * @param params
     * @return
     */
    @Override
    public String doInBackground(String... params) {
        String nombreCarpeta = params[0];
        String idUsuario = params[1];
        String contrasenya = params[2];
        String alias = params[3];
        String result = "";

        try {//SetAliasUsr
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/SetAliasUsr/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"idUsr\": \"" + idUsuario + "\", \"alias\": \"" + alias + "\", \"contrasenya\": \"" + contrasenya + "\", \"idUsr2\": \"" + idUsuario + "\"}";

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