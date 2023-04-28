package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskNotifications;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameNotifications;

public class Notifications extends AppCompatActivity {
    protected String[] doRequest() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Hago la petición para obtener las notificaciones del usuario
        MyTaskAskNotifications task = new MyTaskAskNotifications();
        String respuesta = task.execute(idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String doRequestAskNameNotifications(String idNot) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Hago la petición para obtener el nombre de la notificación
        MyTaskAskNameNotifications task = new MyTaskAskNameNotifications();
        String respuesta = task.execute(idUsuario, contrasenya, idNot).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Hago la petición para obtener las notificaciones del usuario
        String response[] = new String[]{"Error"};
        try {
            response = doRequest();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response[0].equals("200")) {
            // Para cada notificación obtengo su nombre
            String nombreNotificaciones[] = new String[response.length - 1];
            for (int i = 1; i < response.length; i++) {
                try {
                    nombreNotificaciones[i - 1] = doRequestAskNameNotifications(response[i]);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Muestro los nombres de las canciones en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.notificationsListView, nombreNotificaciones);
            ListView listView = findViewById(R.id.notificationsListView);
            listView.setAdapter(adapter);
        }

        // Añado onCreate para botón home
        ImageView homeBtn = findViewById(R.id.menuIconNotifications);
        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Notifications.this, Menu.class);
            startActivity(intent);
        });

        // Añado onCreate para botón perfil
        ImageView profileBtn = findViewById(R.id.profileIconNotifications);
        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Notifications.this, Profile.class);
            startActivity(intent);
        });
    }
}