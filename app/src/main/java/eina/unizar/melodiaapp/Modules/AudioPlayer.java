package eina.unizar.melodiaapp.Modules;

import android.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase que implementa un reproductor de audio.
 */
public class AudioPlayer {
    private boolean paused = false;
    private MediaPlayer mediaPlayer = new MediaPlayer();


    /**
     * Constructor de la clase AudioPlayer.
     */
    public AudioPlayer() {
        this.mediaPlayer = new MediaPlayer();
    }

    /**
     * Método para reproducir un audio.
     * @param path
     */
    public void play(String path) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer.reset();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para reproducir un audio.
     * @param audioFile
     */
    public void play(byte[] audioFile){
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("playing_sound", "mp3");
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(audioFile);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    /**
     * Método para pausar el audio, si no se estaba reproduciendo no hace nada.
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            paused = true;
        }
    }

    /**
     * Método para reanudar el audio, si no se estaba reproduciendo no hace nada.
     */
    public void resume() {
        if (paused) {
            mediaPlayer.start();
            paused = false;
        }
    }

    /**
     * Método para parar el audio, si no se estaba reproduciendo no hace nada.
     */
    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            //mediaPlayer.release();
            mediaPlayer.reset();
        }
    }

    /**
     * Método para saber si se está reproduciendo un audio.
     * @return true si se está reproduciendo, false en caso contrario.
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
