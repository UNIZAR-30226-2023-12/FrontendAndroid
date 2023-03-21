package eina.unizar.melodiaapp;

import static android.util.Log.d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import eina.unizar.melodiaapp.Modules.AudioPlayer;
import eina.unizar.melodiaapp.Modules.GETRequest;

public class Player extends AppCompatActivity {

    byte[] AudioFile = null;
    AudioPlayer reproductor = new AudioPlayer();
    LayoutInflater vi;
    View volumeBar;
    LinearLayout volumeBarSlot;
    Boolean isVolumeBarPresent = false;
    MediaPlayer mediaPlayer = new MediaPlayer();

    /**
     * Función invocada al crear la pantalla. Inicializa todos los elementos de la interfaz de usuario
     *
     * @param savedInstanceState Si la actividad se ha reinicializado después de ser apagada,
     *                           este Bundle contiene los datos que había aportado recientemente en
     *                           {@link #onSaveInstanceState}.  <b><i>Nota: En cualquier otro caso su
     *                           valor es null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();

        new GETRequest(){
            @Override
            protected void onPostExecute(JSONObject feed){
                try {
                    AudioFile =  android.util.Base64.decode(feed.getString("fichero"), android.util.Base64.DEFAULT);
                    // create temp file that will hold byte array
                    File tempMp3 = File.createTempFile("playing_sound", "mp3");
                    tempMp3.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(tempMp3);
                    fos.write(AudioFile);
                    fos.close();
                    AudioFile = null;

                    reproductor.play(tempMp3.getAbsolutePath());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute("http://192.168.56.1:8080/Melodia/Prueba");

        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        volumeBar = vi.inflate(R.layout.volume_bar, null);
        volumeBarSlot = findViewById(R.id.volume_bar_slot);

        ImageButton equalizer = findViewById(R.id.equalizer);
        ImageButton volume = findViewById(R.id.volume);

        equalizer.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Equalizer.class);
                startActivity(intent);
            }
        });

        volume.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                displayBar();
            }
        });


    }

    /**
     * Función invocada al pulsar el botón de volumen.
     * Si la barra de volumen se estaba mostrando, la oculta.
     * Si estaba oculta, la muestra.
     */
    protected void displayBar(){

        if(isVolumeBarPresent) {
            volumeBarSlot.removeView(volumeBar);
            isVolumeBarPresent = false;
        }else{
            volumeBarSlot.addView(volumeBar);
            isVolumeBarPresent = true;
        }
    }
}