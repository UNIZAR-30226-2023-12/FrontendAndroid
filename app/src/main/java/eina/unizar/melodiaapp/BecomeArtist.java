package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskArtist;

public class BecomeArtist extends AppCompatActivity {
    protected String doRequest() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Obtengo los campos de la pantalla
        EditText motivo = findViewById(R.id.inReason);
        EditText url = findViewById(R.id.urlAudioArtist);

        MyTaskAskArtist task = new MyTaskAskArtist();
        String respuesta = task.execute(idUsuario, contrasenya, url.getText().toString(), motivo.getText().toString()).get();
        return respuesta;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_artist);

        // Añado el listener para el botón de solicitar
        TextView requestbtn = findViewById(R.id.bSolicitar);
        requestbtn.setOnClickListener(v -> {
            try {
                String response = doRequest();
                if (response.equals("200")) {
                    Toast.makeText(getApplicationContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error al enviar la solicitud, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, Profile.class);
                startActivity(intent);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}