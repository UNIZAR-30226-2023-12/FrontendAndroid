package eina.unizar.melodiaapp.Modules;

import android.content.Context;
import android.media.AudioManager;

public class VolumeControl {
    private AudioManager audioManager;

    public VolumeControl(Context context){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
    public int getVolumen(){
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    public turnUpVolume(){
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
    }
    public turnDownVolume(){
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
    }
}

/*
 * Botones para subir y bajar el volumen, en el menu de reproduccion, al pulsar el boton de volumen
 * se debería sacar un submenu con el volumen actual y al desplazar subir o bajar el volumen.
 * Este código es el que se puede copiar y pegar
 */
Button subirVolumenBtn = findViewById(R.id.subir_volumen_btn);
subirVolumenBtn.setOnClickListener(new View.onClickListener(){
    @Override
    public void onClick(View v){
        VolumeControl volumeControl = new VolumenControl(getApplicationContext());
        volumenControl.turnUpVolume();
        }
});

Button bajarVolumenBtn = findViewById(R.id.bajar_volumen_btn);
bajarVolumenBtn.setOnClickListener(new View.onClickListener(){
@Override
public void onClick(View v){
        VolumeControl volumeControl = new VolumenControl(getApplicationContext());
        volumenControl.turnDownVolume();
        }
});
