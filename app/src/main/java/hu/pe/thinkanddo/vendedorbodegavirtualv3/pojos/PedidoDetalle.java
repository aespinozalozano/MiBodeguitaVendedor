package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

public class PedidoDetalle {

    private String desc_larg;
    private String id_producto;
    private String cantidad;
    private String precio;
    private String precio_con_comision;
    private String stock;

    public PedidoDetalle() {
    }

    public PedidoDetalle(String desc_larg, String id_producto, String cantidad, String precio, String precio_con_comision, String stock) {
        this.desc_larg = desc_larg;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.precio_con_comision = precio_con_comision;
        this.stock = stock;
    }

    public String getDesc_larg() {
        return desc_larg;
    }

    public void setDesc_larg(String desc_larg) {
        this.desc_larg = desc_larg;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPrecio_con_comision() {
        return precio_con_comision;
    }

    public void setPrecio_con_comision(String precio_con_comision) {
        this.precio_con_comision = precio_con_comision;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
