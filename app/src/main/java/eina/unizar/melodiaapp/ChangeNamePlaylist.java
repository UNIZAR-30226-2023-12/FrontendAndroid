package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskChangeNamePlaylist;

/**
 * Clase que gestiona la actividad de cambiar el nombre de una playlist
 */
public class ChangeNamePlaylist extends AppCompatActivity {
    /**
     * Método que realiza la petición de cambiar el nombre de una playlist
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestChangeNamePlaylist() throws ExecutionException, InterruptedException {
        // Obtengo usuario, contraseña e id de la playlist a modificar
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");
        String idLista = preferences.getString("idListaChangeName", "");

        // Obtengo el nombre de la playlist
        TextView nombrePlaylist = findViewById(R.id.NewPlaylistName);
        String nombre = nombrePlaylist.getText().toString();

        // Hago la petición
        MyTaskChangeNamePlaylist task = new MyTaskChangeNamePlaylist();

        return task.execute(idUsuario, contrasenya, idLista, nombre).get();
    }

    /**
     * Método que se ejecuta al crear la actividad
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name_playlist);

        // Listener para el perfil
        ImageView perfil = findViewById(R.id.profileIconChangeNamePlaylist);
        perfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        // Listener para notificaciones
        ImageView notificaciones = findViewById(R.id.bellIconChangeNamePlaylist);
        notificaciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        // Listener para el botón de home
        ImageView home = findViewById(R.id.imageHomeChangeNamePlaylist);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        // Listener para el botón de cambiar nombre
        TextView changeName = findViewById(R.id.bChangeNamePlaylist);
        changeName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String response = "Error";
                try {
                    response = doRequestChangeNamePlaylist();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (response.equals("200")) {
                    Intent intent = new Intent(getApplicationContext(), Playlist.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error al cambiar el nombre de la playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}