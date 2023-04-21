package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Integer artista = 1; //TODO colocar función para saber si es artista

        TextView becomeArist = findViewById(R.id.bBecomeArtist);
        TextView upload = findViewById(R.id.bUploadSong);

        if (artista == 0){//Escondemos Subir cancion
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