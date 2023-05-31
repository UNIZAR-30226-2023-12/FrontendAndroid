package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskUploadAudio;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Clase que implementa la subida de audios
 */
public class upload_audio extends AppCompatActivity {

    private String ruta = "Error";
    /**
     * Hace la petición para subir una canción
     * Y convierte la ruta obtenida en un string en base64
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    protected String doRequestUpload(String rutaAudio) throws ExecutionException, InterruptedException, IOException {
        // Obtengo usuario y contraseña de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");
        String nombreArtista = preferences.getString("nombre", "");

        // Obtengo los campos de la pantalla
        EditText nombre = findViewById(R.id.Name);
        EditText esCancion = findViewById(R.id.esCancion);
        EditText duracion = findViewById(R.id.inDuracion);
        EditText genero = findViewById(R.id.InGeneros);
        EditText calidad = findViewById(R.id.InCalidad);

        // Convierto el fichero en un string en base64
        String[] absolutePath = ruta.split(":");
        absolutePath[0] = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + absolutePath[1];
        File file = new File(absolutePath[0]);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, "UTF-8");
        // convierto str a base64
        String rutaFinal = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
        String fichero = new String(rutaFinal.getBytes("UTF-8"));

        Pattern patron = Pattern.compile("[\\p{Cntrl}]");

        // Eliminar caracteres de control
        Matcher matcher = patron.matcher(fichero);
        String cadenaLimpia = matcher.replaceAll("");

        // Hago la petición para subir la canción
        MyTaskUploadAudio task = new MyTaskUploadAudio();

        return task.execute(idUsuario, contrasenya, nombre.getText().toString(), cadenaLimpia, esCancion.getText().toString(), duracion.getText().toString(), genero.getText().toString(), calidad.getText().toString(), nombreArtista).get();
    }

    /**
     * Crea la actividad e inicializa los elementos de la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        Intent intent2 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent2.setData(uri);
        startActivity(intent2);


        // Onclick para el botón de seleccionar audio
        findViewById(R.id.SelectAudio).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, 10);
        });

        // Onclick para el botón de subir canción
        findViewById(R.id.bSubir).setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), this.ruta, Toast.LENGTH_LONG).show();
            String response = "Error";
            try {
                response = doRequestUpload(this.ruta);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.equals("200")) {
                Toast.makeText(getApplicationContext(), "Canción subida correctamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(upload_audio.this, Menu.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Error al subir la canción, código: " + response, Toast.LENGTH_LONG).show();
            }
        });


    }

    /**
     * Devuelve la ruta del audio seleccionado
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String rutaAudio = data.getData().getPath();
            this.ruta = rutaAudio;
        }
    }
}