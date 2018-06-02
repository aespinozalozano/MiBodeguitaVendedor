package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;



public class Confirmar_cancelar_pedido {

    private String id_pedido;
    private String mensaje;


    public Confirmar_cancelar_pedido() {
    }

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
