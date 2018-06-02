package hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Confirmar_cancelar_pedido;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos.Pedidos;


public class Preferences {


    private static final String STRING_PREFERENCES = "vendedorPreference";
    private static final String PREFERENCE_LISTA_PEDIDO = "listaPedido";
    private static  final String PREFERENCE_CONFIRMAR_ANULAR_PEDIDO = "anularConfirmar";
    private static  final String PREFERENCE_ID_TIENDA = "idtienda";
    private static  final String PREFERENCE_COMISION = "comision";


    public static void savePreferenceBoolean(Context c, boolean b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key,b).apply();
    }

    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putString(key,b).apply();
    }

    public static boolean obtenerPreferenceBoolean(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);//Si es que nunca se ha guardado nada en esta key pues retornara false
    }

    public static String obtenerPreferenceString(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(key,"");//Si es que nunca se ha guardado nada en esta key pues retornara una cadena vacia
    }

    public static void save_id_tienda(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCE_ID_TIENDA, key);
        editor.apply();
    }

    public static String load_id_tienda(Context c){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREFERENCE_ID_TIENDA,"");//Si es que nunca se ha guardado nada en esta key pues retornara una cadena vacia
    }

    public static void save_comision(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCE_COMISION, key);
        editor.apply();
    }

    public static String load_comision(Context c){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(PREFERENCE_COMISION,"");//Si es que nunca se ha guardado nada en esta key pues retornara una cadena vacia
    }



    public static List<Confirmar_cancelar_pedido> load_confirmacion_o_anulacion(Context c){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsona = preferences.getString(PREFERENCE_CONFIRMAR_ANULAR_PEDIDO,null);
        List<Confirmar_cancelar_pedido> arrayList = null;
        if(jsona != null){
            Type type = new TypeToken<ArrayList<Confirmar_cancelar_pedido>>(){}.getType();
            arrayList = gson.fromJson(jsona, type);
        }
        return arrayList;
    }

    public static void save_confirmacion_o_anulacion(Context c,List<Confirmar_cancelar_pedido> arrayList){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString(PREFERENCE_CONFIRMAR_ANULAR_PEDIDO, json);
        editor.apply();
    }

    public static void saveArrayPedidos(Context c, List<Pedidos> arrayList) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString(PREFERENCE_LISTA_PEDIDO, json);
        editor.apply();
    }

    public static List<Pedidos> loadArrayPedidos(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(PREFERENCE_LISTA_PEDIDO, null);
        List<Pedidos> arrayList = null;
        if(json!=null) {
            Type type = new TypeToken<ArrayList<Pedidos>>(){}.getType();
            arrayList = gson.fromJson(json, type);
        }
        return arrayList;
    }

    public static void deleteArrayPedidos(Context c){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PREFERENCE_LISTA_PEDIDO).apply();
    }

    public static void deleteConfirmarAnular(Context c){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PREFERENCE_CONFIRMAR_ANULAR_PEDIDO).apply();
    }

    public static String getUserMailPrefs(SharedPreferences preferences){
        return preferences.getString("mail","");
    }

    public static String getUserPassPrefs(SharedPreferences preferences){
        return preferences.getString("clave","");
    }

}
