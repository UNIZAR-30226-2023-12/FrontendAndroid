package eina.unizar.melodiaapp;

import static android.util.Log.d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
import android.media.AudioManager;

import eina.unizar.melodiaapp.Modules.GETRequest;

import eina.unizar.melodiaapp.Modules.AudioPlayer;

public class Player extends AppCompatActivity { //TODO idAudio esta hardcodeado? donde esta la id de la canción

    byte[] AudioFile = null;
    AudioPlayer reproductor = new AudioPlayer();
    LayoutInflater vi;
    View volumeBar;
    SeekBar volumeBarSlot;
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
        Bundle extras = getIntent().getExtras();

        TextView hidden = findViewById(R.id.hidden_description);
        TextView author = findViewById(R.id.author_text);

        if ( extras != null){//Colocamos valores extra de demo música

            String body = extras.getString("body");
            String name = extras.getString("name");

            hidden.setText(body);
            author.setText(name);
        }
        else{//Eliminamos la descripción escondida

            hidden.setVisibility(View.GONE);
        }

        // Obtengo el idUsr de SharedPreferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");

        String InputString = "?idAudio=idAudio:2&calidad=False&esCancion=True&idUsr=" + idUsr;

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
        }.execute("GetFicheroSong/", InputString);

        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        volumeBar = vi.inflate(R.layout.volume_bar, null);
        volumeBarSlot = findViewById(R.id.volume_bar_slot);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Configurar el rango de valores del SeekBar
        volumeBarSlot.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBarSlot.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        // Agregar un listener para el cambio de valor del SeekBar
        volumeBarSlot.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Actualizar el volumen del audio
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No se necesita implementación
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No se necesita implementación
            }
        });

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

        ImageView song2Playlist = findViewById(R.id.bAddSong2Playlist);
        song2Playlist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String opcion = "Append";
                Intent intent = new Intent(getApplicationContext(), Playlist.class);
                intent.putExtra("key", opcion);
                intent.putExtra("songId", "idAudio:1");
                startActivity(intent);
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
            volumeBarSlot.setVisibility(View.GONE);
            isVolumeBarPresent = false;
        }else{
            volumeBarSlot.setVisibility(View.VISIBLE);
            isVolumeBarPresent = true;
        }
    }
}