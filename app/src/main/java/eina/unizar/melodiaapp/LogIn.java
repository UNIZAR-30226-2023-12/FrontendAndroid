package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import eina.unizar.melodiaapp.Modules.MyTask;

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
 * Clase que codifica la actividad LogIn
 */
public class LogIn extends AppCompatActivity {

    /**
     * Función invocada al crear la pantalla. Inicializa todos los elementos de la interfaz de usuario
     *
     * @param savedInstanceState Si la actividad se ha reinicializado después de ser apagada,
     *                           este Bundle contiene los datos que había aportado recientemente en
     *                           onSaveInstanceState.  <b><i>Nota: En cualquier otro caso su
     *                           valor es null.</i></b>
     *
     */
    protected String doRequest() throws ExecutionException, InterruptedException {

        EditText eTemail = findViewById(R.id.inEmail);
        EditText eTcontra = findViewById(R.id.inPasswd);


        String id = eTemail.getText().toString();
        String contra = eTcontra.getText().toString();

        MyTask task = new MyTask();
        return task.execute(id, contra).get();



        //"https://127.0.0.1/ValidateUser/?usr=value11&passwd=value2"
    }

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