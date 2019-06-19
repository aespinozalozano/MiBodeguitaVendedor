package hu.pe.thinkanddo.vendedorbodegavirtualv3.servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Confirmar_cancelar_pedido;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.PedidoCompleto;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Pedidos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades.PantallaPedidos;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Variables;


public class   MiFirebaseServicioMensaje extends FirebaseMessagingService {

    /*private ArrayList<PedidoCompleto> lista_ped= new ArrayList<>();

    @Override
    public void onMessageReceived(RemoteMessage remote) {
        super.onMessageReceived(remote);

        //Log.e("tag", remote.getData().get("id_pedido"));

        if (remote.getData() != null) {

            if(remote.getData().get("tipo_mensaje").equals("enviar_pedido")){

                try {

                    JSONArray pedido_jsonarray = new JSONArray(remote.getData().get("pedidos_completo_can_pre"));

                    for (int i = 0; i < pedido_jsonarray.length(); i++) {

                        JSONObject pedi = pedido_jsonarray.getJSONObject(i);

                        if (pedi.length() > 0) {
                            lista_ped.add(new PedidoCompleto(pedi.getString("desc_larg"), pedi.getString("cantidad"), pedi.getString("precio"),pedi.getString("precio_con_comision"), pedi.getString("stock"), pedi.getString("cat")));

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String id_pedido = remote.getData().get("id_pedido");
                String direccion = remote.getData().get("direccion");
                String id_cliente = remote.getData().get("idcliente");
                String hora_mensaje = remote.getData().get("hora_mensaje");
                String telefono = remote.getData().get("telefono");
                String pagara_con = remote.getData().get("pagara_con");
                String obs = remote.getData().get("obs");
                String usuario = remote.getData().get("usuario");
                String cabezera = remote.getData().get("cabezera");
                String cuerpo = remote.getData().get("cuerpo");


                showNotification(cabezera, cuerpo);
                guardarEnLocal();
                pedido_fcm(id_pedido, usuario, lista_ped, pagara_con, direccion, obs, hora_mensaje, telefono, id_cliente);

            }else if(remote.getData().get("tipo_mensaje").equals("confirmar_anular")) {

                Intent i = new Intent(Variables.CONFIRMAR_CANCELAR_PEDIDO);
                i.putExtra("mensaje",remote.getData().get("mensaje"));
                i.putExtra("id_pedido",remote.getData().get("id_pedido"));

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);

                List<Confirmar_cancelar_pedido> completoList = Preferences.load_confirmacion_o_anulacion(getApplicationContext());
                if(completoList==null) completoList=new ArrayList<>();
                Confirmar_cancelar_pedido lista = new Confirmar_cancelar_pedido();
                lista.setId_pedido(remote.getData().get("id_pedido"));
                lista.setMensaje(remote.getData().get("mensaje"));
                completoList.add(lista);
                Preferences.save_confirmacion_o_anulacion(this,completoList);

            }
        }

    }

    private void guardarEnLocal() {
    }

    private void pedido_fcm(String idpedido, String usuario, ArrayList<PedidoCompleto> lista_ped, String pagara_con, String direccion, String obs, String horaMensaje, String telefono, String idcliente){


        Intent i = new Intent(Variables.PEDIDO);
        i.putExtra("idPedido",idpedido);
        i.putExtra("usuario",usuario);
        i.putParcelableArrayListExtra("pedidos_completo",lista_ped);
        i.putExtra("pagara_con",pagara_con);
        i.putExtra("direccion",direccion);
        i.putExtra("obs",obs);
        i.putExtra("horaMensaje",horaMensaje);
        i.putExtra("telefono",telefono);
        i.putExtra("id_cliente",idcliente);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);

        List<Pedidos> completoList = Preferences.loadArrayPedidos(getApplicationContext());
        if(completoList==null) completoList=new ArrayList<>();
        Pedidos lista = new Pedidos();
        lista.setIdPedido(idpedido);
        lista.setUsuario(usuario);
        lista.setPedidoCompleto(lista_ped);
        lista.setPagar_con(pagara_con);
        lista.setDireccion(direccion);
        lista.setObs(obs);
        lista.setHora_mensaje(horaMensaje);
        lista.setTelefono(telefono);
        lista.setIdCliente(idcliente);
        lista.setEstado_pedido("CAMBIAR ESTADO PEDIDO");
        completoList.add(lista);
        Preferences.saveArrayPedidos(this,completoList);

    }

    private void showNotification(String cabezera, String cuerpo){
        Intent i = new Intent(this,PantallaPedidos.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

        Builder builder = new Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(cabezera);
        builder.setContentText(cuerpo);
        builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.audio_bodega_virtual));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(cuerpo);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0,builder.build());
        }

    }*/
}
