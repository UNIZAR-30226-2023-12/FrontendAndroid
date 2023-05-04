package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Reproductions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductions);

        Bundle extras = getIntent().getExtras();
        String mode = extras.getString("key");

        if (extras != null) {

            if (mode == "admin") {//Modo admin
                TextView title = findViewById(R.id.repName);
                title.setText("@string/Reproduction_titleAdmin");
            }
            else{
                System.out.println("Error: Bad mode\n");
            }
        }
    }
}