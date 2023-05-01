package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskNameSongs;
import eina.unizar.melodiaapp.Modules.MyTaskAskSongsArtist;

public class ArtistSongs extends AppCompatActivity {
    protected String[] doRequestAskSongs() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskSongsArtist task = new MyTaskAskSongsArtist();
        String respuesta = task.execute(idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String doRequestAskNameSongs(String idSong) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskNameSongs task = new MyTaskAskNameSongs();
        String respuesta = task.execute(idUsuario, contrasenya, idSong).get();
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
        setContentView(R.layout.activity_artist_songs);

        // Obtengo los ids de las canciones del artista
        String[] idsCanciones = new String[]{"Error"};
        try {
            idsCanciones = doRequestAskSongs();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (idsCanciones[0].equals("200")) {
            // Obtengo los nombres de las canciones del artista
            String[] nombresCanciones = new String[idsCanciones.length - 1];
            for (int i = 1; i < idsCanciones.length; i++) {
                try {
                    nombresCanciones[i - 1] = doRequestAskNameSongs(idsCanciones[i]);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Muestro los nombres de las canciones en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresCanciones);
            ListView listView = findViewById(R.id.listSongsArtist);
            listView.setAdapter(adapter);
        }

        // OnClick botón home
        ImageView home = findViewById(R.id.menuIconArtistSongs);
        home.setOnClickListener(v -> {
            Intent intent = new Intent(ArtistSongs.this, Menu.class);
        });

        // OnClick botón perfil
        ImageView profile = findViewById(R.id.profileIconArtistSongs);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(ArtistSongs.this, Profile.class);
        });

        // OnClick botón de notificaciones
        ImageView notifications = findViewById(R.id.bellIconArtistSongs);
        notifications.setOnClickListener(v -> {
            Intent intent = new Intent(ArtistSongs.this, Notifications.class);
        });
    }
}