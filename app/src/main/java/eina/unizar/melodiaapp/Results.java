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
    protected String[] doRequestAskGlobalSearchResults(String query, String n) throws ExecutionException, InterruptedException {

        MyTaskAskGlobalSearchResults task = new MyTaskAskGlobalSearchResults();
        String respuesta = task.execute(query, n).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String whatAmI(String id) {

        String response[] = id.split(":");

        if (response[0] == "idAudio"){
            return "cancion";
        }
        else if (response[0] == "idArtista"){
            return "artista";
        }
        else if (response[0] == "idLista"){
            return "playlist";
        }
        else{
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
                String searchQuery = extras.getString("query");
                //String searchN = extras.getString("amount"); de momento lo hardcodeamos
                String searchN = "15";

                listaIdResultados = doRequestAskGlobalSearchResults(searchQuery, searchN); //Id objeto
                if (listaIdResultados[0].equals("Error")) {
                    Toast.makeText(getApplicationContext(), "Error al obtener los resultados", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Obtengo los nombres de los elementos
                    String nombresElementos[] = new String[listaIdResultados.length - 1];

                    Integer i = 1;
                    for (i = 1; i <= nombresElementos.length; i++) {

                        //Necesitamos diferenciar entre canción, artista y autor
                        switch (whatAmI(listaIdResultados[i])) {
                            case "cancion":
                                // Si el elemento es una canción
                                nombresElementos[i - 1] = doRequestAskNameSongs(listaIdResultados[i]);
                                break;
                            case "artista":
                                // Si el elemento es un artista
                                //nombresElementos[i - 1] = //TODO pedir al backend una función con el nombre del artista si das su id
                                break;
                            case "playlist":
                                // Si el elemento es una playlist
                                nombresElementos[i - 1] = doRequestAskNameListas(listaIdResultados[i]);
                                break;
                            default:
                                // handle unknown result
                                break;
                        }



                    /*
                    // Si el nombre es la de Favoritos se guarda en SharedPreferences con su id
                    if (nombresListas[i - 1].equals("Favoritos")) {
                        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("idListaFavoritos", listaListasRepUser[i]);
                        editor.apply();
                    }

                    */
                    }



                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.textview_item, R.id.listTextViewSingle, nombresElementos);
                    ListView listView = findViewById(R.id.listResults);
                    listView.setAdapter(adapter);


                    /*
                     * Bucle para añadir un tag con el id de cada elemento.
                     * Se podría añadir en el bucle anterior pero de momento
                     * esta separado para facilitar cambios al código
                     */
                    for (int j = 0; j < listaIdResultados.length-1; j++) {
                        // Obtener una referencia a la lista en concreto
                        View listItem = adapter.getView(j, null, listView);
                        TextView textView = listItem.findViewById(R.id.listTextViewSingle);

                        // Añadir el tag con la id de la lista
                        String idElemento = listaIdResultados[j+1];
                        textView.setTag(idElemento);

                        LayoutInflater inflater = getLayoutInflater();
                        View header = inflater.inflate(R.layout.textview_item, listView, false);
                        listView.addHeaderView(header, null, false);

                        TextView row = header.findViewById(R.id.listTextViewSingle);
                        row.setText(nombresElementos[j]);
                        row.setTag(listaIdResultados[j+1]);


                        TextView btnView = header.findViewById(R.id.listTextViewSingle);
                        btnView.setOnClickListener(new AdapterView.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] test = new String[listaIdResultados.length - 1];
                                String idElemento = (String) v.getTag();


                                System.arraycopy(listaIdResultados, 1, test, 0, test.length);
                                Toast.makeText(getApplicationContext(), "Elemento Id: " + idElemento, Toast.LENGTH_SHORT).show();
                                //Por defecto lo escribimos como cancionActual, pero si no es asi lo cambiamos
                                SharedPreferences preferences = getSharedPreferences("cancionActual", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                switch (whatAmI(idElemento)) {
                                    case "cancion":
                                        // Si el elemento es una canción

                                        editor.putString("idCancionActual", idElemento);
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), Player.class);
                                        startActivity(intent);
                                        break;
                                    case "artista":
                                        // Si el elemento es un artista
                                        //TODO ver el perfil de un artista
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

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        


        ImageView profileIcon = findViewById(R.id.profileIconAResults);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconAResults);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        ImageView homeIcon = findViewById(R.id.imageHomeResults);
        homeIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });
    }
}