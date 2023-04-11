package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    protected void doRequest() {

        EditText eTemail = findViewById(R.id.inEmail);
        EditText eTcontra = findViewById(R.id.inPasswd);


        String id = eTemail.getText().toString();
        String contra = eTcontra.getText().toString();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("127.0.0.1")
                .appendPath("ValidateUser")
                .appendQueryParameter("usr", id)
                .appendQueryParameter("passwd", contra);
        String urlS = builder.build().toString();
        Toast.makeText(getApplicationContext(), urlS, Toast.LENGTH_SHORT).show();


        //"https://127.0.0.1/ValidateUser/?usr=value11&passwd=value2"

        try {
            //No olvides el try catch
            URL urlLogin = new URL(urlS);

            HttpURLConnection connection = (HttpURLConnection) urlLogin.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            try{//Intentamos obtener respuesta

                //Respuesta de ValidateUser
                InputStream respuesta = new BufferedInputStream(connection.getInputStream());
                //Respuesta de la petción http
                int responseCode = connection.getResponseCode();


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    ///La petición se ha realizado correctamente
                    // Leemos la respuesta de ValidateUser
                    BufferedReader reader = new BufferedReader(new InputStreamReader(respuesta));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    respuesta.close();

                    // Convertimos la respuesta a int
                    int intValue = Integer.parseInt(response.toString());

                    // Do something with the int value
                    Toast.makeText(getApplicationContext(), "Valor de la respuesta http:" + intValue, Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Error en la respuesta http", Toast.LENGTH_SHORT).show();

                }


            } finally {
                connection.disconnect();

            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error en el URL", Toast.LENGTH_SHORT).show();

        }
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
                doRequest();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
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