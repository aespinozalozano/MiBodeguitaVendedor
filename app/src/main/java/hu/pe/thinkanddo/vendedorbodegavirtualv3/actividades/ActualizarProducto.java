package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;

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
import java.util.ArrayList;
import java.util.HashMap;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments.ActualizarStockFrag2;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments.Frag_buscar_productos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;

public class ActualizarProducto extends AppCompatActivity implements ActualizarStockFrag2.OnFragmentInteractionListener{

    private static final String TAG ="ActualizarProducto" ;
    private Toolbar toolbar;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        toolbar = findViewById(R.id.toolbar_actulizar_prod);
        toolbar.setTitle("Actualizar producto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Frag_buscar_productos fr = Frag_buscar_productos.newInstance("");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_contenedor,fr,"frag_buscar_prod").commit();



        /*AutoCompleteTextView actv = new AutoCompleteTextView(this);
        actv.setThreshold(1);
        String[] from = { "name", "description" };
        int[] to = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter a = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0);
        a.setStringConversionColumn(1);
        FilterQueryProvider provider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                // run in the background thread
                Log.d(TAG, "runQuery constraint: " + constraint);
                if (constraint == null) {
                    return null;
                }
                String[] columnNames = { BaseColumns._ID, "name", "description" };
                MatrixCursor c = new MatrixCursor(columnNames);
                try {
                    String urlString = "https://en.wikipedia.org/w/api.php?" +
                            "action=opensearch&search=" + constraint +
                            "&limit=8&namespace=0&format=json";
                    URL url = new URL(urlString);
                    InputStream stream = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String jsonStr = reader.readLine();
                    // output ["query", ["n0", "n1", ..], ["d0", "d1", ..]]
                    JSONArray json = new JSONArray(jsonStr);
                    JSONArray names = json.getJSONArray(1);
                    JSONArray descriptions = json.getJSONArray(2);
                    for (int i = 0; i < names.length(); i++) {
                        c.newRow().add(i).add(names.getString(i)).add(descriptions.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return c;
            }
        };
        a.setFilterQueryProvider(provider);
        actv.setAdapter(a);
        setContentView(actv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));*/

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actualizar_producto, menu);

        //invalidateOptionsMenu();

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_settings));

        searchView.setQueryHint("Buscar producto...");
        //searchView.setSearchableInfo(gets);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {



            @Override
            public boolean onQueryTextSubmit(String query) {
               // Frag_buscar_productos fr = Frag_buscar_productos.newInstance(query);
               // getSupportFragmentManager().beginTransaction().replace(R.id.fl_contenedor,fr,"frag_buscar_prod").commit();
                /*searchView.setFocusable(false);
                searchView.setIconified(false);
                searchView.clearFocus();*/



                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                Frag_buscar_productos fr = Frag_buscar_productos.newInstance(newText);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_contenedor,fr,"frag_buscar_prod").commit();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentInteraction_actualizar_stock_frag(Uri uri) {

    }
}
