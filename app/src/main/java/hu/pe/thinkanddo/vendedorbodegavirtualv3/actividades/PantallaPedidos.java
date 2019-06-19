package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments.ActualizarStockFrag2;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments.ComisionFrag;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments.ContactoFrag;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.interfaces.ItemClickListener_pedido_detalle;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.fragments.NoDisponibleFrag;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.PedidoCompleto;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Pedidos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores.PedidosAdapter;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.SqliteHelper;

import static hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Count.setCounting;


@SuppressWarnings("ALL")
public class PantallaPedidos extends AppCompatActivity implements ActualizarStockFrag2.OnFragmentInteractionListener{

    private List<Pedidos> pedidosa;
    private PedidosAdapter adapter;
    RecyclerView rvPedidos;
    private static  final String TAG = "MyActivity";
    private BroadcastReceiver bR;
    public  Toolbar toolbar;
    public SoundPool sp;
    public int flujodemusica;
    private String idTienda ;
    private String a = "0";
    public MenuItem menu1A;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;


    private SqliteHelper con;

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    final String URL_CAMBIAR_DISPONIBILIDAD = "http://52.67.109.198/ServicioWebVendedor/cambiar_disponibilidad.php";

    private final ItemClickListener_pedido_detalle f= new ItemClickListener_pedido_detalle() {
        @Override
        public void onClick(View view, int position) {

            Pedidos producto = adapter.getProducto(position);
            Intent intent = new Intent(PantallaPedidos.this,PedidoDetalleActivity.class);
            intent.putExtra("object",producto);
            startActivity(intent);

            //showDialog(producto[2],producto[1],producto[4],producto[0],producto[3]);
            //Frag_pedir_cantidad mifragment = Frag_pedir_cantidad.newInstance(producto[0],producto[1],producto[2],producto[3],producto[4]);

            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_contenedor,mifragment).addToBackStack("pc").commit();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pedidos);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);//este sonido va a trabajar con el canal normal de musica del movil, eso quiere decir que se va a poder subir y bajar volumen con los botones respectivos
        flujodemusica= sp.load(this,R.raw.audio_bodega_virtual,1);
        idTienda = Preferences.load_id_tienda(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mi Bodeguita Vendedor");

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);

        pedidosa=new ArrayList<>();
        rvPedidos = (RecyclerView) findViewById(R.id.rv_pedidos);
        progressBar = findViewById(R.id.pbPantallaPedidos);
        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setReverseLayout(true);
        lim.setStackFromEnd(true);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        rvPedidos.setLayoutManager(lim);

        String idTienda = Preferences.load_id_tienda(PantallaPedidos.this);

        //rvPedidos.smoothScrollToPosition(adapter.getItemCount());
        //adapter.notifyItemInserted(0);
        //adapter.notifyDataSetChanged();

        final long server_timestamp = new Date().getTime();
        long milliseconds = 0;

        SimpleDateFormat sfd = new SimpleDateFormat("dd/M/yyyy hh:mm:ss",Locale.getDefault());
        String date = sfd.format(server_timestamp);
        String[] parts = date.split(" ");
        String dateInicio = parts[0]+" 00:00:00";

        Date d = null;
        try {
            d = sfd.parse(dateInicio);
            milliseconds = d.getTime();

            //tiempoTranscurridoDelDia = server_timestamp-milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*database.getReference("pedidos").child(Preferences.load_id_tienda(PantallaPedidos.this)).orderByChild("fechorcre").startAt(milliseconds).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Gson gson = new Gson();

               // List<Pedidos> arrayList = null;

                    Type type = new TypeToken<ArrayList<PedidoCompleto>>(){}.getType();


                    String id_pedido = (String)dataSnapshot.child("idPedido").getValue();
                    String usuario = (String)dataSnapshot.child("usuario").getValue();
                    List<PedidoCompleto> ListObjetos = gson.fromJson((String)dataSnapshot.child("pedidoCompleto").getValue(), type);
                    //ArrayList<PedidoCompleto> ListObjetos = (String)snapshot.child("pedidos_completo").getValue();
                    String pagara_con = (String)dataSnapshot.child("pagar_con").getValue();
                    String direccion = (String)dataSnapshot.child("direccion").getValue();
                    String obs = (String)dataSnapshot.child("obs").getValue();
                    //String hora_mensaje = (String)snapshot.child("horaMensaje").getValue();
                    String telefono = (String)dataSnapshot.child("telefono").getValue();
                    String id_cliente = (String)dataSnapshot.child("id_cliente").getValue();
                    String estado_pedido = (String)dataSnapshot.child("estado_pedido").getValue();

                    SimpleDateFormat sfd = new SimpleDateFormat("dd/M/yyyy hh:mm:ss",Locale.getDefault());
                    String date = sfd.format(new Date((long)dataSnapshot.child("fechorcre").getValue()));

                    //CrearPedido(id_pedido,usuario,ListObjetos,pagara_con,direccion,obs,"11:09 pm",telefono,id_cliente);
                    pedidosa.add(new Pedidos(id_pedido,usuario,ListObjetos,pagara_con,direccion,telefono,obs,estado_pedido,date,id_cliente));
                    //Collections.reverse(pedidosa);



                if (pedidosa.size()>0){
                    adapter = new PedidosAdapter(pedidosa,PantallaPedidos.this);
                    rvPedidos.setAdapter(adapter);
                    rvPedidos.smoothScrollToPosition(pedidosa.size());
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
        });*/


        FirebaseDatabase.getInstance().getReference("pedidos").child(Preferences.load_id_tienda(PantallaPedidos.this)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pedidosa.clear();


                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                            Pedidos pedido = snapshot.getValue(Pedidos.class);

                            pedidosa.add(pedido);

                            /*for (int i=0;i<pedidosa.size();i++){

                                Type type = new TypeToken<ArrayList<PedidoCompleto>>(){}.getType();

                                SimpleDateFormat sfd = new SimpleDateFormat("dd/M/yyyy hh:mm:ss",Locale.getDefault());
                                String date = sfd.format(new Date((long)snapshot.child("fechorcre").getValue()));

                                List<PedidoCompleto> ListObjetos = new Gson().fromJson((String)dataSnapshot.child("pedidoCompleto").getValue(), type);


                                Pedidos pedido = new Pedidos();
                                pedido.setDireccion((String)snapshot.child("direcion").getValue());
                                pedido.setEstado_pedido((String)snapshot.child("estado_pedido").getValue());
                                pedido.setHora_mensaje(date);
                                pedido.setIdCliente((String)snapshot.child("idCliente").getValue());
                                pedido.setIdPedido((String)snapshot.child("idPedido").getValue());
                                pedido.setObs((String)snapshot.child("obs").getValue());
                                pedido.setPagar_con((String)snapshot.child("pagar_con").getValue());
                                pedido.setTelefono((String)snapshot.child("telefono").getValue());
                                pedido.setUsuario((String)snapshot.child("usuario").getValue());
                                pedido.setTotal((String)snapshot.child("total").getValue());
                                pedido.setTotal_con_comision((String)snapshot.child("total_con_comision").getValue());
                                pedido.setPedidoCompleto(ListObjetos);

                                pedidosa.set(i,pedido);
                                adapter.notifyItemChanged(i);

                            }*/

                        }

                    adapter = new PedidosAdapter(pedidosa,PantallaPedidos.this);
                    rvPedidos.setAdapter(adapter);
                    rvPedidos.smoothScrollToPosition(pedidosa.size());
                    adapter.setClickListener(f);
                    progressBar.setVisibility(View.GONE);







                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
        });





        /*bR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(Variables.PEDIDO.equals(intent.getAction())){

                    String id_pedido = intent.getStringExtra("idPedido");
                    String usuario = intent.getStringExtra("usuario");
                    ArrayList<PedidoCompleto> ListObjetos = intent.getParcelableArrayListExtra("pedidos_completo");
                    String pagara_con = intent.getStringExtra("pagara_con");
                    String direccion = intent.getStringExtra("direccion");
                    String obs = intent.getStringExtra("obs");
                    String hora_mensaje = intent.getStringExtra("horaMensaje");
                    String telefono = intent.getStringExtra("telefono");
                    String id_cliente = intent.getStringExtra("id_cliente");

                    CrearPedido(id_pedido,usuario,ListObjetos,pagara_con,direccion,obs,hora_mensaje,telefono,id_cliente);

                    SQLiteDatabase db;

                    for(int i=0;i<ListObjetos.size();i++) {
                        if (Integer.parseInt(ListObjetos.get(i).getStock()) <= 2) {

                            LayerDrawable icon = (LayerDrawable) menu1A.getIcon();
                            a=String.valueOf(i+1);
                            setCounting(PantallaPedidos.this, icon, String.valueOf(a));
                            invalidateOptionsMenu();

                            con = new SqliteHelper(getApplicationContext(), "bd_mensajes", null, 1);

                            db = con.getWritableDatabase();

                            ContentValues values = new ContentValues();
                            values.put(Variables.CAMPO_CATEGORIA, ListObjetos.get(i).getCategoria());
                            values.put(Variables.CAMPO_PRODUCTO, ListObjetos.get(i).getDesc_larg());
                            values.put(Variables.CAMPO_STOCK, Integer.parseInt(ListObjetos.get(i).getStock()));
                            db.insert(Variables.TABLA_MENSAJES,Variables.CAMPO_ID,values);
                        }

                    }

                    //adapter.notifyItemInserted(0);
                    sp.play(flujodemusica, 1, 1, 0, 0, 1);
                    cancelNotification(PantallaPedidos.this);

                }else if(Variables.CONFIRMAR_CANCELAR_PEDIDO.equals(intent.getAction())){

                    String mensaje = intent.getStringExtra("mensaje");
                    String id_pedido = intent.getStringExtra("id_pedido");

                    for(int i=0;i<pedidosa.size();i++){
                        if( pedidosa.get(i).getIdPedido().equals(id_pedido)){
                            pedidosa.get(i).setEstado_pedido(mensaje);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        };*/

    }



    public static void cancelNotification(Context ctx) {
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nMgr != null) {
            nMgr.cancel(0);
        }
    }


    /*public void CrearPedido(String idPedido, String usuario, List<PedidoCompleto> pedidos_completo, String pagara_con, String direccion, String obs, String hora_mensaje, String telefono, String idcliente){
        Pedidos lista= new Pedidos();
        lista.setIdPedido(idPedido);
        lista.setUsuario(usuario);
        lista.setPedidoCompleto(pedidos_completo);
        lista.setPagar_con(pagara_con);
        lista.setDireccion(direccion);
        lista.setObs(obs);
        lista.setHora_mensaje(hora_mensaje);
        lista.setTelefono(telefono);
        lista.setIdCliente(idcliente);
        lista.setEstado_pedido("CAMBIAR ESTADO PEDIDO");
        pedidosa.add(0,lista);
        //adapter.notifyItemInserted(0);
        rvPedidos.smoothScrollToPosition(pedidosa.size());
        //Preferences.deleteArrayPedidos(PantallaPedidos.this);


    }*/

    @Override
    protected void onPause(){
        super.onPause();
        Log.e(TAG,"onpause");
        //Preferences.deleteArrayPedidos(PantallaPedidos.this);
        //Preferences.deleteConfirmarAnular(PantallaPedidos.this);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(bR);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onstop");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*LocalBroadcastManager.getInstance(this).registerReceiver(bR,new IntentFilter(Variables.PEDIDO));
        LocalBroadcastManager.getInstance(this).registerReceiver(bR,new IntentFilter(Variables.CONFIRMAR_CANCELAR_PEDIDO));
        List<Pedidos> completoList = Preferences.loadArrayPedidos(this);
        if(completoList!=null){
            for(Pedidos p : completoList){
                pedidosa.add(0,p);
                adapter.notifyItemInserted(0);
            }
            Preferences.deleteArrayPedidos(PantallaPedidos.this);
        }

        List<Confirmar_cancelar_pedido> completoList2 = Preferences.load_confirmacion_o_anulacion(this);
        if(completoList2!=null){
            for(int i=0;i<pedidosa.size();i++){
                for(int in=0;in<completoList2.size();in++){
                    if( pedidosa.get(i).getIdPedido().equals(completoList2.get(in).getIdPedido())){
                        pedidosa.get(i).setEstado_pedido(completoList2.get(in).getMensaje());
                        adapter.notifyItemChanged(i);
                    }
                }

            }

            Preferences.deleteConfirmarAnular(PantallaPedidos.this);

        }

        cancelNotification(PantallaPedidos.this);*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu1A = menu.findItem(R.id.ic_group);
        LayerDrawable icon = (LayerDrawable) menu1A.getIcon();
        setCounting(this, icon, a);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.x

        int id =item.getItemId();

        android.support.v4.app.Fragment mifragment = null;
        boolean fragmentSeleccionado = false;

        switch (id) {
            case R.id.no_disponible:

                cambiarDisponibilidad("0", idTienda);
                mifragment = new NoDisponibleFrag();
                fragmentSeleccionado = true;
                item.setVisible(false);
                toolbar.getMenu().findItem(R.id.disponible).setVisible(true);

                break;
            case R.id.disponible:

                Fragment g = getSupportFragmentManager().findFragmentByTag("et");
                getSupportFragmentManager().beginTransaction().remove(g).commit();
                cambiarDisponibilidad("1", idTienda);
                item.setVisible(false);
                toolbar.getMenu().findItem(R.id.no_disponible).setVisible(true);
                return true;

            case R.id.actualizar_prod: {

                startActivity(new Intent(this,ActualizarProducto.class));
                    return true;

            }
            case R.id.enviar_notificacion_clientes: {

                startActivity(new Intent(this,EnviarNotificacionActivity.class));
                return true;

            }
            case R.id.comision: {

                Fragment mifragmentd = new ComisionFrag();
                if (getSupportFragmentManager().getBackStackEntryCount()>=0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, mifragmentd, "comi").addToBackStack("comision").commit();
                    return true;
                } else {
                    return true;
                }

            }
            case R.id.cerrar_sesion:
                cambiarDisponibilidad("0", idTienda);
                rvPedidos.removeAllViews();
                pedidosa.clear();
                Preferences.deleteArrayPedidos(PantallaPedidos.this);
                firebaseAuth.signOut();
                finish();
                return true;

            case R.id.contacto:

                Fragment mifragmentd = ContactoFrag.newInstance(idTienda);
                if (getSupportFragmentManager().getBackStackEntryCount()>=0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, mifragmentd, "contacto").addToBackStack("contacto").commit();
                    return true;
                } else {
                    return true;
                }

            case R.id.ic_group:


                startActivity(new Intent(this, MsjeStockBajoActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
                /*toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        toolbar.setNavigationIcon(null);
                        toolbar.setTitle("Mi Bodeguita Vendedor");
                    }
                });

                LayerDrawable icon = (LayerDrawable) menu1A.getIcon();
                a = "0";
                setCounting(PantallaPedidos.this, icon, a);
                invalidateOptionsMenu();

                Fragment fra = new MsjStockFrag();
                if (getSupportFragmentManager().getBackStackEntryCount()>=0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fra, "frmsj").addToBackStack("h").commit();
                    toolbar.setTitle("Productos stock bajo");
                    return true;
                } else {
                    return true;
                }*/

        }

        if(fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,mifragment,"et").addToBackStack(null).commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void cambiarDisponibilidad(String activo, String idTienda){


        HashMap<String,String> parau = new HashMap<>();
        parau.put("activo", activo);
        parau.put("idTienda", idTienda);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL_CAMBIAR_DISPONIBILIDAD,new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
            }
        });


        MySingleton.getInstance(this).addToRequestQueue(request);

    }



    @Override
    public void onFragmentInteraction_actualizar_stock_frag(Uri uri) {

    }
}

