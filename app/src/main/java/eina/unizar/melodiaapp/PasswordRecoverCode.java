package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskRecoverPasswd;

/**
 * Clase que codifica la actividad para cambio de contraseña
 */
public class PasswordRecoverCode extends AppCompatActivity {
    /**
     * Llama a la función que realiza la petición al servidor para cambiar la contraseña
     * @param email email del usuario
     * @param contrasenya contraseña del usuario
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestChangePasswd(String email, String contrasenya) throws ExecutionException, InterruptedException {

        EditText code = findViewById(R.id.inCodeRecover);
        EditText eTcontra = findViewById(R.id.inPasswd);

        String codeString = code.getText().toString();

        MyTaskRecoverPasswd task = new MyTaskRecoverPasswd();
        String respuesta = task.execute(email, contrasenya, codeString).get();

        return respuesta;
    }

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
        setContentView(R.layout.activity_password_recover_code);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

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
                    String response = "Error";
                    try {
                        response = doRequestChangePasswd(email, pass1);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (response.equals("200")) {
                        Toast.makeText(getApplicationContext(), "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}