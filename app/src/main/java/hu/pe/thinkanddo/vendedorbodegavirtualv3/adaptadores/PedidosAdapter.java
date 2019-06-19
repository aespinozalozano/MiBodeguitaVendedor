package hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.interfaces.ItemClickListener_pedido_detalle;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.MensajeChat;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Pedidos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;


public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidosViewHolder> {


    private Context context;
    private List<Pedidos> pedidos;
    private ItemClickListener_pedido_detalle clickListener;



    public PedidosAdapter(List<Pedidos> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;

    }

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedidos_cabecera, parent, false);
        return new PedidosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PedidosViewHolder pedidoViewHolder, final int position) {

        final Pedidos pedido = pedidos.get(position);


        long now = System.currentTimeMillis();

        long tiempoTranscurrido = (now - pedido.getFechorcre())/60000L;

        Format format = new SimpleDateFormat("hh:mm a",Locale.getDefault());

        pedidoViewHolder.hora_mensaje.setText(format.format(pedido.getFechorcre()));
        pedidoViewHolder.nombre.setText(ucFirst(pedido.getUsuario()));
        pedidoViewHolder.direccion.setText(pedido.getDireccion());


        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        final DecimalFormat df = new DecimalFormat("0.00",simbolos);
        final DecimalFormat df1 = new DecimalFormat("000",simbolos);


        pedidoViewHolder.tv_total_con_comision.setText("S/ "+df.format(Float.parseFloat(pedido.getTotal_con_comision())));
        pedidoViewHolder.estado_pedido.setText(pedido.getEstado_pedido());

        String NPedido = df1.format(position+1);


        pedidoViewHolder.numeroPedido.setText("Nº "+NPedido);

        switch (pedidoViewHolder.estado_pedido.getText().toString()) {
            case "Completado":
                pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.colorVerde));
                break;
            case "Anulado":
                pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.colorRojo));
                break;
            case "Pendiente":
                pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                break;
            case "En camino":
                pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.fondo_dir_tel));
                break;
            default:
                break;
        }

        //tomo el ultimo chat y valido si ya fue visto, sino es asi muestra la barra roja que indica nuevo mensaje
        FirebaseDatabase.getInstance().getReference("pedidos")
                .child(Preferences.load_id_tienda(context))
                .child(pedido.getIdPedido())
                .child("chat").limitToLast(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        MensajeChat chat = dataSnapshot.getValue(MensajeChat.class);

                        if (chat.getIdEmisor().equals(pedido.getIdCliente()) && !chat.isVisto()){
                            pedidoViewHolder.viewMensajeNuevo.setVisibility(View.VISIBLE);
                        }else{
                            pedidoViewHolder.viewMensajeNuevo.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    public Pedidos getProducto(int position){

        return pedidos.get(position);
    }

    public void setClickListener(ItemClickListener_pedido_detalle itemClickListener) {
        this.clickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(pedidos.get(position).getIdPedido());
    }

    private String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    class PedidosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView hora_mensaje;
        private TextView nombre;
        private TextView direccion;
        private TextView tv_total_con_comision;
        private TextView estado_pedido;
        private TextView numeroPedido;
        private View viewMensajeNuevo;


        PedidosViewHolder(View itemView){
            super(itemView);

            hora_mensaje = itemView.findViewById(R.id.tvHoraPedido);
            nombre = itemView.findViewById(R.id.tvNombreCliente);
            direccion = itemView.findViewById(R.id.tvDireccionCliente);
            estado_pedido= itemView.findViewById(R.id.tvStatusPedido);
            tv_total_con_comision = itemView.findViewById(R.id.tvPrecioTotalPedido);
            numeroPedido = itemView.findViewById(R.id.tvNumeroPedido);
            viewMensajeNuevo = itemView.findViewById(R.id.viewMensajeNuevo);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}