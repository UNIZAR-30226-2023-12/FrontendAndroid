package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequest() throws ExecutionException, InterruptedException {

        EditText eTemail = findViewById(R.id.inEmail);
        EditText eTcontra = findViewById(R.id.inPasswd);


        String id = eTemail.getText().toString();
        String contra = eTcontra.getText().toString();

        MyTaskLogin task = new MyTaskLogin();
        return task.execute(id, contra).get();
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
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();

        TextView acces = findViewById(R.id.bAcceder);
        acces.setOnClickListener(new View.OnClickListener(){
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
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error en el login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView recover = findViewById(R.id.iForgor);
        recover.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordRecover.class);
                startActivity(intent);
            }
        });
    }
}