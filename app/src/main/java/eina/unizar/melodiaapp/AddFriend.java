package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskAskAddFriend;
import eina.unizar.melodiaapp.Modules.MyTaskAskFriends;
import eina.unizar.melodiaapp.Modules.MyTaskAskGlobalSearchResults;
import eina.unizar.melodiaapp.Modules.MyTaskChangeNamePlaylist;

/**
 * Clase que gestiona la actividad de añadir amigos
 */
public class AddFriend extends AppCompatActivity {
    /**
     * Método que realiza la petición de añadir un amigo
     * @param idAmigo id del amigo a añadir
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestAskFriend(String idAmigo) throws ExecutionException, InterruptedException {

        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        // Hago la petición
        MyTaskAskAddFriend task = new MyTaskAskAddFriend();

        return task.execute(idUsuario, contrasenya, idAmigo).get();
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
        setContentView(R.layout.activity_add_friend);

        ImageView notificacionesBTN = findViewById(R.id.bellIconAAdd);
        notificacionesBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notifications.class);
                startActivity(intent);
            }
        });

        ImageView profileIcon = findViewById(R.id.profileIconAAdd);
        profileIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        ImageView homeIcon = findViewById(R.id.menuIconAAdd);
        homeIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        TextView anyadir = findViewById(R.id.bAddFren);
        anyadir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView tvIdAmigo = findViewById(R.id.friendSearch);
                String idAmigo = tvIdAmigo.getText().toString();
                String respuesta = "error";
                try {
                    respuesta = doRequestAskFriend(idAmigo);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), respuesta, Toast.LENGTH_SHORT).show();
            }
        });
    }


}