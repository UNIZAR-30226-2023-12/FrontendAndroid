package eina.unizar.melodiaapp;

import static android.util.Log.d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.media.AudioManager;

import eina.unizar.melodiaapp.Modules.GETRequest;
import eina.unizar.melodiaapp.Modules.MyTaskAskLink;
import eina.unizar.melodiaapp.Modules.MyTaskDeleteSongLista;
import eina.unizar.melodiaapp.Modules.MyTaskGetRating;
import eina.unizar.melodiaapp.Modules.MyTaskSetRating;
import eina.unizar.melodiaapp.Modules.MyTaskSetSecHeared;

import eina.unizar.melodiaapp.Modules.AudioPlayer;
import eina.unizar.melodiaapp.Modules.MyTaskSetSongLista;

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
    private String idAudioActual = "idAudio:2";
    private String idsCancionesPlaylist = "";
    private String nombresCanciones = "";
    private int posicionCancionActual = 0;
    private String[] idsCancionesPlaylistArray;

    protected String doRequestSetLastSecondHeared(String seconds) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");

        MyTaskSetSecHeared task = new MyTaskSetSecHeared();
        return task.execute(idUsr, passwd, idAudioActual, seconds).get();
    }

    protected String doRequestSetRating(String rating) throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");

        MyTaskSetRating task = new MyTaskSetRating();
        return task.execute(idUsr, passwd, idAudioActual, rating).get();
    }

    protected String doRequestGetRating() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");

        MyTaskGetRating task = new MyTaskGetRating();
        String[] respuesta = task.execute(idUsr, passwd, idAudioActual).get().split(",");

        if (respuesta[0].equals("200")) {
            return respuesta[1];
        } else {
            return "Error";
        }
    }

    protected String doRequestAskLink() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");

        MyTaskAskLink task = new MyTaskAskLink();
        String result = task.execute(idUsr, passwd, idAudioActual).get();
        String response[] = result.split(",");

        if (response[0].equals("200")) {
            return response[1];
        } else {
            return "Error";
        }
    }

    // Para añadir canción a la lista favoritos
    protected String doRequestAddFav() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");
        String idLista = preferences.getString("idListaFavoritos", "");

        MyTaskSetSongLista task = new MyTaskSetSongLista();
        String result = task.execute(idUsr, passwd, idLista, idAudioActual).get();
        String[] response = result.split(",");

        if (response[0].equals("200")) {
            return "200";
        } else {
            return "Error";
        }
    }

    // Para borrar canción de favoritos
    protected String doRequestDeleteFav() throws ExecutionException, InterruptedException {
        // Obtengo usuario y contrasenya de shared preferences
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String idUsr = preferences.getString("idUsuario", "");
        String passwd = preferences.getString("contrasenya", "");
        String idLista = preferences.getString("idListaFavoritos", "");

        MyTaskDeleteSongLista task = new MyTaskDeleteSongLista();
        String result = task.execute(idUsr, passwd, idLista, idAudioActual).get();
        String[] response = result.split(",");

        if (response[0].equals("200")) {
            return "200";
        } else {
            return "Error";
        }
    }

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
        hidden.setVisibility(View.GONE);

        if ( extras != null){//Colocamos valores extra de demo música
            String tipoRep = extras.getString("tipoRep");
            if (tipoRep.equals("playlistNormal")) {
                String modoReproduccion = extras.getString("playingMode");
                // Obtengo de shared preferences los ids y nombres de canciones
                SharedPreferences preferencesPL = getSharedPreferences("playlistActual", MODE_PRIVATE);
                String idsCancionesPlaylistNoParser = preferencesPL.getString("idsCancionesPlaylist", "");
                idAudioActual = preferencesPL.getString("idCancionActual", "");

                String[] idsCancionesPlaylistParser = idsCancionesPlaylistNoParser.split(",");
                idsCancionesPlaylistArray = new String[idsCancionesPlaylistParser.length - 1];
                System.arraycopy(idsCancionesPlaylistParser, 1, idsCancionesPlaylistArray, 0, idsCancionesPlaylistParser.length - 1);

                if (modoReproduccion.equals("repeat")) {
                    // Obtengo el idUsr de SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                    String idUsr = preferences.getString("idUsuario", "");

                    String InputString = "?idAudio=" + idAudioActual + "&calidad=False&esCancion=True&idUsr=" + idUsr;

                    new GETRequest() {
                        @Override
                        protected void onPostExecute(JSONObject feed) {
                            try {
                                AudioFile = Base64.decode(feed.getString("fichero"), Base64.DEFAULT);
                                // create temp file that will hold byte array
                                File tempMp3 = File.createTempFile("playing_sound", "mp3");
                                tempMp3.deleteOnExit();
                                FileOutputStream fos = new FileOutputStream(tempMp3);
                                fos.write(AudioFile);
                                fos.close();
                                AudioFile = null;

                                mediaPlayer.setDataSource(tempMp3.getAbsolutePath());
                                mediaPlayer.prepare();
                                mediaPlayer.start();

                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mediaPlayer.seekTo(0); // Reinicia la reproducción al inicio
                                        mediaPlayer.start(); // Inicia la reproducción nuevamente
                                    }
                                });

                                play_pause = findViewById(R.id.play_pause);
                                play_pause.setChecked(true);
                                play_pause.setOnClickListener(new ToggleButton.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (play_pause.isChecked()) {
                                            mediaPlayer.start();
                                        } else {
                                            mediaPlayer.pause();
                                        }
                                    }
                                });

                                stop = findViewById(R.id.stop_track);
                                stop.setOnClickListener(new ImageButton.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mediaPlayer.stop();
                                        int currentTime = mediaPlayer.getCurrentPosition();
                                        try {
                                            doRequestSetLastSecondHeared(String.valueOf(currentTime));
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
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
                }
                else if (modoReproduccion.equals("linear")) {
                    // Obtengo el idUsr de SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                    String idUsr = preferences.getString("idUsuario", "");
                    posicionCancionActual = 0;
                    for (int i = 0; i < idsCancionesPlaylistArray.length; i++) {
                        if (idsCancionesPlaylistArray[i].equals(idAudioActual)) {
                            posicionCancionActual = i;
                            break;
                        }
                    }
                    ReproducirAudio(idUsr);
                }
                else if (modoReproduccion.equals("random")) {
                    // Obtengo el idUsr de SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                    String idUsr = preferences.getString("idUsuario", "");
                    posicionCancionActual = 0;

                    // Desordeno el array de ids de canciones
                    List<String> ids = new ArrayList<>(Arrays.asList(idsCancionesPlaylistArray));
                    Collections.shuffle(ids);
                    idsCancionesPlaylistArray = ids.toArray(new String[ids.size()]);

                    for (int i = 0; i < idsCancionesPlaylistArray.length; i++) {
                        if (idsCancionesPlaylistArray[i].equals(idAudioActual)) {
                            posicionCancionActual = i;
                            break;
                        }
                    }
                    ReproducirAudio(idUsr);
                }
            }
        }


        // Seteo el rating
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        try {
            String rating = doRequestGetRating();
            if (!rating.equals("Error")) {
                ratingBar.setRating(Float.parseFloat(rating));
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }



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
                // Envío en los extras el id de la canción en el MediaPlayer
                Intent intent = new Intent(getApplicationContext(), EqualizerScreen.class);
                String sessionId = String.valueOf(mediaPlayer.getAudioSessionId());
                intent.putExtra("audioID", sessionId);
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
                intent.putExtra("songId", "idAudio:2");
                startActivity(intent);
            }
        });

        // OnClick para el botón de compartir
        ImageButton share = findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = "Error";
                try {
                    response = doRequestAskLink();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!response.equals("Error")) {
                    // Copio el link en el portapapeles del dispositivo
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("link", response);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), "Link copiado al portapapeles", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error al obtener el link", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // OnClick para la ratingbar
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            // Se ejecuta cuando se cambia el valor de la ratingbar
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String ratingValue = String.valueOf(ratingBar.getRating());
                TextView ratingValueTextFinal = findViewById(R.id.ratingCuantity);
                ratingValueTextFinal.setText(ratingValue);
                try {
                    doRequestSetRating(ratingValue);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Setear la valoración numérica
        TextView ratingValue = findViewById(R.id.ratingCuantity);
        ratingValue.setText(String.valueOf(ratingBar.getRating()));

        // OnClick para el botón de añadir a favoritos
        ToggleButton fav = findViewById(R.id.favorite_button);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activo el botón de favoritos
                ToggleButton favBtn = v.findViewById(R.id.favorite_button);
                if (!favBtn.isChecked()) {
                    //favBtn.setChecked(false);
                    try {
                        doRequestDeleteFav();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    //favBtn.setChecked(true);
                    try {
                        doRequestAddFav();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

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

    private void ReproducirAudio(String idUsr) {
        String InputString = "?idAudio=" + idsCancionesPlaylistArray[posicionCancionActual] + "&calidad=False&esCancion=True&idUsr=" + idUsr;

        new GETRequest() {
            @Override
            protected void onPostExecute(JSONObject feed) {
                try {
                    AudioFile = Base64.decode(feed.getString("fichero"), Base64.DEFAULT);
                    // create temp file that will hold byte array
                    File tempMp3 = File.createTempFile("playing_sound", "mp3");
                    tempMp3.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(tempMp3);
                    fos.write(AudioFile);
                    fos.close();
                    AudioFile = null;

                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    mediaPlayer = new MediaPlayer();


                    mediaPlayer.setDataSource(tempMp3.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            posicionCancionActual = (posicionCancionActual + 1) % idsCancionesPlaylistArray.length;
                            mediaPlayer.release();
                            mediaPlayer = null;
                            mediaPlayer = new MediaPlayer();
                            ReproducirAudio(idUsr);
                        }
                    });

                    play_pause = findViewById(R.id.play_pause);
                    play_pause.setChecked(true);
                    play_pause.setOnClickListener(new ToggleButton.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (play_pause.isChecked()) {
                                mediaPlayer.start();
                            } else {
                                mediaPlayer.pause();
                            }
                        }
                    });

                    stop = findViewById(R.id.stop_track);
                    stop.setOnClickListener(new ImageButton.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mediaPlayer.stop();
                            int currentTime = mediaPlayer.getCurrentPosition();
                            try {
                                doRequestSetLastSecondHeared(String.valueOf(currentTime));
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
    }
}