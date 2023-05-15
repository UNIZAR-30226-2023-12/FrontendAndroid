package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskGetAudioFromLink;
import eina.unizar.melodiaapp.R;

public class ListenFromLink extends AppCompatActivity {

    protected String doRequestGetAudioFromLink() throws ExecutionException, InterruptedException, ExecutionException {
        // Obtengo usuario, contraseña e id de la playlist a modificar
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Obtengo el link introducido
        EditText inLink = (EditText) findViewById(R.id.inLink);
        String link = inLink.getText().toString();

        MyTaskGetAudioFromLink task = new MyTaskGetAudioFromLink();
        String respuesta = task.execute(idUsuario, contrasenya, link).get();
        String response[] = respuesta.split(",");

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
        setContentView(R.layout.activity_listen_from_link);

        // OnClick para el botón de obtener
        TextView btnObtener = findViewById(R.id.bAcceder);
        btnObtener.setOnClickListener(v -> {
            String audio = "Error";
            try {
                audio = doRequestGetAudioFromLink();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if (!audio.equals("Error")) {
                SharedPreferences preferences = getSharedPreferences("playlistActual", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("idCancionActual", audio);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), Player.class);
                intent.putExtra("tipoRep", "individual");
                intent.putExtra("idCancionActual", audio);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Error al obtener el audio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}