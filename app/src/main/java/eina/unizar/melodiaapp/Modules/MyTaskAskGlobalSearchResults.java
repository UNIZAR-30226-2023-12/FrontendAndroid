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

public class MyTaskAskGlobalSearchResults extends AsyncTask<String, Void, String> {

    public String doInBackground(String... params) {
        String query = params[0];
        String n = params[1];
        String result = "";

        try {
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/GlobalSearch/");        //GlobalSearch(String query, int n) : Set<String>
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"query\": \"" + query + "\", \"n\": \"" + n + "\"}";

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
                JsonArray audios = jsonObject.getAsJsonArray("audios");
                JsonArray artistas = jsonObject.getAsJsonArray("artistas");
                JsonArray listas = jsonObject.getAsJsonArray("listas");


                // Obtengo los ids de todas los resultados del array y los concateno en un string
                String idsAudios = "";
                String idsArtistas = "";
                String idsListas = "";
                for (int i = 0; i < audios.size(); i++) {
                    idsAudios += audios.get(i).getAsString() + ",";
                }
                for (int i = 0; i < artistas.size(); i++) {
                    idsArtistas += artistas.get(i).getAsString() + ",";
                }
                for (int i = 0; i < listas.size(); i++) {
                    idsListas += listas.get(i).getAsString() + ",";
                }

                return "200;" + idsAudios + ";" + idsArtistas + ";" + idsListas;
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
