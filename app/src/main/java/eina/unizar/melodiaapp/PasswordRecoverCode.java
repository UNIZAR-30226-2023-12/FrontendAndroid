package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase que codifica la actividad para cambio de contraseña
 */
public class PasswordRecoverCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover_code);

        TextView bCheckPasswd = findViewById(R.id.bNewPasswordConfirm);
        bCheckPasswd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText password1 = findViewById(R.id.inNewPassword);
                EditText password2 = findViewById(R.id.inNewPassword);

                String pass1 = password1.getText().toString();
                String pass2 = password2.getText().toString();

                if (pass1.length() < 7 || pass2.length() < 7){
                    Toast.makeText(getApplicationContext(), "Las contraseñas deben tener almenos 7 letras o números", Toast.LENGTH_SHORT).show();
                }
                else if(pass1.equals(pass2)){
                    Intent intent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}