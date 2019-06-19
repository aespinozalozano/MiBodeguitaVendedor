package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores.ChatAdapter;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.MensajeChat;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.PedidoDetalle;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Pedidos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;

public class PedidoDetalleActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TextView tvSubtotal,tvTotal,tvComision,tvPagaraCon,tvVuelto,tvUsuario,tvFechaHora,tvDireccion,tvComentario,tvSinMensajes,tvStatusPedido;
    private LinearLayout llComentario,llTop;
    private AppCompatImageView ibMensaje,ibLlamar;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<MensajeChat> chatList;
    private Button  btnEnviarPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detalle);
        overridePendingTransitionEnter();

        final Pedidos pedido = (Pedidos)getIntent().getSerializableExtra("object");


        Type listType = new TypeToken<List<PedidoDetalle>>() {}.getType();
        List<PedidoDetalle> pedidoLista = new Gson().fromJson(pedido.getPedidoCompleto(),listType);



        //Log.e("TAG", "pedido "+pedido.getUsuario() );
        llComentario = findViewById(R.id.ll_comentario_adicional);
        tvComentario = findViewById(R.id.tvComentarioPedidoDetalle);
        tvUsuario = findViewById(R.id.tvNombreClienteEnPedidoDetalle);
        tvFechaHora = findViewById(R.id.tvHoraPedidoEnPedidoDetalle);
        tvDireccion = findViewById(R.id.tvDireccionClienteEnPedidoDetalle);
        tableLayout = findViewById(R.id.tableLayoutPedidos);
        tvSubtotal = findViewById(R.id.tvSubtotalPedidoDetalle);
        tvTotal = findViewById(R.id.tvTotalPedidoDetalle);
        tvComision = findViewById(R.id.tvComisionPedidoDetalle);
        tvPagaraCon = findViewById(R.id.tvPagaraconPedidoDetalle);
        tvVuelto = findViewById(R.id.tvVueltoPedidoDetalle);
        ibLlamar = findViewById(R.id.ibLlamarEnPedidoDetalle);
        ibMensaje = findViewById(R.id.ibMensajeEnPedidoDetalle);
        recyclerView =findViewById(R.id.rvChatConCliente);
        tvSinMensajes = findViewById(R.id.tvSinMensajesPedidoDetalle);
        llTop = findViewById(R.id.linearLayout2);
        tvStatusPedido = findViewById(R.id.tvStatusPedidoEnPedidoDetalle);
        btnEnviarPedido = findViewById(R.id.btnEnviarEnPedidoDetalle);



        chatList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("pedidos")
                .child(Preferences.load_id_tienda(PedidoDetalleActivity.this))
                .child(pedido.getIdPedido())
                .child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MensajeChat mensajeChat = snapshot.getValue(MensajeChat.class);
                    chatList.add(mensajeChat);
                }


                adapter = new ChatAdapter( PedidoDetalleActivity.this,chatList);
                LinearLayoutManager lim = new LinearLayoutManager(PedidoDetalleActivity.this);
                lim.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(lim);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference("pedidos")
                .child(Preferences.load_id_tienda(PedidoDetalleActivity.this))
                .child(pedido.getIdPedido())
                .child("estado_pedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String estadoPedido = (String)dataSnapshot.getValue();
                if (estadoPedido.equals("Completado")){

                    Drawable img = PedidoDetalleActivity.this.getResources().getDrawable( R.drawable.ic_check_black_24dp );
                    tvStatusPedido.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);

                    tvStatusPedido.setText("Pedido finalizado");
                    tvStatusPedido.setTextColor(getResources().getColor(R.color.fondo_boton_enviar_en_pedido_detalle));
                    btnEnviarPedido.setVisibility(View.GONE);
                    tvStatusPedido.setVisibility(View.VISIBLE);
                }else if (estadoPedido.equals("En camino")){

                    Drawable img = PedidoDetalleActivity.this.getResources().getDrawable( R.drawable.ic_send_black_24dp );
                    tvStatusPedido.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                    tvStatusPedido.setText("Pedido en camino");
                    tvStatusPedido.setTextColor(getResources().getColor(R.color.fondo_dir_tel));
                    btnEnviarPedido.setVisibility(View.GONE);
                    tvStatusPedido.setVisibility(View.VISIBLE);
                }else if (estadoPedido.equals("Pendiente")){
                    btnEnviarPedido.setVisibility(View.VISIBLE);
                    tvStatusPedido.setVisibility(View.GONE);
                }else if (estadoPedido.equals("Anulado")){
                    Drawable img = PedidoDetalleActivity.this.getResources().getDrawable( R.drawable.ic_cancel_black_24dp );
                    tvStatusPedido.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                    tvStatusPedido.setText("Pedido anulado");
                    tvStatusPedido.setTextColor(getResources().getColor(R.color.colorRojoPedidoDetalle));
                    btnEnviarPedido.setVisibility(View.GONE);
                    tvStatusPedido.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(pedido.getObs().equals("Sin observaciones"))llComentario.setVisibility(View.GONE);

        long now = System.currentTimeMillis();
        long tiempoTranscurrido = (now - pedido.getFechorcre())/60000L;
        String tiempo = "Hace "+tiempoTranscurrido +" min";

        tvFechaHora.setText(tiempo);
        tvUsuario.setText(ucFirst(pedido.getUsuario()));
        tvDireccion.setText(pedido.getDireccion());
        tvComentario.setText(pedido.getObs());

        //float totales = 0;

        tableLayout.removeAllViews();

        for (int i = 0; i < pedidoLista.size(); i++) {

            TableRow gh = new TableRow(this);
            gh.setPadding(10,0,10,0);
            gh.setGravity(Gravity.START);

            TableLayout.LayoutParams paramse;
            paramse = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 5.0f);
            gh.setLayoutParams(paramse);

            TextView descrip = new TextView(this);
            TableRow.LayoutParams paramse1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3.0f);
            descrip.setLayoutParams(paramse1);
            descrip.setPadding(10, 10, 10, 10);
            descrip.setText(pedidoLista.get(i).getDesc_larg());
            descrip.setGravity(Gravity.START);
            descrip.setTextColor(getResources().getColor(android.R.color.black));
            descrip.setAlpha(0.87f);
            gh.addView(descrip);

            TextView cantidad = new TextView(this);
            TableRow.LayoutParams paramse2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            cantidad.setLayoutParams(paramse2);
            cantidad.setPadding(10, 10, 10, 10);
            cantidad.setText(new StringBuilder(pedidoLista.get(i).getCantidad() + " unid"));
            cantidad.setGravity(Gravity.CENTER);
            cantidad.setTextColor(getResources().getColor(android.R.color.black));
            cantidad.setAlpha(0.87f);
            gh.addView(cantidad);

            TextView precio = new TextView(this);
            TableRow.LayoutParams paramse3 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            paramse3.setMarginEnd(32);
            precio.setLayoutParams(paramse3);
            precio.setPadding(10, 10, 10, 10);
            precio.setText(new StringBuilder("S/ " + pedidoLista.get(i).getPrecio()));
            precio.setGravity(Gravity.END);
            precio.setTextColor(getResources().getColor(android.R.color.black));
            precio.setAlpha(0.87f);
            gh.addView(precio);

            tableLayout.addView(gh, i);
            //totales = Integer.parseInt(pedidoLista.get(i).getCantidad()) * Float.parseFloat(pedidoLista.get(i).getPrecio()) + totales;

        }

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        final DecimalFormat df = new DecimalFormat("0.0",simbolos);

        float comision = Float.valueOf(pedido.getTotal_con_comision())-Float.parseFloat(pedido.getTotal());
        float vuelto = Float.parseFloat(pedido.getPagar_con())-Float.parseFloat(pedido.getTotal_con_comision());

        tvSubtotal.setText(String.format("S/ %s", df.format(Float.parseFloat(pedido.getTotal()))));
        tvTotal.setText(String.format("S/ %s", df.format(Float.parseFloat(pedido.getTotal_con_comision()))));
        tvComision.setText(String.format("S/ %s", df.format(comision)));
        tvPagaraCon.setText(String.format("S/ %s", df.format(Float.parseFloat(pedido.getPagar_con()))));
        tvVuelto.setText(String.format("S/ %s", df.format(vuelto)));



        ibLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pedido.getTelefono()));
                startActivity(i);
            }

        });

        ibMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(pedido);
            }
        });

        btnEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("estado_pedido","En camino");

                FirebaseDatabase.getInstance().getReference("pedidos")
                        .child(Preferences.load_id_tienda(PedidoDetalleActivity.this))
                        .child(pedido.getIdPedido())
                        .updateChildren(hashMap);

            }
        });


    }

    private String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    private void showDialog(final Pedidos pedidoParametro) {

        showKeyboard();
        // custom dialog
        final Dialog dialog = new Dialog(PedidoDetalleActivity.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mensaje_cliente_tienda);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText etMensaje = dialog.findViewById(R.id.etEscribirMensajePedidoDetalle);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelarMensajeEnPedidoDetalle);
        Button btnEnviar = dialog.findViewById(R.id.btnEnviarMensajeEnPedidoDetalle);


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                dialog.dismiss();
            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensaje = etMensaje.getText().toString().trim();

                if (mensaje.length()!=0){
                    String idTienda = Preferences.load_id_tienda(PedidoDetalleActivity.this);

                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("idEmisor",idTienda);
                    hashMap.put("idReceptor",pedidoParametro.getIdCliente());
                    hashMap.put("datetime",ServerValue.TIMESTAMP);
                    hashMap.put("mensaje",mensaje);


                    FirebaseDatabase.getInstance().getReference("pedidos")
                            .child(idTienda)
                            .child(pedidoParametro.getIdPedido())
                            .child("chat").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            closeKeyboard();
                            dialog.dismiss();
                        }
                    });
                }else{
                    Toast.makeText(PedidoDetalleActivity.this, "Mensaje vacio", Toast.LENGTH_SHORT).show();
                }



            }
        });

        dialog.show();

    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.animator.slide_from_right, R.animator.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right);
    }
}
