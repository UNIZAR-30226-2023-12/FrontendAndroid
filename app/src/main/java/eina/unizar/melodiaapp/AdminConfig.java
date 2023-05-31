package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Clase que gestiona la actividad de configuración de administrador
 */
public class AdminConfig extends AppCompatActivity {
    /**
     * Método que se ejecuta al crear la actividad
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_config);
        String mode = "Admin";

        TextView reproducciones = findViewById(R.id.bViews);
        reproducciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//TODO reproducciones
                Intent intent = new Intent(getApplicationContext(), AllSeconds.class);
                intent.putExtra("key",mode);
                startActivity(intent);
            }
        });
    }


}