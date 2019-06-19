package hu.pe.thinkanddo.vendedorbodegavirtualv3.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.MensajeChat;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.HolderMensaje>{

    private List<MensajeChat> listMensajeChat;
    private Context c;
    private String urlFoto;
    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;

    public ChatAdapter(Context c, List<MensajeChat> listMensajeChat) {
        this.c = c;
        this.listMensajeChat = listMensajeChat;
    }

    public void addMensaje(MensajeChat m){
        listMensajeChat.add(m);
        notifyItemInserted(listMensajeChat.size());
    }

    public void deleteItems(){
        listMensajeChat.clear();
        //notifyItemInserted(listMensajeChat.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == MESSAGE_SENT)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_emisor, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_receptor, parent, false);

        return new HolderMensaje(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final HolderMensaje holder, int position) {

        final MensajeChat mChat = listMensajeChat.get(position);


        Date fecha = new Date(mChat.getDatetime());
        SimpleDateFormat sfd = new SimpleDateFormat("hh:mm a",Locale.getDefault());
        String date = sfd.format(fecha);
        holder.hora.setText(date);


        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        holder.hora.measure(0,0);

        //con estas lineas de codigo limito el ancho maximo del textview mensaje para que no desplace a la hora fuera de la pantalla
        int width = display.getWidth();
        int withMensaje = holder.hora.getMeasuredWidth();
        int margenes = (int)convertDpToPixel(30,c);

        holder.mensaje.setVisibility(View.VISIBLE);
        holder.mensaje.setText(mChat.getMensaje());
        holder.mensaje.setMaxWidth(width-withMensaje-margenes);

    }

    @Override
    public int getItemCount() {
        return listMensajeChat.size();
    }

    @Override
    public int getItemViewType(int position) {

        String idTienda = Preferences.load_id_tienda(c);

        if (listMensajeChat.get(position).getIdEmisor().equals(idTienda))
            return MESSAGE_SENT;

        return MESSAGE_RECEIVED;
    }


    public class HolderMensaje extends RecyclerView.ViewHolder{


        private TextView mensaje;
        private TextView hora;

        public HolderMensaje(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.mensajeMensaje);
            hora = itemView.findViewById(R.id.horaMensaje);

        }

    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
