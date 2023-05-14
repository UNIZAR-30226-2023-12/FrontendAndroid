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

import eina.unizar.melodiaapp.Modules.MyTaskAskNamePlaylist;
import eina.unizar.melodiaapp.Modules.MyTaskAskPlaylists;
import eina.unizar.melodiaapp.Modules.MyTaskAskProfile;

/**
 * Clase que codifica la actividad del menu principal de la aplicación
 */
public class Menu extends AppCompatActivity {

    protected String searchKin = "regular";

    /**
     * Función que llama a la task encargada de pedir al servidor las listas de reproducción del usuario
     * Si ha ido bien devuelve un array con el código de respuesta y el json con las listas de reproducción
     * Sino devuelve un array con el código de error
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String[] doRequestAskPlaylists() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskPlaylists task = new MyTaskAskPlaylists();
        String respuesta = task.execute(idUsuario, contrasenya, idUsuario).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    /**
     * Función que llama a la task encargada de pedir al servidor el nombre de una lista de reproducción
     * Si ha ido bien devuelve un string con el nombre de la lista
     * Sino devuelve un string con el código de error
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestAskNameListas(String idLista) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskNamePlaylist task = new MyTaskAskNamePlaylist();
        String respuesta = task.execute(idUsuario, contrasenya, idLista).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    protected String[] doRequestAskUser() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskProfile task = new MyTaskAskProfile();
        String respuesta = task.execute(idUsuario, contrasenya, idUsuario).get();
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

        String[] listaListasRepUser = null; //Id playlists
        try {
            listaListasRepUser = doRequestAskPlaylists();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (listaListasRepUser[0].equals("Error")) {
            Toast.makeText(getApplicationContext(), "Error al obtener las listas de reproducción", Toast.LENGTH_SHORT).show();
        }
        else {
            //Obtengo los nombres de las listas
            String nombresListas[] = new String[listaListasRepUser.length - 1];
            Integer i = 1;
            for (i = 1; i <= nombresListas.length; i++) {
                try {
                    nombresListas[i - 1] = doRequestAskNameListas(listaListasRepUser[i]);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                // Si el nombre es la de Favoritos se guarda en SharedPreferences con su id
                if (nombresListas[i - 1].equals("Favoritos")) {
                    SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("idListaFavoritos", listaListasRepUser[i]);
                    editor.apply();
                }
            }
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

                if(searchKin.equals("regular")) {
                    intent.putExtra("mode", "regularSearch");
                }
                else if (searchKin.equals("randomizer")){
                    intent.putExtra("mode", "randomizer");
                }
                else{
                    System.out.println("Error, unknown query search mode");
                }
                startActivity(intent);
            }
        });

        TextView boton_top10 = findViewById(R.id.bTopTen);
        boton_top10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Results.class);
                intent.putExtra("mode","top10");
                startActivity(intent);
            }
        });

        TextView add_friend = findViewById(R.id.bAddFriend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddFriend.class);
                startActivity(intent);
            }
        });
        TextView boton_aleatoria = findViewById(R.id.bRandom);
        boton_aleatoria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Player.class);
                intent.putExtra("tipoRep", "RandomRep");
                intent.putExtra("playingMode","linear");
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
                intent.putExtra("key", "admin");
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

        ImageView searchKind = findViewById(R.id.searchTypeIcon);
        searchKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(searchKin.equals("regular")){
                    searchKin = "randomizer";
                    searchKind.setImageResource(R.drawable.random);
                }
                else if (searchKin.equals("randomizer")){
                    searchKin = "regular";
                    searchKind.setImageResource(R.drawable.regular);
                }
            }
        });
    }
}