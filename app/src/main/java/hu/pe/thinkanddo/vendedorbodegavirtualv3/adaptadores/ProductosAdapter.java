package hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.interfaces.ItemClickListener_listar_productos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Lista_productos;


public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.Lista_productosViewHolder> {

    private final List<Lista_productos> item;
    private ItemClickListener_listar_productos clickListener;
    private Context ctx;


    public ProductosAdapter(Context ctx, List<Lista_productos> item) {
        this.item = item;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public Lista_productosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlistaprod,parent,false);
        return new Lista_productosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Lista_productosViewHolder holder, int position) {

        Lista_productos producto = item.get(position);

        holder.tv_descrip_larga.setText(producto.getDESCRIPCION_LARGA());

        float precio = Float.parseFloat(producto.getPRECIO_CON_COMISION());

        holder.tv_precio.setText(new StringBuilder("S/. "+precio));

        String url = "https://s3-sa-east-1.amazonaws.com/prodmb/id"+producto.getID_PRODUCTO()+".png";


            Glide.with(ctx)
                    .load(url)
                    .error(R.drawable.im_no_disp)
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.cargando)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.iv_prod);


    }

    @Override
    public int getItemCount() {
        if (item != null) return item.size();
        else return 0;
    }

    public String[] getProducto(int position){

        return new String[]{item.get(position).getID_PRODUCTO(),item.get(position).getDESCRIPCION_LARGA(),item.get(position).getPRECIO(),item.get(position).getPRECIO_CON_COMISION(),item.get(position).getSTOCK()};
    }

    public void setClickListener(ItemClickListener_listar_productos itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class Lista_productosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView tv_descrip_larga;
        final TextView tv_precio;
        AppCompatImageView iv_prod;


        Lista_productosViewHolder(View itemView) {
            super(itemView);

            tv_descrip_larga = itemView.findViewById(R.id.tv_descrip_larga);
            tv_precio = itemView.findViewById(R.id.tv_precio);
            iv_prod = itemView.findViewById(R.id.im_producto);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


}
