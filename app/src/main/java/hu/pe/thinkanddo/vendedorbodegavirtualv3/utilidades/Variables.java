package hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades;


public class Variables {


    public static final String TABLA_MENSAJES = "mensajes";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_CATEGORIA = "categoria";
    public static final String CAMPO_PRODUCTO = "producto";
    public static final String CAMPO_STOCK = "stock";


    final static String CREAR_TABLA = "CREATE TABLE "+ TABLA_MENSAJES + " (" + CAMPO_ID +" integer PRIMARY KEY AUTOINCREMENT, " +  CAMPO_CATEGORIA + " text, " + CAMPO_PRODUCTO + " text, " + CAMPO_STOCK + " integer);";

    public static String TOKEN = "";


    // broadcast receiver intent filters
    public static final String CONFIRMAR_CANCELAR_PEDIDO = "confirmarCancelar";
    public static final String PEDIDO = "pedido";
}
