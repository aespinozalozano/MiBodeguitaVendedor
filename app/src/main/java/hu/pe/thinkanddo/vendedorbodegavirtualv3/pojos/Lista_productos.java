package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

public class Lista_productos {

    private String ID_PRODUCTO;
    private String DESCRIPCION_LARGA;
    private int CANTIDAD;
    private String PRECIO;
    private String PRECIO_CON_COMISION;
    private String STOCK;

    public Lista_productos() {
    }

    public Lista_productos(String ID_PRODUCTO, String DESCRIPCION_LARGA, String PRECIO, String PRECIO_CON_COMISION, String STOCK) {
        this.DESCRIPCION_LARGA = DESCRIPCION_LARGA;
        this.ID_PRODUCTO = ID_PRODUCTO;
        this.STOCK = STOCK;
        this.PRECIO = PRECIO;
        this.PRECIO_CON_COMISION = PRECIO_CON_COMISION;
    }

    public Lista_productos(String ID_PRODUCTO, String DESCRIPCION_LARGA, String PRECIO, String PRECIO_CON_COMISION, String STOCK, int CANTIDAD) {
        this.DESCRIPCION_LARGA = DESCRIPCION_LARGA;
        this.ID_PRODUCTO = ID_PRODUCTO;
        this.STOCK = STOCK;
        this.PRECIO = PRECIO;
        this.PRECIO_CON_COMISION = PRECIO_CON_COMISION;
        this.CANTIDAD = CANTIDAD;
    }

    public String getPRECIO_CON_COMISION() {
        return PRECIO_CON_COMISION;
    }

    public void setPRECIO_CON_COMISION(String PRECIO_CON_COMISION) {
        this.PRECIO_CON_COMISION = PRECIO_CON_COMISION;
    }

    public int getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(int CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public String getID_PRODUCTO() {
        return ID_PRODUCTO;
    }

    public void setID_PRODUCTO(String ID_PRODUCTO) {
        this.ID_PRODUCTO = ID_PRODUCTO;
    }

    public String getSTOCK() {
        return STOCK;
    }

    public void setSTOCK(String STOCK) {
        this.STOCK = STOCK;
    }

    public String getDESCRIPCION_LARGA() {
        return DESCRIPCION_LARGA;
    }

    public void setDESCRIPCION_LARGA(String DESCRIPCION_LARGA) {
        this.DESCRIPCION_LARGA = DESCRIPCION_LARGA;
    }

    public String getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(String PRECIO) {
        this.PRECIO = PRECIO;
    }
}
