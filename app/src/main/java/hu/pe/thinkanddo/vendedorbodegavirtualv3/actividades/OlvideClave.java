package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;


public class OlvideClave extends AppCompatActivity {

    private TextView tv_mensaje;
    private TextInputEditText et_mail;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_clave);

        et_mail = (TextInputEditText) findViewById(R.id.et_mail_olvide);
        Button btn_enviar = (Button) findViewById(R.id.btn_enviar_mail_olvide);
        tv_mensaje = (TextView)findViewById(R.id.tv_mostrar_mensaje);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_mail.getText().length()>0){
                    String mail = et_mail.getText().toString().trim();

                    auth.sendPasswordResetEmail(mail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        tv_mensaje.setText(new StringBuilder("Te hemos enviado un correo para reestablecer tu contrasena"));
                                        et_mail.setText("");
                                    }
                                }
                            });
                }



            }
        });
    }


}
