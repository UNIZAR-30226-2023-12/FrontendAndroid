package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Clase que codifica la actividad de registrarse
 */
public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
    }
}