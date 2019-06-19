package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

import java.io.Serializable;
import java.util.List;


public class Pedidos implements Serializable {

    private String idPedido;
    private String pedidoCompleto;
    private String pagar_con;
    private String direccion;
    private String telefono;
    private String obs;
    private String estado_pedido;
    private String idCliente;
    private String total;
    private String total_con_comision;
    private long fechorcre;
    private String usuario;

    public Pedidos(){}

    public Pedidos(String idPedido, String pedidoCompleto, String pagar_con, String direccion, String telefono, String obs, String estado_pedido, String idCliente, String total, String total_con_comision, long fechorcre, String usuario) {
        this.idPedido = idPedido;
        this.pedidoCompleto = pedidoCompleto;
        this.pagar_con = pagar_con;
        this.direccion = direccion;
        this.telefono = telefono;
        this.obs = obs;
        this.estado_pedido = estado_pedido;
        this.idCliente = idCliente;
        this.total = total;
        this.total_con_comision = total_con_comision;
        this.fechorcre = fechorcre;
        this.usuario = usuario;
    }

    public String getPedidoCompleto() {
        return pedidoCompleto;
    }

    public void setPedidoCompleto(String pedidoCompleto) {
        this.pedidoCompleto = pedidoCompleto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public long getFechorcre() {
        return fechorcre;
    }

    public void setFechorcre(long fechorcre) {
        this.fechorcre = fechorcre;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal_con_comision() {
        return total_con_comision;
    }

    public void setTotal_con_comision(String total_con_comision) {
        this.total_con_comision = total_con_comision;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPagar_con() {
        return pagar_con;
    }

    public void setPagar_con(String pagar_con) {
        this.pagar_con = pagar_con;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}