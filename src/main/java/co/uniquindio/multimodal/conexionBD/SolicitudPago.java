package co.uniquindio.multimodal.conexionBD;

import java.sql.Date;

public class SolicitudPago {
    private int id;
    private String vendedor;
    private Date fecha;
    private double monto;
    private String estado;
    private String comentarioRechazo;

    public SolicitudPago(int id, String vendedor, Date fecha, double monto, String estado, String comentarioRechazo) {
        this.id = id;
        this.vendedor = vendedor;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
        this.comentarioRechazo = comentarioRechazo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getVendedor() { return vendedor; }
    public Date getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getEstado() { return estado; }

    public void setId(int id) {
        this.id = id;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentarioRechazo() {
        return comentarioRechazo;
    }

    public void setComentarioRechazo(String comentarioRechazo) {
        this.comentarioRechazo = comentarioRechazo;
    }
}