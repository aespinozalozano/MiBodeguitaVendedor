package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores.MjsStockAdapter;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.MsjStockReponer;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.SqliteHelper;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Variables;

public class MsjeStockBajoActivity extends AppCompatActivity {


    private List<MsjStockReponer> list;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msje_stock_bajo);

        toolbar = (Toolbar) findViewById(R.id.toolbar_stock_bajo);
        toolbar.setTitle("Producto stock bajo");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_msj_stock);
        Button btn_borrar_todo = (Button) findViewById(R.id.btn_borrar_msj_stock);
        btn_borrar_todo.setTextColor(Color.WHITE);



        rv.setLayoutManager(new LinearLayoutManager(this));

        consultarMsjStock();

        final MjsStockAdapter adapter = new MjsStockAdapter(list,this);
        rv.setAdapter(adapter);

        btn_borrar_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SqliteHelper con = new SqliteHelper(MsjeStockBajoActivity.this,"bd_mensajes",null,1);
                SQLiteDatabase db = con.getWritableDatabase();

                db.execSQL("DELETE FROM " + Variables.TABLA_MENSAJES + ";");


                list.clear();
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void consultarMsjStock() {

        list = new ArrayList<>();

        SqliteHelper con = new SqliteHelper(this,"bd_mensajes",null,1);

        SQLiteDatabase db = con.getReadableDatabase();

        MsjStockReponer msj;

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Variables.TABLA_MENSAJES,null);

        while (cursor.moveToNext()){

            msj = new MsjStockReponer();
            msj.setId(cursor.getInt(0));
            msj.setCategoria(cursor.getString(1));
            msj.setProducto(cursor.getString(2));
            msj.setStock(cursor.getString(3));

            list.add(msj);
        }

        cursor.close();
    }
}
