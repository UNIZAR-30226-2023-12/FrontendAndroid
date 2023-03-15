package eina.unizar.melodiaapp.Modules;

import android.media.MediaPlayer;


public class AudioPlayer {
    private boolean paused = false;
    private String path;
    private MediaPlayer mediaPlayer;


    /*
     * Constructor de la clase, recibe como parámetro la ruta del archivo de audio que se quiere reproducir.
     */
    public AudioPlayer(String path) {
        this.path = path;
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
