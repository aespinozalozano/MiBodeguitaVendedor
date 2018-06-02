package hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ComisionFrag extends Fragment {


    private TextView tv_deuda,tv_periodo,tv_cuenta,tv_nomnbre;
    private ProgressDialog pDialog;


    public ComisionFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_comision, container, false);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage("Espere por favor...");
        pDialog.setCancelable(false);

        tv_deuda = (TextView)v.findViewById(R.id.fc_deuda);
        tv_periodo = (TextView)v.findViewById(R.id.fc_perido);
        tv_cuenta = (TextView)v.findViewById(R.id.fc_num_cuenta);
        tv_nomnbre = (TextView)v.findViewById(R.id.fc_nombre);

        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek( Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek( 4 );
        calendar.setTime(date);
        int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);


        String URL_COMISION = "http://52.67.109.198/ServicioWebVendedor/getDatosComision.php";
        obtDatosComision(URL_COMISION, Preferences.load_id_tienda(getActivity()),String.valueOf(numberWeekOfYear-1),String.valueOf(calendar.get(Calendar.YEAR)));

        return  v;
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

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

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
    public void onResume() {
        super.onResume();
        showpDialog();
    }
}
