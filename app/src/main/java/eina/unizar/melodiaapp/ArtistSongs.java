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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskNameSongs;
import eina.unizar.melodiaapp.Modules.MyTaskAskSongsArtist;
import eina.unizar.melodiaapp.Modules.MyTaskGetSongSeconds;

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

    protected String doRequestGetSongSeconds(String idAudio) throws ExecutionException, InterruptedException {
        MyTaskGetSongSeconds task = new MyTaskGetSongSeconds();
        String respuesta = task.execute(idAudio).get();
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
            // Obtengo los nombres de las canciones del artista y el tiempo que han sido escuchadas
            String[] nombresCanciones = new String[idsCanciones.length - 1];
            String[] segundosCanciones = new String[idsCanciones.length - 1];
            for (int i = 1; i < idsCanciones.length; i++) {
                try {
                    nombresCanciones[i - 1] = doRequestAskNameSongs(idsCanciones[i]);
                    segundosCanciones[i - 1] = doRequestGetSongSeconds(idsCanciones[i]);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Muestro los nombres de las canciones en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item_artist, R.id.listTextView, nombresCanciones);
            ListView listView = findViewById(R.id.listSongsArtist);
            listView.setAdapter(adapter);

            for (int j = 0; j < idsCanciones.length-1; j++) {
                View item = adapter.getView(j, null, listView);
                item.setTag(idsCanciones[j+1]);
                TextView textView = item.findViewById(R.id.listTextView);

                LayoutInflater inflater = getLayoutInflater();
                View header = inflater.inflate(R.layout.cancion_item_artist, null);
                listView.addHeaderView(header);

                TextView row = header.findViewById(R.id.listTextView);
                row.setText(nombresCanciones[j]);
                row.setTag(idsCanciones[j+1] + "," + segundosCanciones[j]);

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO ENVIAR AL REPRODUCTOR LA PLAYLIST
                        String idCancion = (String) v.getTag();
                        SharedPreferences preferences = getSharedPreferences("cancionActual", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("idCancionActual", idCancion);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Player.class);
                        startActivity(intent);
                    }
                });

                ImageView deleteBtn = header.findViewById(R.id.BorradoCancionArtist);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO ELIMINAR CANCION DE LA PLAYLIST
                        String idCancion = (String) v.getTag();
                        Intent intent = new Intent(getApplicationContext(), Player.class);
                        startActivity(intent);
                    }
                });

                //Botón de options para mostrar los segundos que ha sido escuchada una canción
                ImageView optionsBtn = header.findViewById(R.id.optionsSongsArtist);
                optionsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO MOSTRAR SEGUNDOS QUE HA SIDO ESCUCHADA LA CANCIÓN
                        String tag = (String) v.getTag();
                        String[] tagSplit = tag.split(",");
                        Toast.makeText(getApplicationContext(), "La canción ha sido escuchada " + tagSplit[1] + " segundos", Toast.LENGTH_SHORT).show();

                    }
                });

            }
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
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