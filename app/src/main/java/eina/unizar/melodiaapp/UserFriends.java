package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import eina.unizar.melodiaapp.Modules.MyTaskAskFriends;
import eina.unizar.melodiaapp.Modules.MyTaskAskNameFriends;
import eina.unizar.melodiaapp.Modules.MyTaskGetSubsribeArtist;

public class UserFriends extends AppCompatActivity {
    /**
     * Realiza la request correspondiente para obtener a los amigos y devuelve sus ids en caso
     * de que la request haya sido correcta y sino devuelve un string de error
     * @return
     * @throws Exception
     */
    protected String[] doRequestAskFriends() throws Exception {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskAskFriends task = new MyTaskAskFriends();
        String respuesta = task.execute(idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    protected String doRequesAskNameFriend(String idUsrAmigo) throws Exception {
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");
        MyTaskAskNameFriends task = new MyTaskAskNameFriends();
        String respuesta = task.execute(idUsuario, contrasenya, idUsrAmigo).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response[1];
        }
        else {
            return "Error";
        }
    }

    protected String[] doRequestGetSubsribeArtist() throws Exception {
        // Obtengo usuario y contraseña
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsuario = preferences.getString("idUsuario", "");
        String contrasenya = preferences.getString("contrasenya", "");

        MyTaskGetSubsribeArtist task = new MyTaskGetSubsribeArtist();
        String respuesta = task.execute(idUsuario, contrasenya).get();
        String response[] = respuesta.split(",");

        if (response[0].equals("200")) {
            return response;
        }
        else {
            return new String[]{"Error"};
        }
    }

    /**
     * Inicializa la pantalla y realiza la request correspondiente para obtener a los amigos
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends);

        // Hago la request para obtener los ids de mis amigos
        String[] idsAmigos = new String[]{"Error"};
        try {
            idsAmigos = doRequestAskFriends();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] idsArtistas = new String[]{"Error"};
        try {
            idsArtistas = doRequestGetSubsribeArtist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Hago la request para obtener los nombres de mis amigos
        if (idsAmigos[0].equals("200")) {
            String nombresAmigos[] = new String[idsAmigos.length - 1];
            for (int i = 1; i < idsAmigos.length; i++) {
                try {
                    nombresAmigos[i - 1] = doRequesAskNameFriend(idsAmigos[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Una vez obtenidos los nombres, los muestro en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresAmigos);
            ListView listView = findViewById(R.id.FriendsListView);
            listView.setAdapter(adapter);
        }
        else {
            Toast.makeText(getApplicationContext(), "Error al obtener los amigos", Toast.LENGTH_SHORT).show();
        }

        if (idsArtistas[0].equals("200")) {
            String nombresArtistas[] = new String[idsArtistas.length - 1];
            for (int i = 1; i < idsArtistas.length; i++) {
                try {
                    nombresArtistas[i - 1] = doRequesAskNameFriend(idsAmigos[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Una vez obtenidos los nombres, los muestro en la interfaz
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancion_item, R.id.listTextView, nombresArtistas);
            ListView listView = findViewById(R.id.FriendsListView);
            listView.setAdapter(adapter);
        }
        else {
            Toast.makeText(getApplicationContext(), "Error al obtener los artistas suscritos", Toast.LENGTH_SHORT).show();
        }

        // OnClick para el botón de menú
        ImageView menu = findViewById(R.id.menuIconFriends);
        menu.setOnClickListener(v -> {
            Intent intent = new Intent(UserFriends.this, Menu.class);
            startActivity(intent);
        });

        // OnClick para el botón de notificaciones
        ImageView notificaciones = findViewById(R.id.bellIconFriendsotifications);
        notificaciones.setOnClickListener(v -> {
            Intent intent = new Intent(UserFriends.this, Notifications.class);
            startActivity(intent);
        });

        // OnClick para el botón del perfil
        ImageView perfil = findViewById(R.id.profileIconFriends);
        perfil.setOnClickListener(v -> {
            Intent intent = new Intent(UserFriends.this, Profile.class);
            startActivity(intent);
        });
    }
}