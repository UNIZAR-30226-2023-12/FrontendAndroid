package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
/**
 * Clase que codifica la actividad Ecualizador
 */
public class EqualizerScreen extends AppCompatActivity {
    // Variables privadas para gestionar la ecualización
    private SeekBar mSeekBarBass;
    private SeekBar mSeekBarMid;
    private SeekBar mSeekBarTreble;
    private Equalizer mEqualizer;
    private String audioID;

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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);
        getSupportActionBar().hide();

        // Obtenemos el id de la canción
        Intent intent = getIntent();
        audioID = intent.getStringExtra("audioID");

        // Obtenemos referencias a las seekbars
        mSeekBarBass = findViewById(R.id.seekBar2);
        mSeekBarMid = findViewById(R.id.seekBar3);
        mSeekBarTreble = findViewById(R.id.seekBar4);

        // Creo una instancia de la clase Equalizer
        mEqualizer = new Equalizer(0, Integer.parseInt(audioID));
        mEqualizer.setEnabled(true);

        mSeekBarBass.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mSeekBarMid.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mSeekBarTreble.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        // Establecemos el valor máximo para cada seekbar
        mSeekBarBass.setMax(mEqualizer.getBandLevelRange()[1] - mEqualizer.getBandLevelRange()[0]);
        mSeekBarMid.setMax(mEqualizer.getBandLevelRange()[1] - mEqualizer.getBandLevelRange()[0]);
        mSeekBarTreble.setMax(mEqualizer.getBandLevelRange()[1] - mEqualizer.getBandLevelRange()[0]);
    }

    /**
     * Función invocada al destruir la actividad. Libera los recursos utilizados por el ecualizador
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Implementación de un seekbar
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Obtenemos el índice de la banda correspondiente a la seekbar actual
            int band = -1;
            switch (seekBar.getId()) {
                case R.id.seekBar2:
                    band = 0; // Banda de graves
                    break;
                case R.id.seekBar3:
                    band = 1; // Banda de medios
                    break;
                case R.id.seekBar4:
                    band = 2; // Banda de agudos
                    break;
            }

            // Si el índice es válido, actualizamos el ecualizador
            if (band != -1) {
                updateEqualizer(seekBar.getId(), progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // No hacemos nada
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // No hacemos nada
        }
    };

    /**
     * Función que actualiza el ecualizador
     * @param seekBarId Identificador de la seekbar que ha cambiado
     * @param progress Progreso de la seekbar
     */
    private void updateEqualizer(int seekBarId, int progress) {
        // Obtenemos el índice de la banda correspondiente a la seekbar actual
        int band = -1;
        switch (seekBarId) {
            case R.id.seekBar2:
                band = 0; // Banda de graves
                break;
            case R.id.seekBar3:
                band = 1; // Banda de medios
                break;
            case R.id.seekBar4:
                band = 2; // Banda de agudos
                break;
        }

        // Si el índice es válido, actualizamos el valor de la banda en el ecualizador
        if (band != -1) {
            // Obtenemos el ecualizador del sistema
            mEqualizer = new Equalizer(0, Integer.parseInt(audioID));

            // Obtenemos los límites de la banda y calculamos el valor en dB a partir del progreso de la seekbar
            int minLevel = mEqualizer.getBandLevelRange()[0];
            int maxLevel = mEqualizer.getBandLevelRange()[1];
            int levelRange = maxLevel - minLevel;
            int level = minLevel + levelRange * progress / 100;

            // Actualizamos el valor de la banda correspondiente en el ecualizador
            mEqualizer.setBandLevel((short) band, (short) level);
        }
    }
}