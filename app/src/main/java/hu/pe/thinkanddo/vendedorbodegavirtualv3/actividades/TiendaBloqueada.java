package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;

public class TiendaBloqueada extends AppCompatActivity {

    private TextView tvCelular,tvMail;
    private String celular,mail;
    private TextView tv_deuda,tv_periodo,tv_cuenta,tv_nomnbre;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_bloqueada);

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Espere por favor...");
        pDialog.setCancelable(false);

        tvCelular = (TextView)findViewById(R.id.tb_celular);
        tvMail = (TextView)findViewById(R.id.tb_email);
        tv_deuda = (TextView)findViewById(R.id.tb_deuda);
        tv_periodo = (TextView)findViewById(R.id.tb_periodo);
        tv_cuenta = (TextView)findViewById(R.id.tb_num_cuenta);
        tv_nomnbre = (TextView)findViewById(R.id.tb_nombre);

        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek( Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek( 4 );
        calendar.setTime(date);
        int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        String URL_DATOS_EMPRESA = "http://52.67.109.198/ServicioWebVendedor/obt_datos_empresa.php";
        obtDatosEmpresa(URL_DATOS_EMPRESA);
        String URL_COMISION = "http://52.67.109.198/ServicioWebVendedor/getDatosComision.php";
        obtDatosComision(URL_COMISION, Preferences.load_id_tienda(this),String.valueOf(numberWeekOfYear-1),String.valueOf(calendar.get(Calendar.YEAR)));

    }

    private void obtDatosEmpresa(String url){

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            celular = response.getString("CELULAR");
                            mail = response.getString("CORREO");

                            tvCelular.setText(celular);
                            tvMail.setText(mail);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void obtDatosComision(String url,String idTienda,String semana, String anio){

        HashMap<String,String> parau = new HashMap<>();
        parau.put("id_tienda", idTienda);
        parau.put("semana", semana);
        parau.put("anio", anio);


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.get("resultado").equals("0")){
                                tv_deuda.setText(R.string.comision_no_disponible);
                                tv_periodo.setText(R.string.periodo_no_disponible);
                                tv_cuenta.setText(R.string.informacion_no_disponible);
                                tv_nomnbre.setText(R.string.informacion_no_disponible);

                                hidepDialog();



                            }else{
                                JSONObject jsonObject = response.getJSONObject("resultado");

                                String deuda = jsonObject.getString("saldo");
                                String periodo = jsonObject.getString("periodo");
                                String cuenta = jsonObject.getString("NUMERO_CUENTA");
                                String nombre = jsonObject.getString("NOMBRE_CUENTA");

                                tv_deuda.setText(String.format("S/ %s", deuda));
                                tv_periodo.setText(periodo);
                                tv_cuenta.setText(cuenta);
                                tv_nomnbre.setText(nombre);

                                hidepDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showpDialog();
    }
}
