package eina.unizar.melodiaapp.Modules;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyTaskCreatePlaylist extends AsyncTask<String, Void, String> {
    @Override
    public String doInBackground(String... params) {
        return "200";
    }
}
