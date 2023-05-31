package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskEsPodcast;
import eina.unizar.melodiaapp.Modules.MyTaskAskGenreSong;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameSongs;
import eina.unizar.melodiaapp.Modules.MyTaskAskSongs;
import eina.unizar.melodiaapp.Modules.MyTaskDeleteSongLista;

/**
 * Clase que codifica la actividad de la playlist de favoritos
 */
public class playlist_favoritos extends AppCompatActivity {

    /**
     * Hace una request para obtener las canciones de una playlist
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * Hace una request para obtener el nombre de una canción
     * @param idSong id de la canción
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * Hace una request para obtener el género de una canción
     * @param idAudio id de la canción
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String doRequestAskGenreSong(String idAudio) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskGenreSong task = new MyTaskAskGenreSong();
        String respuesta = task.execute(idUsuario, contrasenya, idAudio).get();
        String[] response = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    /**
     * Hace una request para obtener si un audio es podcast o canción
     * @param idAudio id del audio
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String doRequestAskEsPodcast(String idAudio) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskEsPodcast task = new MyTaskAskEsPodcast();
        String respuesta = task.execute(idUsuario, contrasenya, idAudio).get();
        String[] response = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    /**
     * Hace una request para eliminar una canción de una playlist
     * @param idPlaylist id de la playlist
     * @param idCancion id de la canción
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestDeleteSongLista(String idPlaylist, String idCancion) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskDeleteSongLista task = new MyTaskDeleteSongLista();
        String respuesta = task.execute(idUsuario, contrasenya, idPlaylist, idCancion).get();
        String[] response = respuesta.split(",");

        if (response[0].equals("200")) {
            return "200";
        }
        else {
            return "Error";
        }
    }

    /**
     * Clase que codifica el adaptador de la playlist de favoritos
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

        // Utilizaré sharedPreferences para almacenar la playlist
        SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
        String idsCancionesString = String.join(",", idsSongs);
        String nombresCancionesString = "";
        String generosCancionesString = "";
        String tipoString = "";

        if (idsSongs[0].equals("200")) {
            String[] nombresSongs = new String[idsSongs.length - 1];
            String[] generosSongs = new String[idsSongs.length - 1];
            String[] tipo = new String[idsSongs.length - 1];
            for (int i = 1; i < idsSongs.length; i++) {
                try {
                    nombresSongs[i - 1] = doRequestAskNameSongs(idsSongs[i]);
                    generosSongs[i - 1] = doRequestAskGenreSong(idsSongs[i]);
                    tipo[i - 1] = doRequestAskEsPodcast(idsSongs[i]);
                    if (generosSongs[i - 1].equals("Error") && tipo[i - 1].equals("no")) {
                        generosSongs[i - 1] = "Podcast";
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            nombresCancionesString = String.join(",", nombresSongs);
            generosCancionesString = String.join(",", generosSongs);
            tipoString = String.join(",", tipo);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("idsCanciones", idsCancionesString);
            editor.putString("nombresCanciones", nombresCancionesString);
            editor.putString("generosCanciones", generosCancionesString);
            editor.putString("tipo", tipoString);
            editor.apply();

            // Muestro los nombres de las canciones en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresSongs);
            ListView listView = findViewById(R.id.listSongsArtist);
            listView.setAdapter(adapter);

            for (int j = 0; j < idsSongs.length-1; j++) {
                View item = adapter.getView(j, null, listView);
                item.setTag(idsSongs[j+1]);
                TextView textView = item.findViewById(R.id.listTextView);

                LayoutInflater inflater = getLayoutInflater();
                View header = inflater.inflate(R.layout.cancion_item, null);
                listView.addHeaderView(header);

                TextView row = header.findViewById(R.id.listTextView);
                row.setText(nombresSongs[j]);
                row.setTag(idsSongs[j+1]);

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO ENVIAR AL REPRODUCTOR LA PLAYLIST
                        String idCancion = (String) v.getTag();
                        SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("idCancionActual", idCancion);
                        editor.putString("idsCancionesPlaylist", idsCancionesString);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Player.class);
                        intent.putExtra("tipoRep", "playlistNormal");
                        intent.putExtra("playingMode","linear");
                        startActivity(intent);
                    }
                });

                ImageView deleteBtn = header.findViewById(R.id.imageView5);
                deleteBtn.setTag(idsSongs[j+1]);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String idCancion = v.getTag().toString();
                        SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                        String idPlaylist = preferences.getString("idPlaylistActual", "");
                        try {
                            doRequestDeleteSongLista(idPlaylist, idCancion);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        recreate();
                    }
                });

            }
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
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

        // OnClick para el botón de ordenar por genero
        TextView genreBTN = findViewById(R.id.bOrdenarGenero);
        genreBTN.setOnClickListener(v -> {
            SharedPreferences preferences1 = getSharedPreferences("playlistActual", MODE_PRIVATE);
            String idsCanciones = preferences1.getString("idsCanciones", "");
            String nombresCanciones = preferences1.getString("nombresCanciones", "");
            String generosCanciones = preferences1.getString("generosCanciones", "");

            // Obtener una referencia al ListView
            ListView listView = findViewById(R.id.listSongsArtist);
            // Obtener la vista de encabezado
            for (int i = 0; i < listView.getChildCount(); i++) {
                View header = listView.getChildAt(i);
                listView.removeHeaderView(header);
            }

            genreBTN.setClickable(false);
            ordenarPorWildcard(idsCanciones, nombresCanciones, generosCanciones);
        });

        // OnClick para el botón de ordenar por tipo
        TextView typeBTN = findViewById(R.id.bOrdenarTipo);
        typeBTN.setOnClickListener(v -> {
            SharedPreferences preferences1 = getSharedPreferences("playlistActual", MODE_PRIVATE);
            String idsCanciones = preferences1.getString("idsCanciones", "");
            String nombresCanciones = preferences1.getString("nombresCanciones", "");
            String tipo = preferences1.getString("tipo", "");

            // Obtener una referencia al ListView
            ListView listView = findViewById(R.id.listSongsArtist);
            // Obtener la vista de encabezado
            for (int i = 0; i < listView.getChildCount(); i++) {
                View header = listView.getChildAt(i);
                listView.removeHeaderView(header);
            }

            typeBTN.setClickable(false);
            ordenarPorWildcard(idsCanciones, nombresCanciones, tipo);
        });
    }

    /**
     * Clase para ordenar los objetos por el wildcard
     *
     * @param idsCancionesString String con los ids de las canciones
     * @param nombresCancionesString String con los nombres de las canciones
     * @param wildcardString String con los wildcards de las canciones
     */
    public void ordenarPorWildcard(String idsCancionesString, String nombresCancionesString, String wildcardString) {
        String[] idsCanciones = idsCancionesString.split(",");
        String[] nombresCanciones = nombresCancionesString.split(","); //Seguimos necesitando los nombres para el setText
        String[] wildcardCanciones = wildcardString.split(",");

        playlist_favoritos.ObjetoConIdNombreYWildcard[] objetos = new playlist_favoritos.ObjetoConIdNombreYWildcard[idsCanciones.length-1];
        for (int i = 1; i < idsCanciones.length; i++) {
            objetos[i-1] = new ObjetoConIdNombreYWildcard(idsCanciones[i], nombresCanciones[i-1], wildcardCanciones[i-1]);
        }


        // Ordenar el array de objetos por el nombre utilizando un comparador
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.sort(objetos, Comparator.comparing(ObjetoConIdNombreYWildcard::getWildcard));
        }

        SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String idsCancionesOrdenadas = "";
        String nombresCancionesOrdenadas = "";
        String wildcardCancionesOrdenadas = "";
        for (int i = 0; i < objetos.length; i++) {
            idsCancionesOrdenadas += objetos[i].getId() + ",";
            nombresCancionesOrdenadas += objetos[i].getNombre() + ",";
            wildcardCancionesOrdenadas += objetos[i].getWildcard() + ",";
        }
        editor.putString("idsCanciones", idsCancionesOrdenadas);
        editor.putString("nombresCanciones", nombresCancionesOrdenadas);
        editor.putString("wildcardCanciones", wildcardCancionesOrdenadas);
        editor.apply();

        nombresCanciones = nombresCancionesOrdenadas.split(",");
        idsCanciones = idsCancionesOrdenadas.split(",");
        wildcardCanciones = wildcardCancionesOrdenadas.split(",");

        // Añado un elemento a las izqda que sea "200"
        String[] idsCanciones2 = new String[idsCanciones.length+1];
        idsCanciones2[0] = "200";
        for (int i = 0; i < idsCanciones.length; i++) {
            idsCanciones2[i+1] = idsCanciones[i];
        }
        idsCanciones = idsCanciones2;

        // Añado un elemento a las izqda que sea "200"
        String[] nombresCanciones2 = new String[wildcardCanciones.length+1];
        nombresCanciones2[0] = "200";
        for (int i = 0; i < wildcardCanciones.length; i++) {
            nombresCanciones2[i+1] = wildcardCanciones[i];
        }
        wildcardCanciones = nombresCanciones2;

        // Añado un elemento a las izqda que sea "200"
        String[] wildcardCanciones2 = new String[wildcardCanciones.length+1];
        wildcardCanciones2[0] = "200";
        for (int i = 0; i < wildcardCanciones.length; i++) {
            wildcardCanciones2[i+1] = wildcardCanciones[i];
        }
        wildcardCanciones = wildcardCanciones2;

        int length = nombresCanciones.length;
        // Pongo las canciones en pantalla
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresCanciones);
        ListView listView = findViewById(R.id.listSongsArtist);
        listView.setAdapter(adapter);

        for (int j = 0; j < idsCanciones.length-1; j++) {
            View item = adapter.getView(j, null, listView);
            item.setTag(idsCanciones[j+1]);
            TextView textView = item.findViewById(R.id.listTextView);

            LayoutInflater inflater = getLayoutInflater();
            View header = inflater.inflate(R.layout.cancion_item, null);
            listView.addHeaderView(header);

            TextView row = header.findViewById(R.id.listTextView);
            row.setText(nombresCanciones[j]);
            row.setTag(idsCanciones[j+1]);

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

        }
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));

    }

    /**
     * Clase que representa un objeto con id, nombre y wildcard
     */
    class ObjetoConIdNombreYWildcard {

        private String id;

        private String nombre;

        private String wildcard; //Puede ser genero, artista, u otra cosa si fuese necesario

        /**
         * Constructor
         * @param id id del objeto
         * @param nombre nombre del objeto
         * @param wildcard wildcard del objeto
         */
        public ObjetoConIdNombreYWildcard(String id, String nombre, String wildcard) {
            this.id = id;
            this.nombre = nombre;
            this.wildcard = wildcard;
        }

        /**
         * Getter del id
         * @return id
         */
        public String getId() {
            return id;
        }

        /**
         * Getter del nombre
         * @return nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Getter del wildcard
         * @return wildcard
         */
        public String getWildcard() {
            return wildcard;
        }
    }
}