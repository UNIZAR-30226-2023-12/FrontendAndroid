package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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

import eina.unizar.melodiaapp.Modules.MyTaskAskPlaylists;
import eina.unizar.melodiaapp.Modules.MyTaskAskNamePlaylist;
import eina.unizar.melodiaapp.Modules.MyTaskChangeNamePlaylist;
import eina.unizar.melodiaapp.Modules.MyTaskSetSongLista;

/**
 * Clase que codifica la actividad de lista de reproducción
 */
public class Playlist extends AppCompatActivity {

    private String listaListasRepUser[];
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

    /**
     * Función que llama a la task encargada de añadir una canción a una lista de reproducción
     * Si ha ido bien devuelve el resultado de task.execute
     * Sino devuelve un código de error
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestSetSongLista(String idSong, String idLista) throws ExecutionException, InterruptedException {
        // Obtengo usuario, contraseña e id de la playlist a modificar
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Obtengo el nombre de la playlist
        TextView nombrePlaylist = findViewById(R.id.NewPlaylistName);
        String nombre = nombrePlaylist.getText().toString();

        // Hago la petición
        MyTaskSetSongLista task = new MyTaskSetSongLista();

        return task.execute(idUsuario, contrasenya, idLista, idSong).get();
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
        setContentView(R.layout.activity_playlist);



        try {
            listaListasRepUser = doRequestAskPlaylists(); //Id playlists
            if (listaListasRepUser[0].equals("Error")) {
                Toast.makeText(getApplicationContext(), "Error al obtener las listas de reproducción", Toast.LENGTH_SHORT).show();
            }
            else {
                //Obtengo los nombres de las listas
                String nombresListas[] = new String[listaListasRepUser.length - 1];
                Integer i = 1;
                for (i = 1; i <= nombresListas.length; i++) {
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
                ListView listView = findViewById(R.id.listPlsylist);
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

                            //TODO hacer la request al backend que borre la lista
                        }
                    });
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] test = new String[listaListasRepUser.length - 1];
                        String idLista = "" + test[position];

                        Bundle extras = getIntent().getExtras();
                        if (extras != null) {
                            String option = extras.getString("key");
                            String idAudio = extras.getString("songId");

                            if (option == "Append"){//Estamos en modo añadir canción
                                //Función añadir cancion
                                Toast.makeText(getApplicationContext(), "Añadir canción en playlist Id: " + idLista, Toast.LENGTH_SHORT).show();
                                try {//Intentamos realizar el cambio en el backend
                                    String addResult = doRequestSetSongLista(idAudio, idLista);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                finish();
                            }
                        }
                        else {
                            System.arraycopy(listaListasRepUser, 1, test, 0, test.length);
                            Toast.makeText(getApplicationContext(), "Playlist Id: " + test[position], Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ImageView profileIcon = findViewById(R.id.profileIconAplaylist);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconAPlaylist);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        TextView createPlaylist = findViewById(R.id.bCrearLista);
        createPlaylist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePlaylist.class);
                startActivity(intent);
                //fillData();
            }
        });

        // Configuración del listener para el botón home
        ImageView homeIcon = findViewById(R.id.imageHomeCCarpeta);
        homeIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Función que se encarga de rellenar la lista de reproducción con los datos de la base de datos
     */
    /*
    private void fillData() {
        TextView playlistName = findViewById(R.id.ListaRejemplo);
        playlistName.setText("Prueba");
    }
    */
}