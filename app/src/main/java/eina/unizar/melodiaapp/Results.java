package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskGlobalSearchResults;
import eina.unizar.melodiaapp.Modules.MyTaskAskNamePlaylist;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameSongs;
import eina.unizar.melodiaapp.Modules.MyTaskAskPlaylists;
import eina.unizar.melodiaapp.Modules.MyTaskAskProfile;
import eina.unizar.melodiaapp.Modules.MyTaskAskTopReproductions;
import eina.unizar.melodiaapp.Modules.MyTaskByWordSearch;
import eina.unizar.melodiaapp.Modules.MyTaskDeletePlaylist;
import eina.unizar.melodiaapp.Modules.MyTaskGetListFolder;
import eina.unizar.melodiaapp.Modules.MyTaskSubscribeToArtist;

public class Results extends AppCompatActivity {

    private String listaIdResultados[]; //TODO asegurar que diferenciamos entre tipos de id

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

    public String doRequestAskNameSongs(String idPlaylist) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskNameSongs task = new MyTaskAskNameSongs();
        String respuesta = task.execute(idUsuario, contrasenya, idPlaylist).get();
        String[] response = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    protected String doRequestAskArtistName(String idUsuario) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences

        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskProfile task = new MyTaskAskProfile();
        String respuesta = task.execute(idUsr, contrasenya, idUsuario).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[2];
        } else {
            return "Error";
        }
    }

    protected String[] doRequestAskGlobalSearchResults(String query, String n) throws ExecutionException, InterruptedException {

        MyTaskAskGlobalSearchResults task = new MyTaskAskGlobalSearchResults();
        String respuesta = task.execute(query, n).get();
        String response[] = respuesta.split(";");

        if (response[0].equals("200")) {
            return response;
        } else {
            return new String[]{"Error"};
        }
    }

    protected String[] doRequestAskMyTaskByWordSearch(String query, String n) throws ExecutionException, InterruptedException {

        MyTaskByWordSearch task = new MyTaskByWordSearch();
        String respuesta = task.execute(query, n).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        } else {
            return new String[]{"Error"};
        }
    }

    protected String[] doRequestAskTop10() throws ExecutionException, InterruptedException {

        MyTaskAskTopReproductions task = new MyTaskAskTopReproductions();
        String respuesta = task.execute("10", "False").get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        } else {
            return new String[]{"Error"};
        }
    }

    protected String[] doRequestGetListFolder(String idCarpeta) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskGetListFolder task = new MyTaskGetListFolder();
        String respuesta = task.execute(idUsuario, contrasenya, idCarpeta).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String doRequestDeletePlaylist(String idPlaylist) throws ExecutionException, InterruptedException {
        // Obtengo usuario, contraseña e id de la playlist a modificar
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Hago la petición
        MyTaskDeletePlaylist task = new MyTaskDeletePlaylist();

        return task.execute(idUsuario, contrasenya, idPlaylist).get();
    }

    protected String doRequestSubscribeToArtist(String idArtist) throws ExecutionException, InterruptedException {
        // Obtengo usuario, contraseña e id del artista a seguir
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Hago la petición
        MyTaskSubscribeToArtist task = new MyTaskSubscribeToArtist();

        return task.execute(idUsuario, contrasenya, idArtist).get();
    }

    protected String whatAmI(String id) {

        String response[] = id.split(":");

        if (response[0].equals("idAudio")) {
            return "cancion";
        } else if (response[0].equals("usuario")) {
            return "artista";
        } else if (response[0].equals("lista")) {
            return "playlist";
        } else {
            return "iAmError";
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                if (extras.getString(("mode")).equals("regularSearch") || extras.getString(("mode")).equals("randomizer")) {


                    String searchQuery = extras.getString("query");
                    //String searchN = extras.getString("amount"); de momento lo hardcodeamos
                    String searchN = "15";

                    listaIdResultados = doRequestAskGlobalSearchResults(searchQuery, searchN); //Id objeto

                    if (listaIdResultados[0].equals("Error")) {
                        Toast.makeText(getApplicationContext(), "Error al obtener los resultados", Toast.LENGTH_SHORT).show();
                    } else {
                        //Obtengo los nombres de los elementos
                        String nombresElementos[] = new String[listaIdResultados.length - 1];

                        Integer i = 1;
                        int contUsers = 0;
                        int contSongs = 0;
                        int contLists = 0;
                        String[] idsUsers = new String[listaIdResultados.length - 1];
                        String[] idsSongs = new String[listaIdResultados.length - 1];
                        String[] idsLists = new String[listaIdResultados.length - 1];
                        for (i = 0; i < listaIdResultados.length-1; i++) {

                            //Necesitamos diferenciar entre canción, artista y autor
                            switch (whatAmI(listaIdResultados[i + 1])) {
                                case "cancion":
                                    // Si el elemento es una canción
                                    idsSongs = new String[listaIdResultados[i + 1].split(",").length];
                                    for (int j = 0; j < listaIdResultados[i + 1].split(",").length; j++) {
                                        nombresElementos[contSongs] = doRequestAskNameSongs(listaIdResultados[i + 1].split(",")[j]);
                                        idsSongs[contSongs] = listaIdResultados[i + 1].split(",")[j];
                                        contSongs++;
                                    }
                                    break;
                                case "artista":
                                    // Si el elemento es un artista
                                    idsUsers = new String[listaIdResultados[i + 1].split(",").length];
                                    for (int j = 0; j < listaIdResultados[i + 1].split(",").length; j++) {
                                        nombresElementos[contUsers] = doRequestAskArtistName(listaIdResultados[i + 1].split(",")[j]);
                                        idsUsers[contUsers] = listaIdResultados[i + 1].split(",")[j];
                                        contUsers++;
                                    }
                                    break;
                                case "playlist":
                                    // Si el elemento es una playlist
                                    idsLists = new String[listaIdResultados[i + 1].split(",").length];
                                    for (int j = 0; j < listaIdResultados[i + 1].split(",").length; j++) {
                                        nombresElementos[contLists] = doRequestAskNameListas(listaIdResultados[i + 1].split(",")[j]);
                                        idsLists[contLists] = listaIdResultados[i + 1].split(",")[j];
                                        contLists++;
                                    }
                                    break;
                                default:
                                    // handle unknown result
                                    break;
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview_item, R.id.listTextViewSingle, nombresElementos);
                        ListView listView = findViewById(R.id.listResults);
                        listView.setAdapter(adapter);

                        int totalResultados =  contSongs + contUsers + contLists - 1;
                        listaIdResultados = new String[totalResultados + 1];
                        listaIdResultados[0] = "200";
                        int cont = 0;
                        for (int j = cont; j < contSongs; j++) {
                            listaIdResultados[j + 1] = idsSongs[j];
                            cont++;
                        }
                        for (int j = cont; j < contUsers; j++) {
                            listaIdResultados[j + 1] = idsUsers[j];
                            cont++;
                        }
                        for (int j = cont; j < contLists; j++) {
                            listaIdResultados[j + 1] = idsLists[j];
                            cont++;
                        }


                        /*
                         * Bucle para añadir un tag con el id de cada elemento.
                         * Se podría añadir en el bucle anterior pero de momento
                         * esta separado para facilitar cambios al código
                         */
                        for (int j = 0; j < totalResultados; j++) {
                            // Obtener una referencia a la lista en concreto
                            View listItem = adapter.getView(j, null, listView);
                            TextView textView = listItem.findViewById(R.id.listTextViewSingle);

                            // Añadir el tag con la id de la lista
                            String idElemento = listaIdResultados[j + 1].split(",")[0];
                            textView.setTag(idElemento);

                            LayoutInflater inflater = getLayoutInflater();
                            View header = inflater.inflate(R.layout.textview_item, listView, false);
                            listView.addHeaderView(header, null, false);

                            TextView row = header.findViewById(R.id.listTextViewSingle);
                            String tipo = whatAmI(idElemento);
                            if (tipo.equals("cancion")) {
                                row.setText(nombresElementos[j] + ",c");
                            }
                            else if (tipo.equals("artista")) {
                                row.setText(nombresElementos[j] + ",a");
                            }
                            else if (tipo.equals("playlist")) {
                                row.setText(nombresElementos[j] + ",p");
                            }
                            row.setTag(idElemento);


                            TextView btnView = header.findViewById(R.id.listTextViewSingle);
                            String[] finalSongs = new String[idsSongs.length+1];
                            finalSongs[0] = "200";
                            System.arraycopy(idsSongs, 0, finalSongs, 1, idsSongs.length);
                            String finalSongsString = String.join(",", finalSongs);

                            // Añado "200" a la primera posición
                            btnView.setOnClickListener(new AdapterView.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] test = new String[listaIdResultados.length - 1];
                                    String idElemento = (String) v.getTag();


                                    System.arraycopy(listaIdResultados, 1, test, 0, test.length);
                                    Toast.makeText(getApplicationContext(), "Elemento Id: " + idElemento, Toast.LENGTH_SHORT).show();
                                    //Por defecto lo escribimos como cancionActual, pero si no es asi lo cambiamos
                                    SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("idCancionActual", idElemento);
                                    editor.putString("idsCancionesPlaylist", finalSongsString);
                                    editor.apply();

                                    switch (whatAmI(idElemento)) {
                                        case "cancion":
                                            // Si el elemento es una canción
                                            Intent intent = new Intent(getApplicationContext(), Player.class);
                                            intent.putExtra("tipoRep", "playlistNormal");
                                            intent.putExtra("playingMode", "linear");
                                            intent.putExtra("idCancionActual", idElemento);
                                            startActivity(intent);
                                            break;
                                        case "artista":
                                            // Si el elemento es un artista
                                            try {
                                                String response = doRequestSubscribeToArtist(idElemento);
                                                if (response.equals("200")) {
                                                    Toast.makeText(getApplicationContext(), "Se ha suscrito al artista", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(), "Error al suscribirse al artista", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "playlist":
                                            // Si el elemento es una playlist
                                            preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                                            editor.putString("idPlaylistActual", idElemento);
                                            editor.apply();
                                            Intent intentP = new Intent(getApplicationContext(), listaReproduccion.class);
                                            startActivity(intentP);
                                            break;
                                        default:
                                            // handle unknown result
                                            break;
                                    }


                                }
                            });
                        }

                        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
                    }
                }
                else{

                    if(extras.getString(("mode")).equals("top10")){
                        listaIdResultados = doRequestAskTop10(); //Id canción
                        if (listaIdResultados[0].equals("Error")) {
                            Toast.makeText(getApplicationContext(), "Error al obtener los resultados", Toast.LENGTH_SHORT).show();
                        } else {
                            //Obtengo los nombres de los elementos
                            String nombresElementos[] = new String[listaIdResultados.length - 1];

                            int i = 1;
                            for (i = 1; i <= nombresElementos.length; i++) {

                                nombresElementos[i - 1] = doRequestAskNameSongs(listaIdResultados[i]);

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview_item, R.id.listTextViewSingle, nombresElementos);
                            ListView listView = findViewById(R.id.listResults);
                            listView.setAdapter(adapter);


                            /*
                             * Bucle para añadir un tag con el id de cada canción.
                             * Se podría añadir en el bucle anterior pero de momento
                             * esta separado para facilitar cambios al código
                             */
                            for (int j = 0; j < listaIdResultados.length - 1; j++) {
                                // Obtener una referencia a la lista en concreto
                                View listItem = adapter.getView(j, null, listView);
                                TextView textView = listItem.findViewById(R.id.listTextViewSingle);

                                // Añadir el tag con la id de la lista
                                String idElemento = listaIdResultados[j + 1];
                                textView.setTag(idElemento);

                                LayoutInflater inflater = getLayoutInflater();
                                View header = inflater.inflate(R.layout.textview_item, listView, false);
                                listView.addHeaderView(header, null, false);

                                TextView row = header.findViewById(R.id.listTextViewSingle);
                                row.setText(nombresElementos[j]);
                                row.setTag(listaIdResultados[j + 1]);


                                TextView btnView = header.findViewById(R.id.listTextViewSingle);
                                btnView.setOnClickListener(new AdapterView.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String[] test = new String[listaIdResultados.length - 1];
                                        String idElemento = (String) v.getTag();


                                        System.arraycopy(listaIdResultados, 1, test, 0, test.length);
                                        Toast.makeText(getApplicationContext(), "Canción Id: " + idElemento, Toast.LENGTH_SHORT).show();
                                        //Por defecto lo escribimos como cancionActual, pero si no es asi lo cambiamos
                                        SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();

                                        editor.putString("idCancionActual", idElemento);
                                        editor.putString("idsCancionesPlaylist", String.join(",", test));
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), Player.class);
                                        intent.putExtra("tipoRep", "playlistNormal");
                                        intent.putExtra("playingMode", "linear");
                                        startActivity(intent);

                                    }
                                });
                            }

                            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
                        }
                    }
                    else if (extras.getString(("mode")).equals("carpeta")) {
                        String idCarpeta = extras.getString("idCarpeta");
                        String listaListasRepUser[] = doRequestGetListFolder(idCarpeta);

                        //Obtengo los nombres de las listas
                        String nombresListas[] = new String[listaListasRepUser.length - 1];
                        Integer i = 1;
                        for (i = 1; i < listaListasRepUser.length; i++) {
                            nombresListas[i - 1] = doRequestAskNameListas(listaListasRepUser[i]);
                            // Si el nombre es la de Favoritos se guarda en SharedPreferences con su id
                            if (nombresListas[i - 1].equals("Favoritos")) {
                                SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("idListaFavoritos", listaListasRepUser[i]);
                                editor.apply();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.playlist_item, R.id.listTextView, nombresListas);
                        ListView listView = findViewById(R.id.listResults);
                        listView.setAdapter(adapter);


                        /*
                         * Bucle para añadir un tag con el id de cada lista.
                         * Se podría añadir en el bucle anterior pero de momento
                         * esta separado para facilitar cambios al código
                         */
                        for (int j = 0; j < listaListasRepUser.length-1; j++) {
                            // Obtener una referencia a la lista en concreto
                            View listItem = adapter.getView(j, null, listView);
                            TextView textView = listItem.findViewById(R.id.listTextView);

                            // Añadir el tag con la id de la lista
                            String idLista = listaListasRepUser[j+1];
                            textView.setTag(idLista);

                            LayoutInflater inflater = getLayoutInflater();
                            View header = inflater.inflate(R.layout.playlist_item, listView, false);
                            listView.addHeaderView(header, null, false);

                            TextView row = header.findViewById(R.id.listTextView);
                            row.setText(nombresListas[j]);
                            row.setTag(listaListasRepUser[j+1]);

                            // Añado listeners para borrar playlist y editar playlist
                            Button editBtn = header.findViewById(R.id.editButton);
                            editBtn.setOnClickListener(new AdapterView.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Obtengo el id de la lista
                                    String idLista = (String) textView.getTag();
                                    // Guardo el idLista en sharedPreferences
                                    SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("idListaChangeName", idLista);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), ChangeNamePlaylist.class);
                                    startActivity(intent);
                                }
                            });

                            ImageView deleteBtn = header.findViewById(R.id.imageView5);
                            deleteBtn.setOnClickListener(new AdapterView.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Obtengo el id de la lista
                                    String idLista = (String) textView.getTag();
                                    String response = "Error";
                                    try {
                                        response = doRequestDeletePlaylist(idLista);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if (response.equals("200")) {
                                        Toast.makeText(getApplicationContext(), "Lista de reproducción borrada", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Playlist.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Error al borrar la lista de reproducción, no tiene los permisos necesarios", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));

                    }
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ImageView profileIcon = findViewById(R.id.profileIconAResults);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconAResults);
        bellIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        ImageView homeIcon = findViewById(R.id.imageHomeResults);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });
    }
}