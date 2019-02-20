package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;

public class EnviarNotificacionActivity extends AppCompatActivity {

    private Button btnEnviar;
    private EditText etMensaje;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_notificacion);

        btnEnviar = findViewById(R.id.btnEnviarNotificacion);
        etMensaje = findViewById(R.id.etMensajeNotificacion);
        progressBar = findViewById(R.id.pgEnviarNotificacionMasiva);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etMensaje.getText().toString().isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    btnEnviar.setEnabled(false);
                    enviarNotificacion(etMensaje.getText().toString());
                }else{
                    Toast.makeText(EnviarNotificacionActivity.this, "no puedes enviar un mensaje vacio", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void enviarNotificacion(String s) {

        HashMap<String,String> parau = new HashMap<>();
        parau.put("mensaje", s);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,"http://52.67.109.198/ServicioWebCliente/enviar_notificacion_masiva.php",new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.get("resultado").equals("1")){
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(EnviarNotificacionActivity.this, "Envio correcto", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(EnviarNotificacionActivity.this, "No pudo enviarse correctamente", Toast.LENGTH_SHORT).show();
                                btnEnviar.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                btnEnviar.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
