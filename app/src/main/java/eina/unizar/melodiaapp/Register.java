package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskRegister;

/**
 * Clase que codifica la actividad de registrarse
 */
public class Register extends AppCompatActivity {
    /**
     * Función que se invoca para realizar la petición al servidor
     * Coge los valores de los campos de texto y los pasa a la función que realiza la petición
     * Devuelve el resultado de la petición
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequest() throws ExecutionException, InterruptedException {

        EditText eTuser = findViewById(R.id.inUserName);
        EditText eTemail = findViewById(R.id.inEmailReg);
        EditText eTcontra = findViewById(R.id.inPasswdReg);

        // Convierto los valores a String
        String user = eTuser.getText().toString();
        String email = eTemail.getText().toString();
        String contra = eTcontra.getText().toString();

        MyTaskRegister task = new MyTaskRegister();
        return task.execute(user, email, contra).get();
    }

    /**
     * Función invocada al crear la pantalla. Inicializa todos los elementos de la interfaz de usuario
     * Crea listeners para los distintos botones
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // Añado el listener para el botón de registrarse
        TextView RegisterBtn = findViewById(R.id.bRegisterConfirm);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}