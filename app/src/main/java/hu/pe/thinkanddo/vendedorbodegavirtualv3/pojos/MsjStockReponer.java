package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

import android.os.Parcel;
import android.os.Parcelable;



public class MsjStockReponer implements Parcelable{

    private int id;
    private String categoria;
    private String producto;
    private String stock;

    public MsjStockReponer() {
    }



    private MsjStockReponer(Parcel in) {
        id = in.readInt();
        categoria = in.readString();
        producto = in.readString();
        stock = in.readString();
    }

    public static final Creator<MsjStockReponer> CREATOR = new Creator<MsjStockReponer>() {
        @Override
        public MsjStockReponer createFromParcel(Parcel in) {
            return new MsjStockReponer(in);
        }

        @Override
        public MsjStockReponer[] newArray(int size) {
            return new MsjStockReponer[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(categoria);
        dest.writeString(producto);
        dest.writeString(stock);
    }
}
