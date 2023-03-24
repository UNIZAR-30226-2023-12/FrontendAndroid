package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Clase que codifica la actividad Carpeta
 */
public class Carpeta extends AppCompatActivity {

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
        setContentView(R.layout.activity_carpeta);

        TextView bCarpetaEjemplo = findViewById(R.id.carpetaEjemplo);
        bCarpetaEjemplo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), playlist.class);
                startActivity(intent);
            }
        });

        TextView bAddCarpeta = findViewById(R.id.bMasCarpetas);
        bAddCarpeta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateCarpeta.class);
                startActivity(intent);
            }
        });
    }


}