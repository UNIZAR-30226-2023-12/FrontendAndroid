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

/**
 * Clase que codifica la actividad escuchar desde link
 */
public class ListenFromLink extends AppCompatActivity {

    /**
     * Método que se ejecuta para llamar a la request de obtener audio desde link
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
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

    /**
     * Método que se ejecuta al crear la actividad
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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