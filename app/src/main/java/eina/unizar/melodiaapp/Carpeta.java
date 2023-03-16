package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Carpeta extends AppCompatActivity {

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