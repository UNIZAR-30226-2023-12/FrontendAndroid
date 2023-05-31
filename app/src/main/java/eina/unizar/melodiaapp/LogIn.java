package eina.unizar.melodiaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import eina.unizar.melodiaapp.Modules.MyTaskLogin;
import eina.unizar.melodiaapp.Modules.MyTaskLogin;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * Clase que codifica la actividad de login
 */
public class LogIn extends AppCompatActivity {
    /**
     * Llama a la función que realiza la petición al servidor para validar usuario
     * con los datos introducidos en el formulario
     * Devuelve el resultado de la petición
     *
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequest() throws ExecutionException, InterruptedException {

        EditText eTemail = findViewById(R.id.inEmail);
        EditText eTcontra = findViewById(R.id.inPasswd);


        String email = eTemail.getText().toString();
        String contra = eTcontra.getText().toString();

        MyTaskLogin task = new MyTaskLogin();
        String respuesta = task.execute(email, contra).get();

        String response[] = respuesta.split(",");
        // Accedo a la primera posición del array, que es el código de respuesta
        if (response[0].equals("200")) {
            // Accedo a la segunda posición del array que es el idUsuario
            String idUsuario = response[1];
            // Guardo el idUsuario y la contraseña en el fichero de preferencias
            SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("idUsuario", idUsuario);
            editor.putString("contrasenya", contra);
            editor.apply();
            return "200";
        } else {
            return "Error";
        }
    }

    /**
     * Función que se ejecuta al crear la actividad. Se encarga de asignar los listeners a los botones
     * de la actividad y de iniciar los distintos componentes de la pantalla
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();

        TextView errorL = findViewById(R.id.hiddenLoginError);
        errorL.setVisibility(View.GONE);

        TextView acces = findViewById(R.id.bAcceder);
        acces.setOnClickListener(new View.OnClickListener() {
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
                if (response.equals("200")) {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);
                } else {
                    errorL.setVisibility(View.VISIBLE);
                }
            }
        });

        TextView recover = findViewById(R.id.iForgor);
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordRecover.class);
                startActivity(intent);
            }
        });


    }
}