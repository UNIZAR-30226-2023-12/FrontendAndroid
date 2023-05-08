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

/**
 * Clase que codifica la actividad del menu principal de la aplicación
 */
public class Menu extends AppCompatActivity {
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

    /**
     * Función invocada al crear la pantalla. Inicializa todos los elementos de la interfaz de usuario
     *
     * @param savedInstanceState Si la actividad se ha reinicializado después de ser apagada,
     *                           este Bundle contiene los datos que había aportado recientemente en
     *                           onSaveInstanceState.  <b><i>Nota: En cualquier otro caso su
     *                           valor es null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        //TODO funcion para designar si es admin
        Boolean Admin = true;

        // Obtengo el tipo de usuario
        String[] response = new String[]{"Error"};
        try {
            response = doRequestAskUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Configuro el botón para que solo sea visible si el usuario es artista
        TextView artistsSongsBTN = findViewById(R.id.ArtistSongs);
        if (response[0].equals("200") && response[1].equals("artista")) {
            artistsSongsBTN.setVisibility(View.VISIBLE);
            // Añado el onClick también
            artistsSongsBTN.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ArtistSongs.class);
                    startActivity(intent);
                }
            });
        }
        else {
            artistsSongsBTN.setVisibility(View.GONE);
        }

        TextView boton_buscar = findViewById(R.id.bSearchConfirm);
        boton_buscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView TVquery = findViewById(R.id.menuSearch);
                String query = TVquery.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Results.class);
                intent.putExtra("query",query);
                startActivity(intent);
            }
        });

        TextView boton_aleatoria = findViewById(R.id.bRandom);
        boton_aleatoria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Player.class);
                startActivity(intent);
            }
        });

        ImageView boton_perfil = findViewById(R.id.profileIcon);
        boton_perfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        TextView boton_listas = findViewById(R.id.bPlaylist);
        boton_listas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Playlist.class);
                startActivity(intent);
            }
        });

        // Onclick para el boton de las carpetas
        TextView folderBTN = findViewById(R.id.bCarpetas);
        folderBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Carpeta.class);
                startActivity(intent);
            }
        });

        // Onclik para el boton de favoritos
        TextView favoritosBTN = findViewById(R.id.bFavs);
        favoritosBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), playlist_favoritos.class);
                startActivity(intent);
            }
        });

        // Onclick para el botón de notificaciones
        ImageView notificacionesBTN = findViewById(R.id.bellIconAMenu);
        notificacionesBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        // Onclick para el botón de amigos
        TextView amigosBTN = findViewById(R.id.bSocial);
        amigosBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserFriends.class);
                startActivity(intent);
            }
        });
    }
}