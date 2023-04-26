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
import eina.unizar.melodiaapp.Modules.MyTaskAskSongs;

public class playlist_favoritos extends AppCompatActivity {

    protected String[] doRequestAskSongs() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");
        String idListaFavUser = preferences.getString("idListaFavoritos", "");

        MyTaskAskSongs task = new MyTaskAskSongs();
        String respuesta = task.execute(idUsuario, contrasenya, idListaFavUser).get();
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
        setContentView(R.layout.activity_playlist_favoritos);

        // Hago la request para obtener las canciones de una playlist
        String idsSongs[] = new String[]{"Error"};
        try {
            idsSongs = doRequestAskSongs();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (idsSongs[0].equals("200")) {
            String nombresSongs[] = new String[idsSongs.length - 1];
            for (int i = 1; i < idsSongs.length; i++) {
                try {
                    nombresSongs[i - 1] = doRequestAskNameSongs(idsSongs[i]);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Muestro los nombres de las canciones en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresSongs);
            ListView listView = findViewById(R.id.listFav);
            listView.setAdapter(adapter);
        }


        // OnClick para el botón home
        ImageView homeBTN = findViewById(R.id.menuIconAlistarepFav);
        homeBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
        });

        // OnClick para el botón de notificaciones
        ImageView notifBTN = findViewById(R.id.bellIconAlistarepFav);
        notifBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Notifications.class);
            startActivity(intent);
        });

        // OnClick para el botón de perfil
        ImageView profileBTN = findViewById(R.id.profileIconAlistarepFav);
        profileBTN.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        });
    }
}