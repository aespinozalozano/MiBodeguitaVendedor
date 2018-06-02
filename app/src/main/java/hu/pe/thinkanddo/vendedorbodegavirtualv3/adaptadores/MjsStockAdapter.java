package hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.MsjStockReponer;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.SqliteHelper;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Variables;


public class MjsStockAdapter extends RecyclerView.Adapter<MjsStockAdapter.MjsStockViewHolder> {

    private Context context;
    private List<MsjStockReponer> lista;



    public MjsStockAdapter(List<MsjStockReponer> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public MjsStockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msj_stock, parent, false);
        return new MjsStockViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MjsStockViewHolder holder, final int position) {

        holder.tvMsjCat.setText(lista.get(position).getCategoria());
        holder.tvMsjCat.setGravity(Gravity.CENTER);
        holder.tvMsjProd.setText(lista.get(position).getProducto());
        holder.tvMsjStock.setText(new StringBuilder(lista.get(position).getStock()));
        holder.tvMsjStock.setTextSize(24f);

        if (Integer.parseInt(lista.get(position).getStock())<10){
            holder.tvMsjStock.setTextColor(Color.parseColor("#F1948A"));
            if (Integer.parseInt(lista.get(position).getStock())<7){
                holder.tvMsjStock.setTextColor(Color.parseColor("#EC7063"));
                if (Integer.parseInt(lista.get(position).getStock())<4){
                    holder.tvMsjStock.setTextColor(Color.parseColor("#E74C3C"));

                }
            }
        }

        holder.imMsjBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SqliteHelper con = new SqliteHelper(context,"bd_mensajes",null,1);
                SQLiteDatabase db = con.getWritableDatabase();

                db.execSQL("DELETE FROM " + Variables.TABLA_MENSAJES + " WHERE " +
                        Variables.CAMPO_ID + " = " + String.valueOf(lista.get(holder.getAdapterPosition()).getId()) + ";");


                lista.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());


            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class MjsStockViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMsjCat;
        private TextView tvMsjProd;
        private TextView tvMsjStock;
        private ImageButton imMsjBorrar;


        MjsStockViewHolder(View itemView) {
            super(itemView);

            tvMsjCat = (TextView)itemView.findViewById(R.id.tv_mjs_cat);
            tvMsjProd = (TextView)itemView.findViewById(R.id.tv_msj_prod);
            tvMsjStock = (TextView)itemView.findViewById(R.id.tv_msj_stock);
            imMsjBorrar = (ImageButton)itemView.findViewById(R.id.ib_msj_delete);
        }
    }
}
