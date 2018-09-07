package hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores.ProductosAdapter;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.interfaces.ItemClickListener_listar_productos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Lista_productos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;


public class Frag_buscar_productos extends Fragment {

    private RecyclerView rv_buscar;
    private ProductosAdapter adapter;
    private List<Lista_productos> item;
    private ProgressBar pb;
    private ImageView imageView;
    private TextView textView;

    private static final String ARG_PARAM1 = "query";


    private final ItemClickListener_listar_productos f= new ItemClickListener_listar_productos() {
        @Override
        public void onClick(View view, int position) {



            String[] producto = adapter.getProducto(position);
            ActualizarStockFrag2 mifragment = ActualizarStockFrag2.newInstance(producto[0],producto[1],producto[2],producto[3],producto[4]);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_contenedor,mifragment).commit();

        }
    };


    public Frag_buscar_productos() {
        // Required empty public constructor

    }

    public static Frag_buscar_productos newInstance(String param1) {
        Frag_buscar_productos fragment = new Frag_buscar_productos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_frag_buscar_productos, container, false);
        pb = v.findViewById(R.id.progessbar2);
        imageView = v.findViewById(R.id.imageView6);
        textView = v.findViewById(R.id.textView2);

        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        rv_buscar = v.findViewById(R.id.rv_buscar_prod);
        rv_buscar.setHasFixedSize(true);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String query = getArguments().getString("query");
        if (query != null) buscarProductos(Preferences.load_id_tienda(getActivity()), query);
    }


    private void buscarProductos(final String idTienda, final String query){

        HashMap<String, String> parat = new HashMap<>();
        parat.put("id_tienda", idTienda);
        parat.put("query", query.toUpperCase());

        JsonObjectRequest prodRequest = new JsonObjectRequest(Request.Method.POST, "http://52.67.109.198/ServicioWebCliente/buscar_productos.php",new JSONObject(parat),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getJSONArray("resultado").length()==0){

                                //Toast.makeText(getActivity(), "Lo sentimos, no tenemos ese producto", Toast.LENGTH_SHORT).show();
                                imageView.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                /*Fragment f = new SinProductoFragment();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.remove(getActivity().getSupportFragmentManager().findFragmentByTag("frag_buscar_prod"));
                                ft.add(R.id.fl_contenedor,f,"no hay");
                                ft.commit();
                                getActivity().onBackPressed();
                                getActivity().getSupportFragmentManager().beginTransaction().add(new SinProductoFragment(),"sin_prod").commit();*/

                            }else{
                                JSONArray ja = response.getJSONArray("resultado");
                                item = new ArrayList<>();
                                item = parseJsona(ja);
                                adapter = new ProductosAdapter(getActivity(),item);
                                LinearLayoutManager lim = new LinearLayoutManager(getActivity());
                                lim.setOrientation(LinearLayoutManager.VERTICAL);
                                rv_buscar.setLayoutManager(lim);
                                rv_buscar.setAdapter(adapter);
                                pb.setVisibility(View.INVISIBLE);
                                adapter.setClickListener(f);
                                adapter.notifyDataSetChanged();
                                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        );
        //queue.add(prodRequest);
        MySingleton.getInstance(getActivity()).addToRequestQueue(prodRequest);
    }

    private List<Lista_productos> parseJsona(JSONArray jsonArray){
        // Variables locales
        List<Lista_productos> postss = new ArrayList<>();
        JSONObject objetod;

        try {


            for(int i=0; i<jsonArray.length(); i++){


                objetod = jsonArray.getJSONObject(i);

                Lista_productos post = new Lista_productos(
                        objetod.getString("ID_PRODUCTO"),
                        objetod.getString("DESCRIPCION_LARGA"),
                        objetod.getString("PRECIO"),
                        objetod.getString("PRECIO_CON_COMISION"),
                        objetod.getString("STOCK"));


                postss.add(post);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return postss;
    }

}
