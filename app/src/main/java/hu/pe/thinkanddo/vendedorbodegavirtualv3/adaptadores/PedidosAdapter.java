package hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Pedidos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;


public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidosViewHolder> {


    private Context context;
    private List<Pedidos> pedidos;


    public PedidosAdapter(List<Pedidos> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;
    }

    @Override
    public PedidosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PedidosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PedidosViewHolder pedidoViewHolder, int position) {

        final Pedidos pedido = pedidos.get(position);

        pedidoViewHolder.obs.setText(pedido.getObs());
        pedidoViewHolder.obs.setPadding(5, 0, 5, 5);
        pedidoViewHolder.obs.setTextSize(17);

        pedidoViewHolder.hora_mensaje.setText(pedido.getHora_mensaje());
        pedidoViewHolder.nombre.setText(pedido.getUsuario());
        pedidoViewHolder.direccion.setText(pedido.getDireccion());
        pedidoViewHolder.telefono.setText(pedido.getTelefono());

        pedidoViewHolder.telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pedido.getTelefono()));
                context.startActivity(i);
            }

        });

        float totales = 0;

        pedidoViewHolder.tl_pedidos.removeAllViews();

        for (int i = 0; i < pedido.getPedidos_completo().size(); i++) {

            TableRow gh = new TableRow(context);
            gh.setPadding(10,0,10,0);
            gh.setGravity(Gravity.START);

            TableLayout.LayoutParams paramse;
            paramse = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 5.0f);
            gh.setLayoutParams(paramse);

            TextView descrip = new TextView(context);
            TableRow.LayoutParams paramse1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3.0f);
            descrip.setLayoutParams(paramse1);
            descrip.setPadding(10, 10, 10, 10);
            descrip.setText(pedido.getPedidos_completo().get(i).getDes_larga());
            descrip.setGravity(Gravity.CENTER);
            gh.addView(descrip);

            TextView cantidad = new TextView(context);
            TableRow.LayoutParams paramse2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            cantidad.setLayoutParams(paramse2);
            cantidad.setPadding(10, 10, 10, 10);
            cantidad.setText(new StringBuilder(pedido.getPedidos_completo().get(i).getCantidad() + " unid"));
            cantidad.setGravity(Gravity.CENTER);
            gh.addView(cantidad);

            TextView precio = new TextView(context);
            TableRow.LayoutParams paramse3 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            precio.setLayoutParams(paramse3);
            precio.setPadding(10, 10, 10, 10);
            precio.setText(new StringBuilder("S/. " + pedido.getPedidos_completo().get(i).getPrecio()));
            precio.setGravity(Gravity.CENTER);
            gh.addView(precio);

            pedidoViewHolder.tl_pedidos.addView(gh, i);
            totales = Integer.parseInt(pedido.getPedidos_completo().get(i).getCantidad()) * Float.parseFloat(pedido.getPedidos_completo().get(i).getPrecio()) + totales;

        }


        float totales_con_comision = 0;

        for (int i = 0; i < pedido.getPedidos_completo().size(); i++) {

            totales_con_comision = Integer.parseInt(pedido.getPedidos_completo().get(i).getCantidad()) * Float.parseFloat(pedido.getPedidos_completo().get(i).getPrecio_con_comision()) + totales_con_comision;

        }

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        final DecimalFormat df = new DecimalFormat("#.#",simbolos);

        final float comision = totales_con_comision-totales;
        final float ganancia_adicional = totales_con_comision-comision-totales;

        pedidoViewHolder.tv_total.setText(String.format("S/ %s", df.format(totales)));
        pedidoViewHolder.tv_ganancia_adicional.setText(String.format("S/. %s", df.format(ganancia_adicional)));
        pedidoViewHolder.tv_comision.setText(String.format("S/ %s", df.format(comision)));
        pedidoViewHolder.tv_total_a_pagar.setText(String.format("S/ %s", df.format(totales_con_comision)));
        pedidoViewHolder.tv_total_a_pagar.setTextSize(20f);
        pedidoViewHolder.tv_total_a_pagar.setTextColor(Color.BLACK);


        pedidoViewHolder.estado_pedido.setText(pedido.getEstado_pedido());

        pedidoViewHolder.estado_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("texto_boton",pedidoViewHolder.estado_pedido.getText().toString());

                if(pedidoViewHolder.estado_pedido.getText().toString().equals("CAMBIAR ESTADO PEDIDO")){

                    cambiar_estado_pedido("PREPARANDO PEDIDO",pedido.getId_cliente(),pedido.getId_pedido());

                    pedidoViewHolder.estado_pedido.setText(R.string.preparando_pedido);
                    pedidoViewHolder.estado_pedido.setBackgroundColor(ContextCompat.getColor(context, R.color.preparando_pedido));

                }else if(pedidoViewHolder.estado_pedido.getText().toString().equals("PREPARANDO PEDIDO")){

                    cambiar_estado_pedido("PEDIDO ENVIADO",pedido.getId_cliente(),pedido.getId_pedido());

                    pedidoViewHolder.estado_pedido.setText(R.string.pedido_enviado);
                    pedidoViewHolder.estado_pedido.setBackgroundColor(ContextCompat.getColor(context, R.color.pedido_enviado));
                    pedidoViewHolder.estado_pedido.setClickable(false);

                }

            }
        });

        pedidoViewHolder.estado_pedido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String estado = s.toString();

                switch (estado) {
                    case "PEDIDO COMPLETADO":
                        pedidoViewHolder.estado_pedido.setBackgroundColor(ContextCompat.getColor(context, R.color.pedido_completado));
                        pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.colorBlanco));
                        break;
                    case "PEDIDO ANULADO":
                        pedidoViewHolder.estado_pedido.setBackgroundColor(ContextCompat.getColor(context, R.color.pedido_anulado));
                        pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.colorBlanco));
                        break;
                    case "CAMBIAR ESTADO PEDIDO":
                        pedidoViewHolder.estado_pedido.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlanco));
                        pedidoViewHolder.estado_pedido.setTextColor(ContextCompat.getColor(context, R.color.texto_secundario));
                        break;
                }

            }
        });


        pedidoViewHolder.pagaraCon.setText(String.format("Cliente pagara con: S/. %s", pedido.getPagara_con()));

        pedidoViewHolder.vuelto.setTextColor(Color.WHITE);
        pedidoViewHolder.vuelto.setTextSize(20);
        pedidoViewHolder.vuelto.setTypeface(null, Typeface.BOLD_ITALIC);
        pedidoViewHolder.vuelto.setGravity(Gravity.CENTER);
        pedidoViewHolder.vuelto.setText(new StringBuilder("VUELTO:  " + "S/. "+df.format(Float.parseFloat(pedido.getPagara_con()) - totales_con_comision)));

    }


    private void cambiar_estado_pedido(String s,String id_cliente,String id_pedido) {

        HashMap<String, String> parat = new HashMap<>();

        parat.put("estado_pedido",s);
        parat.put("id_cli",id_cliente);
        parat.put("id_pedido",id_pedido);

        String URL_CAMBIAR_ESTADO = "http://52.67.109.198/ServicioWebVendedor/enviar_estado_pedido.php";

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_CAMBIAR_ESTADO, new JSONObject(parat),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        String rpta = "0";

                        try {
                            rpta = response.getString("result");
                        } catch (JSONException e) {e.printStackTrace();}
                        if (rpta.equals("1")) {

                            Log.e("envio_estado_pedido","correcto");

                            //pedidoViewHolder.estado_pedido.setText(R.string.preparando_pedido);

                            //Toast.makeText(getActivity(), "Tu pedido se envio correctamente", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(pedidos.get(position).getId_pedido());
    }


    class PedidosViewHolder extends RecyclerView.ViewHolder {

        private TextView hora_mensaje;
        private TextView nombre;
        private TextView direccion;
        private TextView telefono;
        private TextView vuelto;
        private TextView tv_total;
        private TextView tv_ganancia_adicional;
        private TextView tv_comision;
        private TextView estado_pedido;
        private TableLayout tl_pedidos;
        private TextView pagaraCon;
        private TextView tv_total_a_pagar;
        private TextView obs;


        PedidosViewHolder(View itemView){
            super(itemView);

            hora_mensaje = itemView.findViewById(R.id.fechaHora);
            nombre = itemView.findViewById(R.id.tv_nombre);
            direccion = itemView.findViewById(R.id.tv_direccion);
            telefono = itemView.findViewById(R.id.llamarCliente);
            tl_pedidos= itemView.findViewById(R.id.tableLayoutPedidos);
            obs = itemView.findViewById(R.id.observaciones);
            estado_pedido= itemView.findViewById(R.id.btnEstado_pedido);
            vuelto = itemView.findViewById(R.id.tvVuelto);
            tv_total = itemView.findViewById(R.id.tvTotal);
            tv_ganancia_adicional = itemView.findViewById(R.id.tvGananciaAdicional);
            tv_comision = itemView.findViewById(R.id.tvComision);
            pagaraCon = itemView.findViewById(R.id.tv_pagaraCon);
            tv_total_a_pagar = itemView.findViewById(R.id.tv_total_pagar);

        }


    }
}