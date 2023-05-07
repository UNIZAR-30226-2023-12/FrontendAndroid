package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskNameSongs;
import eina.unizar.melodiaapp.Modules.MyTaskAskSongs;

public class listaReproduccion extends AppCompatActivity {

    protected  ArrayAdapter<String> adapterPrueba;

    public String[] doRequestAskSongs(String idPlaylist) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskSongs task = new MyTaskAskSongs();
        String respuesta = task.execute(idUsuario, contrasenya, idPlaylist).get();
        String[] response = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reproduccion);
        ImageView profileIcon = findViewById(R.id.profileIconAlistarep);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });


        ImageView bellIcon = findViewById(R.id.bellIconAlistarep);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        // Configuración del listener para el botón home
        ImageView homeIcon = findViewById(R.id.menuIconAlistarep);
        homeIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        // Botón para la ordenación por nombre
        TextView ordenarNombre = findViewById(R.id.bOrdenarNombre);
        ordenarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                String idsCanciones = preferences.getString("idsCanciones", "");
                String nombresCanciones = preferences.getString("nombresCanciones", "");

                // Obtener una referencia al ListView
                ListView listView = findViewById(R.id.listCRep);
                // Obtener la vista de encabezado
                for (int i = 0; i < listView.getChildCount(); i++) {
                    View headerView = listView.getChildAt(i);
                    listView.removeHeaderView(headerView);
                }

                ordenarPorNombre(idsCanciones, nombresCanciones);
            }
        });

        // Pongo las canciones en pantalla y almaceno los ids en Shared preferences
        SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
        String idPlaylist = preferences.getString("idPlaylistActual", "");

        String[] idsCanciones = new String[]{"Error"};
        try {
            idsCanciones = doRequestAskSongs(idPlaylist);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        String idsCancionesString = String.join(",", idsCanciones);
        String nombresCancionesString = "";

        String[] nombresCanciones = new String[idsCanciones.length-1];
        for (int i = 1; i < idsCanciones.length; i++) {
            try {
                nombresCanciones[i-1] = doRequestAskNameSongs(idsCanciones[i]);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        nombresCancionesString = String.join(",", nombresCanciones);
        // Guardo los ids y los nombres de las canciones en shared preferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idsCanciones", idsCancionesString.substring(idsCancionesString.indexOf(",") + 1));
        editor.putString("nombresCanciones", nombresCancionesString);
        editor.apply();

        // Pongo las canciones en pantalla
        adapterPrueba = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresCanciones);
        ListView listView = findViewById(R.id.listCRep);
        listView.setAdapter(adapterPrueba);

        for (int j = 0; j < idsCanciones.length-1; j++) {
            View item = adapterPrueba.getView(j, null, listView);
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

        public void ordenarPorNombre(String idsCancionesString, String nombresCancionesString) {
            String[] idsCanciones = idsCancionesString.split(",");
            String[] nombresCanciones = nombresCancionesString.split(",");
            ObjetoConIdYNombre[] objetos = new ObjetoConIdYNombre[idsCanciones.length];
            for (int i = 0; i < idsCanciones.length; i++) {
                objetos[i] = new ObjetoConIdYNombre(idsCanciones[i], nombresCanciones[i]);
            }

            // Ordenar el array de objetos por el nombre utilizando un comparador
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Arrays.sort(objetos, Comparator.comparing(ObjetoConIdYNombre::getNombre));
            }

            SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String idsCancionesOrdenadas = "";
            String nombresCancionesOrdenadas = "";
            for (int i = 0; i < objetos.length; i++) {
                idsCancionesOrdenadas += objetos[i].getId() + ",";
                nombresCancionesOrdenadas += objetos[i].getNombre() + ",";
            }
            editor.putString("idsCanciones", idsCancionesOrdenadas);
            editor.putString("nombresCanciones", nombresCancionesOrdenadas);
            editor.apply();

            nombresCanciones = nombresCancionesOrdenadas.split(",");
            idsCanciones = idsCancionesOrdenadas.split(",");

            // Añado un elemento a las izqda que sea "200"
            String[] idsCanciones2 = new String[idsCanciones.length+1];
            idsCanciones2[0] = "200";
            for (int i = 0; i < idsCanciones.length; i++) {
                idsCanciones2[i+1] = idsCanciones[i];
            }
            idsCanciones = idsCanciones2;

            // Añado un elemento a las izqda que sea "200"
            String[] nombresCanciones2 = new String[nombresCanciones.length+1];
            nombresCanciones2[0] = "200";
            for (int i = 0; i < nombresCanciones.length; i++) {
                nombresCanciones2[i+1] = nombresCanciones[i];
            }
            nombresCanciones = nombresCanciones2;


            // Pongo las canciones en pantalla
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresCanciones);
            ListView listView = findViewById(R.id.listCRep);
            listView.setAdapter(adapter);

            for (int j = 0; j < idsCanciones.length-1; j++) {
                View item = adapter.getView(j, null, listView);
                item.setTag(idsCanciones[j+1]);
                TextView textView = item.findViewById(R.id.listTextView);

                LayoutInflater inflater = getLayoutInflater();
                View header = inflater.inflate(R.layout.cancion_item, null);
                listView.addHeaderView(header);

                TextView row = header.findViewById(R.id.listTextView);
                row.setText(nombresCanciones[j+1]);
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
        class ObjetoConIdYNombre {
            private String id;
            private String nombre;

            public ObjetoConIdYNombre(String id, String nombre) {
                this.id = id;
                this.nombre = nombre;
            }

            public String getId() {
                return id;
            }

            public String getNombre() {
                return nombre;
            }
        }
}
