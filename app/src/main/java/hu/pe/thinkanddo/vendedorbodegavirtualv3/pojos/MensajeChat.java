package hu.pe.thinkanddo.vendedorbodegavirtualv3.pojos;

public class MensajeChat {

    private String idEmisor;
    private String idReceptor;
    private long datetime;
    private String mensaje;
    private boolean visto;

    public MensajeChat(){}

    public MensajeChat(String idEmisor, String idReceptor, long datetime, String mensaje, boolean visto) {
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.datetime = datetime;
        this.mensaje = mensaje;
        this.visto = visto;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
