package eina.unizar.melodiaapp.Modules;

import android.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class AudioPlayer {
    private boolean paused = false;
    private MediaPlayer mediaPlayer = new MediaPlayer();


    /*
     * Constructor de la clase, recibe como parámetro la ruta del archivo de audio que se quiere reproducir.
     */
    public AudioPlayer() {
        this.mediaPlayer = new MediaPlayer();
    }

    /*
     * Método para reproducir el audio, si ya se estaba reproduciendo se para y se reproduce el nuevo.
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

    /*
     * Método para reproducir audio de un array de bytes
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

    /*
     * Método para pausar el audio, si no se estaba reproduciendo no hace nada.
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            paused = true;
        }
    }

    /*
     * Método para reanudar el audio, si no se estaba reproduciendo no hace nada.
     */
    public void resume() {
        if (paused) {
            mediaPlayer.start();
            paused = false;
        }
    }

    /*
     * Método para parar el audio, si no se estaba reproduciendo no hace nada.
     * Parar significa dejar de escuchar el audio, y subir el instante a BBDD
     */
    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer.reset();
        }
    }

    /*
     * Método para saber si se está reproduciendo o no algún audio.
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
