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
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import eina.unizar.melodiaapp.Modules.GETRequest;

import eina.unizar.melodiaapp.Modules.AudioPlayer;

public class Player extends AppCompatActivity {

    byte[] AudioFile = null;
    AudioPlayer reproductor = new AudioPlayer();
    LayoutInflater vi;
    View volumeBar;
    LinearLayout volumeBarSlot;
    Boolean isVolumeBarPresent = false;
    MediaPlayer mediaPlayer = new MediaPlayer();
    ToggleButton play_pause;
    ImageButton stop;

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
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();

        Map<String, Object> data = new HashMap<>();
        data.put("idSong", "idAudio:1");
        data.put("calidadAlta", "False");

        StringBuilder sb = new StringBuilder("");
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                sb.append("&");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        sb.deleteCharAt(sb.length() - 1);

        new GETRequest() {
            @Override
            protected void onPostExecute(JSONObject feed) {
                try {
                    AudioFile = android.util.Base64.decode(feed.getString("fichero"), android.util.Base64.DEFAULT);
                    // create temp file that will hold byte array
                    File tempMp3 = File.createTempFile("playing_sound", "mp3");
                    tempMp3.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(tempMp3);
                    fos.write(AudioFile);
                    fos.close();
                    AudioFile = null;

                    reproductor.play(tempMp3.getAbsolutePath());

                    play_pause = findViewById(R.id.play_pause);
                    play_pause.setChecked(true);
                    play_pause.setOnClickListener(new ToggleButton.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (play_pause.isChecked()) {
                                reproductor.resume();
                            } else {
                                reproductor.pause();
                            }
                        }
                    });

                    stop = findViewById(R.id.stop_track);
                    stop.setOnClickListener(new ImageButton.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reproductor.stop();
                            play_pause.setChecked(false);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute("GetSong/", sb.toString());

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