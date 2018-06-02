package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

import android.os.Parcel;
import android.os.Parcelable;



public class PedidoCompleto implements Parcelable{

    private String des_larga;
    private String cantidad;
    private String precio;
    private String precio_con_comision;
    private String stock;
    private String categoria;

    public PedidoCompleto(String des_larga,String cantidad,String precio,String precio_con_comision,String stock,String categoria){

        this.des_larga = des_larga;
        this.cantidad = cantidad;
        this.precio = precio;
        this.precio_con_comision = precio_con_comision;
        this.stock = stock;
        this.categoria = categoria;
    }

    private PedidoCompleto(Parcel in) {
        des_larga = in.readString();
        cantidad = in.readString();
        precio = in.readString();
        precio_con_comision = in.readString();
        stock = in.readString();
        categoria = in.readString();
    }

    public static final Creator<PedidoCompleto> CREATOR = new Creator<PedidoCompleto>() {
        @Override
        public PedidoCompleto createFromParcel(Parcel in) {
            return new PedidoCompleto(in);
        }

        @Override
        public PedidoCompleto[] newArray(int size) {
            return new PedidoCompleto[size];
        }
    };

    public String getPrecio_con_comision() {
        return precio_con_comision;
    }


    public String getCategoria() {
        return categoria;
    }


    public String getDes_larga() {
        return des_larga;
    }


    public String getCantidad() {
        return cantidad;
    }


    public String getPrecio() {
        return precio;
    }


    public String getStock() {
        return stock;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(des_larga);
        dest.writeString(cantidad);
        dest.writeString(precio);
        dest.writeString(precio_con_comision);
        dest.writeString(stock);
        dest.writeString(categoria);
    }
}
