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
 * Clase que implementa una petición POST para realizar una búsqueda por palabras.
 */
public class MyTaskByWordSearch extends AsyncTask<String, Void, String> {
    /**
     * Método que se ejecuta en segundo plano para realizar la petición al servidor y
     * obtener los ids de los resultados de la búsqueda.
     * @param params params[0] es la query y params[1] es n
     *
     * @return Devuelve un string con el código de respuesta y los ids de los resultados
     */
    public String doInBackground(String... params) {
        String query = params[0];
        String n = params[1];
        String result = "";

        try {
            MySingleton singleton = MySingleton.getInstance();
            URL url = new URL("http://" + singleton.getMyGlobalVariable() + ":8081/ByWordSearch/");        //ByWordSearch(Set<String> query, int n) : Set<String>

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
                JsonArray jsonArray = jsonObject.getAsJsonArray("resultados");
                // Obtengo los ids de todas los resultados del array y los concateno en un string
                String idDevuelto = "";
                for (int i = 0; i < jsonArray.size(); i++) {
                    idDevuelto += jsonArray.get(i).getAsString() + ",";
                }

                return "200," + idDevuelto;
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
