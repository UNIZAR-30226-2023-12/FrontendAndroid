package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskCreatePlaylist;

public class CreatePlaylist extends AppCompatActivity {
    protected String doRequest() throws ExecutionException, InterruptedException {
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

        MyTaskCreatePlaylist task = new MyTaskCreatePlaylist();
        return task.execute(nombre, privacidad).get();
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(response == "200"){
                    /*
                    * TODO: Cambiar el intent para que vaya a la pantalla de la playlist creada y se muestre
                    * */
                    Toast.makeText(getApplicationContext(), "Playlist creada correctamente", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error al crear la playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}