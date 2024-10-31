package co.uniquindio.multimodal.conexionBD;

import java.sql.Date;

public class SolicitudPago {
    private int id;
    private String vendedor;
    private Date fecha;
    private double monto;
    private String estado;

    public SolicitudPago(int id, String vendedor, Date fecha, double monto, String estado) {
        this.id = id;
        this.vendedor = vendedor;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getVendedor() { return vendedor; }
    public Date getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getEstado() { return estado; }
}