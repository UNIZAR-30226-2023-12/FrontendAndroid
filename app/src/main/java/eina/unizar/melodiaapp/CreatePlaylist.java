package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskCreatePlaylist;

public class CreatePlaylist extends AppCompatActivity {
    /**
     * Función que llama a la task encargada de crear una lista de reproducción
     * Si ha ido bien devuelve 200
     * Sino devuelve error
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JSONException
     */
    protected String doRequest() throws ExecutionException, InterruptedException, JSONException {
        EditText eTnombre = findViewById(R.id.inPlaylistName);
        RadioButton rBprivada = findViewById(R.id.radioButtonPrivate);
        RadioButton rBpublica = findViewById(R.id.radioButtonPublic);

        String nombre = eTnombre.getText().toString();
        String privacidad = "";

        if (rBprivada.isChecked()) {
            privacidad = "privada";
        } else if (rBpublica.isChecked()) {
            privacidad = "publica";
        }
        else {
            Toast.makeText(getApplicationContext(), "Error al crear la playlist, debe indicar el tipo", Toast.LENGTH_SHORT).show();
            return "Error";
        }
        // Obtengo de shared preferences el idUsuario y la contraseña del usuario logueado
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");
        String idLista = "";    // id vacío por defecto, el backend asignará el que corresponda
        String tipoLista = "listaReproduccion"; // tipo de lista de reproducción

        MyTaskCreatePlaylist task = new MyTaskCreatePlaylist();
        String respuesta = task.execute(nombre, privacidad, idUsuario, contrasenya, idLista, tipoLista).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            SharedPreferences preferencesLista = getSharedPreferences("listaReproduccion", MODE_PRIVATE);
            String idListaJson = preferencesLista.getString("idListas", "[]");
            JSONArray idListaJsonArray = new JSONArray(idListaJson);
            idListaJsonArray.put(response[1]);
            String nuevo = idListaJsonArray.toString();

            SharedPreferences.Editor editor = preferencesLista.edit();
            editor.putString("idLista", nuevo);
            editor.apply();

            return "200";
        }
        else {
            return "Error";
        }

    }

    /**
     * Función que se ejecuta al crear la actividad. Se encarga de asignar los listeners a los botones
     * de la actividad y de iniciar los distintos componentes de la pantalla
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        ImageView profileIcon = findViewById(R.id.profileIconAcreatePlaylist);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconAcreatePlaylist);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        // Añado el listener para el botón de home
        ImageView homebtn = findViewById(R.id.imageHomeCPlaylist);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        // Añado el listener para el botón de crear playlist
        TextView createPlaylistbtn = findViewById(R.id.bCrearLista);

        createPlaylistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = null;
                try {
                    response = doRequest();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
                if(response == "200"){
                    Toast.makeText(getApplicationContext(), "Playlist creada correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Playlist.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error al crear la playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}