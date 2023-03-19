package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONObject;

import eina.unizar.melodiaapp.Modules.GETRequest;

public class Player extends AppCompatActivity {

    LayoutInflater vi;
    View volumeBar;
    LinearLayout volumeBarSlot;
    Boolean isVolumeBarPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();

        new GETRequest().execute("http://192.168.56.1:8080/Melodia/Prueba");

        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        volumeBar = vi.inflate(R.layout.volume_bar, null);
        volumeBarSlot = findViewById(R.id.volume_bar_slot);

        ImageButton equalizer = findViewById(R.id.equalizer);
        ImageButton volume = findViewById(R.id.volume);

        equalizer.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Equalizer.class);
                startActivity(intent);
            }
        });

        volume.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                displayBar();
            }
        });


    }

    protected void displayBar(){

        if(isVolumeBarPresent) {
            volumeBarSlot.removeView(volumeBar);
            isVolumeBarPresent = false;
        }else{
            volumeBarSlot.addView(volumeBar);
            isVolumeBarPresent = true;
        }
    }
}