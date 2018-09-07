package hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActualizarStockFrag2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActualizarStockFrag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActualizarStockFrag2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private EditText etPrecio,etStock;


    private static final String ARG_PARAM1 = "id_producto";
    private static final String ARG_PARAM2 = "descrip_larga";
    private static final String ARG_PARAM3 = "precio";
    private static final String ARG_PARAM4 = "precio_con_comision";
    private static final String ARG_PARAM5 = "stock";

    // TODO: Rename and change types of parameters
    private String idProducto;
    private String descripcionProducto;
    private String precioProducto;
    private String precioConComisionProducto;
    private String stockProducto;

    private OnFragmentInteractionListener mListener;

    public ActualizarStockFrag2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActualizarStockFrag2.
     */
    // TODO: Rename and change types and number of parameters
    public static ActualizarStockFrag2 newInstance(String param1, String param2, String param3, String param4,String param5) {
        ActualizarStockFrag2 fragment = new ActualizarStockFrag2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idProducto = getArguments().getString(ARG_PARAM1);
            descripcionProducto = getArguments().getString(ARG_PARAM2);
            precioProducto = getArguments().getString(ARG_PARAM3);
            precioConComisionProducto = getArguments().getString(ARG_PARAM4);
            stockProducto = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_actualizar_stock_frag2, container, false);
        TextView tvDescripcion = v.findViewById(R.id.tvDescripProdAct);
        etPrecio = v.findViewById(R.id.etPrecioActProd);
        etStock = v.findViewById(R.id.etStockActProd);
        Button btnActualizar = v.findViewById(R.id.btnActProd);

        tvDescripcion.setText(descripcionProducto);
        etPrecio.setText(precioProducto);
        etStock.setText(stockProducto);
        etPrecio.setSelection(etPrecio.getText().length());

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!precioProducto.equals(etPrecio.getText().toString().trim()) || !stockProducto.equals(etStock.getText().toString().trim()))
                updateStockPrecio(Preferences.load_id_tienda(getActivity()),idProducto,etStock.getText().toString().trim(),etPrecio.getText().toString().trim());
            }
        });




        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction_actualizar_stock_frag(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateStockPrecio(String id_tienda, String id_producto, final String stock, final String precio) {

        Map<String, String> params = new HashMap<>();
        params.put("id_tienda", id_tienda);
        params.put("id_producto", id_producto);
        params.put("stock", stock);
        params.put("precio", precio);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://52.67.109.198/ServicioWebVendedor/updateStockPrecio.php",new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String respuesta = response.getString("resultado");
                            switch (respuesta) {
                                case "exisoto":

                                    precioProducto=precio;
                                    stockProducto=stock;
                                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
                                        }
                                    }
                                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Actualizacion correcta",Snackbar.LENGTH_LONG).show();

                                   // Toast.makeText(getActivity(), "Actualizacion correcta", Toast.LENGTH_SHORT).show();
                                    break;

                                case "fallido":

                                    Snackbar.make(getActivity().findViewById(android.R.id.content),"No se pudo actualizar, intentelo nuevamente",Snackbar.LENGTH_LONG).show();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction_actualizar_stock_frag(Uri uri);
    }
}
