package hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Cat_a_actualizar;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Prod_a_actualizar;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;



public class ActualizarStockFrag extends Fragment{

    private Spinner sp_prod;
    private TextView tv_actual,tv_actualizado;
    private EditText et_cambiar_a;
    private String id_prod;
    private RadioButton rbPrecio,rbStock;

    List<Cat_a_actualizar> list;
    List<Prod_a_actualizar> list_pro;

    public ActualizarStockFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_actualizar_stock, container, false);

        Spinner sp_cat = (Spinner) v.findViewById(R.id.sp_elige_cat);
        sp_prod = (Spinner)v.findViewById(R.id.sp_elige_prod);
        tv_actual = (TextView)v.findViewById(R.id.tv_stock_actual);
        tv_actualizado = (TextView)v.findViewById(R.id.tv_dato_actualizado);
        et_cambiar_a = (EditText)v.findViewById(R.id.et_modifica);
        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        rbPrecio = (RadioButton)v.findViewById(R.id.rb_precio);
        rbStock = (RadioButton)v.findViewById(R.id.rb_stock);
        Button actualizar = (Button) v.findViewById(R.id.btn_act);

        sp_cat.setPrompt("Selecciona una categoria");

        cats_disponibles("http://52.67.109.198/ServicioWebCliente/listar_categorias.php?idtienda="+ Preferences.load_id_tienda(getActivity()), sp_cat);

        sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tv_actual.setText("");
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                   sp_prod.setPrompt("Selecciona un producto");

                    for(int i = 0 ; i < list.size();i++){
                        if(list.get(i).getNombre_cat().equals(selectedItemText)){
                            prods_disponibles("http://52.67.109.198/ServicioWebCliente/listar_productos.php?id_tienda=" + Preferences.load_id_tienda(getActivity()) + "&id_cat="+list.get(i).getId_cat() ,sp_prod);

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_prod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                try {
                    for (int i = 0; i < list.size(); i++) {
                        if (list_pro.get(i).getDescrip_larga().equals(selectedItemText)) {
                            id_prod = list_pro.get(i).getId_producto();
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(rbPrecio.isChecked()){
                    getPrecioStock(Preferences.load_id_tienda(getActivity()),id_prod,"PRECIO");


                }else if (rbStock.isChecked()){
                    getPrecioStock(Preferences.load_id_tienda(getActivity()),id_prod,"STOCK");

                }

            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(rbPrecio.isChecked() && et_cambiar_a.getText().length()>0){

                    updateStockPrecio(Preferences.load_id_tienda(getActivity()),id_prod,Double.parseDouble(et_cambiar_a.getText().toString()),"PRECIO");

                }else if (rbStock.isChecked() && et_cambiar_a.getText().length()>0){

                    updateStockPrecio(Preferences.load_id_tienda(getActivity()),id_prod,Double.parseDouble(et_cambiar_a.getText().toString()),"STOCK");

                }

            }
        });


        return v;
    }



    public void cats_disponibles(String url, final Spinner spinner){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        list = new ArrayList<>();

                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object

                                Cat_a_actualizar cat = new Cat_a_actualizar();

                                JSONObject student = response.getJSONObject(i);
                                cat.setId_cat(student.getString("ID_CATEGORIA"));
                                cat.setNombre_cat(student.getString("NOMBRE_CATEGORIA"));

                                list.add(cat) ;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        List<String> lista = new ArrayList<>();

                        lista.add("Selecciona una categoria..");

                        for(int i=0;i<list.size();i++){
                            lista.add(list.get(i).getNombre_cat());
                        }

                        poblar_spinner(lista,spinner);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                    }
                }
        );

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }

    public void prods_disponibles(String url, final Spinner spinner){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        list_pro = new ArrayList<>();

                        try{


                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object

                                Prod_a_actualizar cat = new Prod_a_actualizar();

                                JSONObject student = response.getJSONObject(i);
                                cat.setId_producto(student.getString("ID_PRODUCTO"));
                                cat.setDescrip_larga(student.getString("DESCRIPCION_LARGA"));

                                list_pro.add(cat) ;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        List<String> lista = new ArrayList<>();

                        lista.add("Selecciona un producto..");

                        for(int i=0;i<list_pro.size();i++){
                            lista.add(list_pro.get(i).getDescrip_larga());
                        }

                        poblar_spinner(lista,spinner);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                    }
                }
        );

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }

    private void poblar_spinner(List<String> list,Spinner spinner) {

        ArrayAdapter<String> adapter =new ArrayAdapter<>(getActivity(),R.layout.item_spinner,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateStockPrecio(String id_tienda, String id_producto, final double valorFinal, String stockPrecio) {

        Map<String, String> params = new HashMap<>();
        params.put("id_tienda", id_tienda);
        params.put("id_producto", id_producto);
        params.put("valor_final", valorFinal+"");
        params.put("stock_precio", stockPrecio);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://52.67.109.198/ServicioWebVendedor/updateStockPrecio.php",new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String respuesta = response.getString("resultado");
                            switch (respuesta) {
                                case "exitoso_precio":
                                    tv_actualizado.setText(R.string.actualizacion_correcta_precio);
                                    et_cambiar_a.setText("");
                                    tv_actual.setText(new StringBuilder("S/. " + valorFinal));
                                    break;
                                case "exitoso_stock":
                                    tv_actualizado.setText(R.string.actualizacion_correcta_stock);
                                    et_cambiar_a.setText("");
                                    tv_actual.setText(new StringBuilder(valorFinal + " Unid."));
                                    break;
                                case "fallido":
                                    tv_actualizado.setText(R.string.actualizacion_incorrecta);
                                    et_cambiar_a.setText("");
                                    tv_actual.setText(new StringBuilder(valorFinal + " Unid."));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void getPrecioStock(String id_tienda, String idProducto, final String stockPrecio){

        HashMap<String,String> parau = new HashMap<>();
        parau.put("id_tienda", id_tienda);
        parau.put("id_producto", idProducto);
        parau.put("stock_precio", stockPrecio);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://52.67.109.198/ServicioWebVendedor/getPrecioStock.php",
                new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String precio_stock = response.getString(stockPrecio);

                            if(stockPrecio.equals("PRECIO")){
                                tv_actual.setText(new StringBuilder("S/. "+ precio_stock));
                            }else if(stockPrecio.equals("STOCK")){
                                tv_actual.setText(new StringBuilder(precio_stock+" Unid."));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                         }


                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                    }
                }
        );

        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);

    }


}
