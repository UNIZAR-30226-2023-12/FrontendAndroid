package eina.unizar.melodiaapp.Modules;

import static android.util.Log.d;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eina.unizar.melodiaapp.MySingleton;

/*
 * Haciendo uso de las distintas funciones que ofrece el backend en su API llamar con urls
 * distintas al constructor para hacer uso de las mismas.
 */

/**
 * Clase que implementa una petición GET.
 */
public class GETRequest extends AsyncTask<String, Void, JSONObject> {

    private Exception exception;
    MySingleton singleton = MySingleton.getInstance();
    
    private String url = "http://" + singleton.getMyGlobalVariable() + ":8081/";

    /**
     * Metodo que realiza la petición GET con los datos especificados
     * @param endpoint endpoint al que realizar la petición
     * @param data datos a enviar en formato JSON
     * @return respuesta de la API
     */
    public JSONObject sendGET(String endpoint, String data) {
        JSONObject jsonResponse = null;

        StringBuilder sb = new StringBuilder(url);
        sb.append(endpoint);
        sb.append(data);

        int status = 0;
        try {

            HttpURLConnection con = (HttpURLConnection) new URL(sb.toString()).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            jsonResponse = new JSONObject(content.toString());
            con.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    @Override
    protected JSONObject doInBackground(String... data) {
        return sendGET(data[0], data[1]);
    }
}