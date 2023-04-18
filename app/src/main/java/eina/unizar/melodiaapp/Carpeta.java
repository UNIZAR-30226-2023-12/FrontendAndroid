package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskFolders;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameFolder;
import eina.unizar.melodiaapp.Modules.MyTaskAskNamePlaylist;

/**
 * Clase que codifica la actividad Carpeta
 */
public class Carpeta extends AppCompatActivity {
    /**
     * Función que llama a la task encargada de pedir al servidor los ids de las carpetas del usuario
     * @return
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
     * @return
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
     * Función que rellena la lista de carpetas del usuario cada vez que es invocada
     */
    private void fillData() throws ExecutionException, InterruptedException {
        //Obtengo de shared preferences los ids de las carpetas del usuario
        SharedPreferences preferences = getSharedPreferences("carpeta", MODE_PRIVATE);
        String idsCarpetasJson = preferences.getString("idCarpetas", "[]");
        if (idsCarpetasJson.equals("[]")) {
            String idsCarpetas[] = doRequestAskFolders();
            if (idsCarpetas[0].equals("200")) {
                //Obtengo los nombres de las carpetas
                String nombresCarpetas[] = new String[idsCarpetas.length - 1];
                for (int i = 1; i < idsCarpetas.length; i++) {
                    nombresCarpetas[i - 1] = doRequestAskNameFolder(idsCarpetas[i]);
                }
                //TODO con idsCarpetasJson rellenar la lista de carpetas en la pantalla
            }
            else {
                Toast.makeText(getApplicationContext(), "Error al obtener las carpetas", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            // Ya tengo los ids guardados en cache
            //TODO con idsCarpetasJson rellenar la lista de carpetas en la pantalla
        }
    }



}