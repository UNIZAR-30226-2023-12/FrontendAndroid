package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskGetRating;
import eina.unizar.melodiaapp.Modules.MyTaskGetTime;

public class AllSeconds extends AppCompatActivity {

    protected String doRequestGetTime() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");

        MyTaskGetTime task = new MyTaskGetTime();
        String[] respuesta = task.execute(idUsr, passwd).get().split(",");

        if (respuesta[0].equals("200")) {
            return respuesta[1];
        } else {
            return "Error";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_seconds);

        String response = "Error";

        try {
            response = doRequestGetTime();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response.equals("Error")){
            System.out.println("Error, no se puede obtener los segundos");
        }
        else{
            TextView hoy = findViewById(R.id.textday);
            hoy.setText(response);
        }







        ImageView profileIcon = findViewById(R.id.profileIconallSeconds);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView bellIcon = findViewById(R.id.bellIconallSeconds);
        bellIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        // Añado el listener para el botón de home
        ImageView homebtn = findViewById(R.id.imageHomeAallSeconds);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });


    }

    }
}