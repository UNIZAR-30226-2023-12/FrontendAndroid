package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskCreateFolder;

/**
 * Clase que codifica la actividad crear carpeta
 */
public class CreateCarpeta extends AppCompatActivity {
    /**
     * Método que se ejecuta para llamar a la request de crear carpeta
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JSONException
     */
    protected String doRequestCreate() throws ExecutionException, InterruptedException, JSONException {
        EditText nombreCarpetaEdit = findViewById(R.id.inCapretaName);
        String nombreCarpeta = nombreCarpetaEdit.getText().toString();
        // Obtengo de shared preferences el idUsuario y la contraseña del usuario logueado
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskCreateFolder task = new MyTaskCreateFolder();
        String respuesta = task.execute(nombreCarpeta, idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            // Almaceno el id de la carpeta en shared preferences
            SharedPreferences carpetaPreferences = getSharedPreferences("carpeta", MODE_PRIVATE);
            String idCarpetaJson = carpetaPreferences.getString("idCarpetas", "[]");
            JSONArray idCarpetaArray = new JSONArray(idCarpetaJson);
            idCarpetaArray.put(response[1]);
            String nuevo = idCarpetaArray.toString();
            SharedPreferences.Editor editor = carpetaPreferences.edit();
            editor.putString("idCarpetas", nuevo);
            editor.apply();

            return "200";
        }
        else {
            return "error";
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
        setContentView(R.layout.activity_create_carpeta);

        ImageView profileIcon = findViewById(R.id.profileIconAcreateCarpeta);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconAcreateCarpeta);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        // Creo el onClick para el botón de home
        ImageView homeIcon = findViewById(R.id.imageHomeC);
        homeIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        // Creo el onClick para el botón de crear carpeta
        TextView createCarpetaIcon = findViewById(R.id.bCrearCarpeta);
        createCarpetaIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String response = null;
                try {
                    response = doRequestCreate();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException | JSONException e) {
                    e.printStackTrace();
                }

                if (response.equals("200")) {
                    Toast.makeText(getApplicationContext(), "Carpeta creada correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error al crear la carpeta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}