package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskPlaylists;
import eina.unizar.melodiaapp.Modules.MyTaskAskNamePlaylist;

/**
 * Clase que codifica la actividad de lista de reproducción
 */
public class Playlist extends AppCompatActivity {
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
            String [] listaListasRepUser = doRequestAskPlaylists(); //Id playlists
            if (listaListasRepUser[0].equals("Error")) {
                Toast.makeText(getApplicationContext(), "Error al obtener las listas de reproducción", Toast.LENGTH_SHORT).show();
            }
            else {
                //Obtengo los nombres de las listas
                String nombresListas[] = new String[listaListasRepUser.length - 1];
                Integer i = 1;
                for (i = 1; i <= nombresListas.length; i++) {
                    nombresListas[i - 1] = doRequestAskNameListas(listaListasRepUser[i]);
                }

                /*
                 * Bucle para añadirun tag con el id de cada lista.
                 * Se podría añadir en el bucle anterior pero de momento
                 * esta separado para facilitar cambios al código
                 */
                ListView listView = findViewById(R.id.listPlsylist);
                for (int j = 0; j < nombresListas.length; j++) {
                    // Obtener una referencia a la lista en concreto
                    View listItem = listView.getChildAt(j);
                    TextView textView = listItem.findViewById(R.id.listTextView);

                    // Añadir el tag con la id de la lista
                    textView.setTag(listaListasRepUser[j]);
                }


                String [] prueba = {"lista1", "lista2", "lista3", "lista4", "lista1", "lista2", "lista3", "lista4", "lista1", "lista2", "lista3", "lista4", "lista1", "lista2", "lista3", "lista4"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, prueba);
                //ListView listView = findViewById(R.id.listPlsylist);
                listView.setAdapter(adapter);
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