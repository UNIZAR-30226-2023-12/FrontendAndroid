package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskProfile;
import eina.unizar.melodiaapp.Modules.MyTaskSetSongLista;
import eina.unizar.melodiaapp.Modules.MyTaskSubscribe;

public class Profile extends AppCompatActivity {
    protected String[] doRequestAskUser() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskProfile task = new MyTaskAskProfile();
        String respuesta = task.execute(idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String[] doRequestAskArtistData(String idUsuario) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences

        String contrasenya = "none";

        MyTaskAskProfile task = new MyTaskAskProfile();
        String respuesta = task.execute(idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String doRequestSubscribe(String idArtista) throws ExecutionException, InterruptedException {
        // Obtengo usuario, contraseña e id de la playlist a modificar
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");


        // Hago la petición
        MyTaskSubscribe task = new MyTaskSubscribe();

        return task.execute(idUsuario, contrasenya, idArtista).get();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String response[] = new String[]{"Error"};
        Boolean visitor = false;
        String idA = "";

        Bundle extras = getIntent().getExtras();
        if ( extras != null) {//Buscamos los valores extra

            String mode = extras.getString("mode");
            idA = extras.getString("idArtista");

            if(mode.equals("visitor")){

                try {
                    response = doRequestAskArtistData(idA);
                    visitor = true;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            else{
                System.out.println("Error, unknown mode");
            }

        }
        else {
            try {
                response = doRequestAskUser();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        TextView name = findViewById(R.id.displayProfileName);
        TextView email = findViewById(R.id.displayEmail);
        name.setText(response[2]);
        // Añado el nombre a shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombre", response[2]);
        editor.apply();
        email.setText(response[3]);

        TextView becomeArist = findViewById(R.id.bBecomeArtist);
        TextView upload = findViewById(R.id.bUploadSong);
        ImageView configAdmin = findViewById(R.id.bAdmin);
        TextView subscribe = findViewById(R.id.bSubscribe);
        //Escondemos el boton de admin y suscribir
        configAdmin.setVisibility(View.GONE);
        subscribe.setVisibility(View.GONE);



        if (visitor){//Mostramos el boton de suscripción
            upload.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            subscribe.setVisibility(View.VISIBLE);
            subscribe.setOnClickListener(v -> {
            //TODO SUBSCRIBE
                try {
                    String suscrito = doRequestSubscribe(extras.getString("idArtista"));
                    //Toast con el resultado
                    Toast.makeText(getApplicationContext(), "Suscripcion a artista: " + suscrito, Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            if (response[1].equals("normalUser")) {//Escondemos Subir cancion
                upload.setVisibility(View.GONE);

                // Configuración del botón de convertirse en artista
                TextView bArtist = findViewById(R.id.bBecomeArtist);
                bArtist.setOnClickListener(v -> {
                    Intent intent = new Intent(this, BecomeArtist.class);
                    startActivity(intent);
                });
            } else {//Escondemos Ser artista
                becomeArist.setVisibility(View.GONE);

                if (response[1].equals("admin")) {
                    configAdmin.setVisibility(View.VISIBLE);

                    configAdmin.setOnClickListener(v -> {
                        Intent intent = new Intent(this, AdminConfig.class);
                        startActivity(intent);
                    });
                } else {


                    // Configuración del botón de subir canción
                    TextView bUpload = findViewById(R.id.bUploadSong);
                    bUpload.setOnClickListener(v -> {
                        Intent intent = new Intent(this, upload_audio.class);
                        startActivity(intent);
                    });
                }
            }
        }

        // Configuración del botón de home
        ImageView homeBtn = findViewById(R.id.menuIconAProfile);
        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        });

        // Configuración del botón de cerrar sesión
        TextView logoutBtn = findViewById(R.id.bCloseSession);
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Configuración del botón de convertirse en artista
        TextView bArtist = findViewById(R.id.bBecomeArtist);
        bArtist.setOnClickListener(v -> {
            Intent intent = new Intent(this, BecomeArtist.class);
            startActivity(intent);
        });


    }
}