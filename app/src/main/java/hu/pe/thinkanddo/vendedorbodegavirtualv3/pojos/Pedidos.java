package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

import java.util.List;


public class Pedidos {

    private String id_pedido;
    private String usuario;
    private List<PedidoCompleto> pedidos_completo;
    private String pagara_con;
    private String direccion;
    private String telefono;
    private String obs;
    private String estado_pedido;
    private String hora_mensaje;
    private String id_cliente;

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Pedidos(){}

    public void setPedidos_completo(List<PedidoCompleto> pedidos_completo) {
        this.pedidos_completo = pedidos_completo;
    }

    public List<PedidoCompleto> getPedidos_completo() {
        return pedidos_completo;
    }

    /*public Pedidos(String usuario, String pedidos_completo, String pagara_con, String direccion, String estado_pedido) {
        this.usuario = usuario;
        this.pedidos_completo = pedidos_completo;
        this.pagara_con = pagara_con;
        this.direccion = direccion;
        this.estado_pedido = estado_pedido;

    }*/

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }



    public String getPagara_con() {
        return pagara_con;
    }

    public void setPagara_con(String pagara_con) {
        this.pagara_con = pagara_con;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado_pedido() {
        return estado_pedido;
    }

    public void setEstado_pedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }

    public String getHora_mensaje() {
        return hora_mensaje;
    }

    public void setHora_mensaje(String hora_mensaje) {
        this.hora_mensaje = hora_mensaje;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}