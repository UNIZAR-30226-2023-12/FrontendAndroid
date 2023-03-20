package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Clase que codifica la actividad de lista de reproducción
 */
public class playlist extends AppCompatActivity {

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
        setContentView(R.layout.activity_playlist);
    }
}