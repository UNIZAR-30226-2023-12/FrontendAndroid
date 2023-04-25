package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskProfile;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String response[] = new String[]{"Error"};
        try {
            response = doRequestAskUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView name = findViewById(R.id.displayProfileName);
        TextView email = findViewById(R.id.displayEmail);
        name.setText(response[2]);
        email.setText(response[3]);

        TextView becomeArist = findViewById(R.id.bBecomeArtist);
        TextView upload = findViewById(R.id.bUploadSong);

        if (response[1].equals("normalUser")){//Escondemos Subir cancion
            upload.setVisibility(View.GONE);

            // Configuración del botón de convertirse en artista
            TextView bArtist = findViewById(R.id.bBecomeArtist);
            bArtist.setOnClickListener(v -> {
                Intent intent = new Intent(this, BecomeArtist.class);
                startActivity(intent);
            });
        }
        else{//Escondemos Ser artista
            becomeArist.setVisibility(View.GONE);

            // Configuración del botón de subir canción
            TextView bUpload = findViewById(R.id.bUploadSong);
            bUpload.setOnClickListener(v -> {
                Intent intent = new Intent(this, upload_audio.class);
                startActivity(intent);
            });
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