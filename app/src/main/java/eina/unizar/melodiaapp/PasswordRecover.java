package eina.unizar.melodiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import eina.unizar.melodiaapp.Modules.MyTaskSendEmail;

/**
 * Clase que codifica la actividad de recuperación de contraseña
 */
public class PasswordRecover extends AppCompatActivity {

    /**
     * Llama a la función que realiza la petición al servidor para enviar un correo
     * @return respuesta del servidor
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected String doRequestSendEmail() throws ExecutionException, InterruptedException {

        EditText eTemail = findViewById(R.id.inEmailRecover);

        String email = eTemail.getText().toString();

        MyTaskSendEmail task = new MyTaskSendEmail();
        String respuesta = task.execute(email).get();

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
        setContentView(R.layout.activity_password_recover);

        TextView bCode = findViewById(R.id.bSendCodeConfirm);
        bCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String response = "Error";
                try {
                    response = doRequestSendEmail();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (response.equals("200")) {
                    Intent intent = new Intent(getApplicationContext(), PasswordRecoverCode.class);
                    intent.putExtra("email", ((EditText) findViewById(R.id.inEmailRecover)).getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error al enviar el correo, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}