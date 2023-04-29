package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskUploadAudio;

public class upload_audio extends AppCompatActivity {
    /**
     * Hace la petición para subir una canción
     * Y convierte la ruta obtenida en un string en base64
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    protected String doRequestUpload() throws ExecutionException, InterruptedException, IOException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Obtengo los campos de la pantalla
        EditText nombre = findViewById(R.id.Name);
        EditText ruta = findViewById(R.id.FileRoute);
        EditText esCancion = findViewById(R.id.esCancion);
        EditText duracion = findViewById(R.id.inDuracion);
        EditText genero = findViewById(R.id.InGeneros);
        EditText calidad = findViewById(R.id.InCalidad);

        // Convierto el fichero en un string en base64
        String rutaAudio = ruta.getText().toString();
        File file = new File(rutaAudio);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, "UTF-8");


        // Hago la petición para subir la canción
        MyTaskUploadAudio task = new MyTaskUploadAudio();

        return task.execute(idUsuario, contrasenya, nombre.getText().toString(), str, esCancion.getText().toString(), duracion.getText().toString(), genero.getText().toString(), calidad.getText().toString()).get();
    }

    /**
     * Crea la actividad e inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        // Onclick para el botón de subir canción
        findViewById(R.id.bSubir).setOnClickListener(v -> {
            String response = "Error";
            try {
                response = doRequestUpload();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.equals("200")) {
                Toast.makeText(getApplicationContext(), "Canción subida correctamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(upload_audio.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Error al subir la canción, código: " + response, Toast.LENGTH_LONG).show();
            }
        });


    }
}