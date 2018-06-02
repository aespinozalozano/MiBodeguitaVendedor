package hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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



public class ContactoFrag extends Fragment {

    private static final String ARG_PARAM1 = "id_tienda";
    private ProgressDialog pDialog;
    private TextView tvFijo,tvCelular,tvMail,tvDireccion;
    private EditText etComen;
    private String fijo,celular,mail,direccion;
    private final String URL_ENVIA_COMEN = "http://52.67.109.198/ServicioWebVendedor/guardar_comentario_vendedor.php";

    public ContactoFrag() {
        // Required empty public constructor
    }


    public static ContactoFrag newInstance(String param1) {
        ContactoFrag fragment = new ContactoFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacto, container, false);

        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage("Espere por favor...");
        pDialog.setCancelable(false);

        tvFijo = (TextView)v.findViewById(R.id.tv_contacto_fijo);
        tvCelular = (TextView)v.findViewById(R.id.tv_contacto_telefono);
        tvMail = (TextView)v.findViewById(R.id.tv_contacto_email);
        tvDireccion = (TextView)v.findViewById(R.id.tv_direccion);
        etComen = (EditText)v.findViewById(R.id.et_envia_comen);
        Button btnEnvComen = (Button) v.findViewById(R.id.btn_envia_comen);
        ImageView iv_fijo = (ImageView) v.findViewById(R.id.iv_fijo);
        ImageView iv_celular = (ImageView) v.findViewById(R.id.iv_celular);
        ImageView iv_mail = (ImageView) v.findViewById(R.id.iv_mail);

        String URL_DATOS_EMPRESA = "http://52.67.109.198/ServicioWebVendedor/obt_datos_empresa.php";
        obtDatosEmpresa(URL_DATOS_EMPRESA);



        iv_fijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + fijo));
                startActivity(i);
            }
        });

        iv_celular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + celular));
                startActivity(i);
            }
        });

        iv_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mail, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Escribe aqui el motivo de tu correo");
                startActivity(emailIntent);
            }
        });

        btnEnvComen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String idTienda = getArguments().getString(ARG_PARAM1);
                String comentario = etComen.getText().toString();
                enviar_comentario(URL_ENVIA_COMEN,idTienda,comentario);
            }
        });

        return v;
    }


    private void obtDatosEmpresa(String url){

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        fijo = response.getString("FIJO");
                        celular = response.getString("CELULAR");
                        mail = response.getString("CORREO");
                        direccion = response.getString("DIRECCION_OFICINA");

                            tvFijo.setText(fijo);
                            tvCelular.setText(celular);
                            tvMail.setText(mail);
                            tvDireccion.setText(direccion);
                            hidepDialog();

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

    private void enviar_comentario(String url,String idTienda,String comentario){

        final String[] resultad = {""};

        HashMap<String,String> parau = new HashMap<>();
        parau.put("id_tienda", idTienda);
        parau.put("comentario", comentario);

        JsonObjectRequest reque = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            resultad[0] = response.getString("resultado");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getContext(),resultad[0], Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(reque);

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
