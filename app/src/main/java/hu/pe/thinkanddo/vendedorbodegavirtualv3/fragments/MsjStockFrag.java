package hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores.MjsStockAdapter;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.MsjStockReponer;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.SqliteHelper;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Variables;

public class MsjStockFrag extends Fragment {


    private List<MsjStockReponer> list;

    public MsjStockFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_msj_stock, container, false);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv_msj_stock);
        Button btn_borrar_todo = (Button) v.findViewById(R.id.btn_borrar_msj_stock);
        btn_borrar_todo.setTextColor(Color.WHITE);



        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        consultarMsjStock();

        final MjsStockAdapter adapter = new MjsStockAdapter(list,getActivity());
        rv.setAdapter(adapter);

        btn_borrar_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SqliteHelper con = new SqliteHelper(getActivity(),"bd_mensajes",null,1);
                SQLiteDatabase db = con.getWritableDatabase();

                db.execSQL("DELETE FROM " + Variables.TABLA_MENSAJES + ";");


                list.clear();
                adapter.notifyDataSetChanged();

            }
        });


        //Toast.makeText(getActivity(), mParam1.toString(), Toast.LENGTH_SHORT).show();

        return v;
    }

    private void consultarMsjStock() {

        list = new ArrayList<>();

        SqliteHelper con = new SqliteHelper(getActivity(),"bd_mensajes",null,1);

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
