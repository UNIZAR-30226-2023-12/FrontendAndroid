package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
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

import eina.unizar.melodiaapp.Modules.MyTaskAskFolders;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameFolder;
import eina.unizar.melodiaapp.Modules.MyTaskAskNamePlaylist;
import eina.unizar.melodiaapp.Modules.MyTaskDeleteFolder;
import eina.unizar.melodiaapp.Modules.MyTaskSetListFolder;

/**
 * Clase que codifica la actividad Carpeta
 */
public class Carpeta extends AppCompatActivity {
    /**
     * Función que llama a la task encargada de pedir al servidor los ids de las carpetas del usuario
     * @return array de strings con los ids de las carpetas
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String[] doRequestAskFolders() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskFolders task = new MyTaskAskFolders();
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
     * Función que llama a la task encargada de pedir al servidor el nombre de una carpeta
     * Si ha ido bien devuelve un string con el nombre de la carpeta
     * Sino devuelve un string con el código de error
     * @return string con el nombre de la carpeta
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestAskNameFolder(String idFolder) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskNameFolder task = new MyTaskAskNameFolder();
        String respuesta = task.execute(idUsuario, contrasenya, idFolder).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    /**
     * Función que llama a la task para borrar una carpeta
     * @param idCarpeta id de la carpeta a borrar
     * @return string con el código de error
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestDeleteFolder(String idCarpeta) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskDeleteFolder task = new MyTaskDeleteFolder();

        return task.execute(idUsuario, contrasenya, idCarpeta).get();
    }

    /**
     * Función que llama a la task para añadir una lista de reproducción a una carpeta
     * @param idCarpeta id de la carpeta
     * @param idPlaylist id de la lista de reproducción
     * @return string con el código de la petición
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestSetListFolder(String idCarpeta, String idPlaylist) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskSetListFolder task = new MyTaskSetListFolder();

        return task.execute(idUsuario, contrasenya, idCarpeta, idPlaylist).get();
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
        setContentView(R.layout.activity_carpeta);

        try {
            fillData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView bCarpetaEjemplo = findViewById(R.id.carpetaEjemplo);
        bCarpetaEjemplo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Playlist.class);
                startActivity(intent);
            }
        });

        TextView bAddCarpeta = findViewById(R.id.bMasCarpetas);
        bAddCarpeta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateCarpeta.class);
                startActivity(intent);
            }
        });

        ImageView profileIcon = findViewById(R.id.profileIconAcarpeta);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconACarpeta);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Función que se encarga de rellenar la lista de carpetas con los datos obtenidos del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void fillData() throws ExecutionException, InterruptedException {
        //Obtengo de shared preferences los ids de las carpetas del usuario
        SharedPreferences preferences = getSharedPreferences("carpeta", MODE_PRIVATE);
        String idsCarpetasJson = preferences.getString("idCarpetas", "[]");
        if (!idsCarpetasJson.equals("[]")) {
            String idsCarpetas[] = doRequestAskFolders();
            if (idsCarpetas[0].equals("200")) {
                //Obtengo los nombres de las carpetas
                String nombresCarpetas[] = new String[idsCarpetas.length - 1];
                for (int i = 1; i < idsCarpetas.length; i++) {
                    nombresCarpetas[i - 1] = doRequestAskNameFolder(idsCarpetas[i]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.playlist_item, R.id.listTextView, nombresCarpetas);
                ListView listView = findViewById(R.id.linearLayout2);
                listView.setAdapter(adapter);

                for (int j = 0; j < idsCarpetas.length-1; j++) {
                    // Obtener una referencia a la lista en concreto
                    View listItem = adapter.getView(j, null, listView);
                    TextView textView = listItem.findViewById(R.id.listTextView);

                    // Añadir el tag con la id de la lista
                    String idLista = idsCarpetas[j+1];
                    textView.setTag(idLista);

                    LayoutInflater inflater = getLayoutInflater();
                    View header = inflater.inflate(R.layout.playlist_item, listView, false);
                    listView.addHeaderView(header, null, false);

                    TextView row = header.findViewById(R.id.listTextView);
                    row.setText(nombresCarpetas[j]);
                    row.setTag(idsCarpetas[j+1]);

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
                            String idCarpeta = (String) textView.getTag();
                            String response = "Error";
                            try {
                                response = doRequestDeleteFolder(idCarpeta);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (response.equals("200")) {
                                Toast.makeText(getApplicationContext(), "Carpeta borrada", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Error al borrar la carpeta, no tiene los permisos necesarios", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    TextView btnView = header.findViewById(R.id.listTextView);
                    btnView.setOnClickListener(new AdapterView.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] test = new String[idsCarpetas.length - 1];
                            String idCarpeta = (String) v.getTag();

                            Bundle extras = getIntent().getExtras();
                            if (extras != null) {
                                String option = extras.getString("key");
                                String idLista = extras.getString("idLista");

                                if (option.equals("Append")){//Estamos en modo añadir canción
                                    //Función añadir cancion
                                    Toast.makeText(getApplicationContext(), "Añadir playlist en carpeta Id: " + idCarpeta, Toast.LENGTH_SHORT).show();
                                    try {//Intentamos realizar el cambio en el backend
                                        String addResult = doRequestSetListFolder(idCarpeta, idLista);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    finish();
                                }
                            }
                            else {
                                System.arraycopy(idsCarpetas, 1, test, 0, test.length);
                                Intent intent = new Intent(getApplicationContext(), Results.class);
                                intent.putExtra("mode", "carpeta");
                                intent.putExtra("idCarpeta", idCarpeta);
                                startActivity(intent);
                            }
                        }
                    });
                }

                listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
            }
            else {
                Toast.makeText(getApplicationContext(), "Error al obtener las carpetas", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            //TODO dar una vuelta a cómo hay que hacer esto
        }
    }



}