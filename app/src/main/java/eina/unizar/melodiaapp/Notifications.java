package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAcceptArtist;
import eina.unizar.melodiaapp.Modules.MyTaskAskNotifications;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameNotifications;

public class Notifications extends AppCompatActivity {
    /**
     * Realiza la petición para obtener los ids de las notificaciones del usuario
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * Realiza la petición para obtener el nombre de una notificación dado su id
     * @param idNot
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    protected String doRequestAcceptArtist(String idNot) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Hago la petición para aceptar la solicitud de artista
        MyTaskAcceptArtist task = new MyTaskAcceptArtist();
        String respuesta = task.execute(idUsuario, contrasenya, idNot).get();

        if (respuesta.equals("200")) {
            return "200";
        }
        else {
            return "Error";
        }
    }

    /**
     * Se encarga de generar la interfaz de la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Bundle extras = getIntent().getExtras();

        if (extras == null || extras.get("key").equals("admin")) {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombreNotificaciones);
                ListView listView = findViewById(R.id.notificationsListView);
                listView.setAdapter(adapter);

                for (int j = 0; j < response.length-1; j++) {
                    View item = adapter.getView(j, null, listView);
                    item.setTag(response[j+1]);
                    TextView textView = item.findViewById(R.id.listTextView);

                    LayoutInflater inflater = getLayoutInflater();
                    View header = inflater.inflate(R.layout.cancion_item, null);
                    listView.addHeaderView(header);

                    TextView row = header.findViewById(R.id.listTextView);
                    row.setText(nombreNotificaciones[j]);
                    row.setTag(response[j+1]);

                    ImageView deleteBtn = header.findViewById(R.id.imageView5);
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO ELIMINAR CANCION DE LA PLAYLIST
                            String idCancion = (String) v.getTag();
                            Intent intent = new Intent(getApplicationContext(), Player.class);
                            startActivity(intent);
                        }
                    });

                    if (extras.get("key").equals("admin")) {
                        //Botón de options para mostrar los segundos que ha sido escuchada una canción
                        ImageView optionsBtn = header.findViewById(R.id.imageView4);
                        optionsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO MOSTRAR SEGUNDOS QUE HA SIDO ESCUCHADA LA CANCIÓN
                                String tag = (String) v.getTag();
                                String respuesta = "Error";
                                try {
                                    respuesta = doRequestAcceptArtist(tag);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (respuesta.equals("200")) {
                                    Toast.makeText(getApplicationContext(), "Solicitud de artista aceptada", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error al aceptar la solicitud de artista", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
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
        else{//Comprobamos si hemos llegado desde AdminConfig
            String mode = extras.getString("key");

            if(mode.equals("admin")){//Si lo hemos hecho
                //TODO mostrar los artistas a aprobar
                //Debemos conocer como guarda el backend los valores de las peticiones de artista para extraerlos

                //Pasaremos información en el intent de la siguiente manera:
                Intent reproducirDemo = new Intent(getApplicationContext(), Player.class);

                //Valores
                String body = "Descripción y porque quiere ser un artista";
                String nombreUsuario = "Nombre artista";
                String idSong = "idCancion"; //No se hasta que punto se necesita aqui

                //Introducción en el intent extras
                reproducirDemo.putExtra("mode", "demo");
                reproducirDemo.putExtra("song",idSong);
                reproducirDemo.putExtra("body",body);
                reproducirDemo.putExtra("name",nombreUsuario);
                startActivity(reproducirDemo);
            }
            else{
                System.out.println("Bad intent, mode value incorrect\n");
                finish();
            }
        }
    }
}