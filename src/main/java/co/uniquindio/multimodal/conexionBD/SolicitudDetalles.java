package co.uniquindio.multimodal.conexionBD;

public class SolicitudDetalles {
    private int idSolicitud;
    private String vendedor;
    private String nivel;
    private double ventasRecientes;
    private double comisionesGeneradas;
    private String cumplimientoMetas;
    private String estado;
    private String comentarioRechazo;

    public SolicitudDetalles(int idSolicitud, String vendedor, String nivel, double ventasRecientes,
                             double comisionesGeneradas, String cumplimientoMetas, String estado, String comentarioRechazo) {
        this.idSolicitud = idSolicitud;
        this.vendedor = vendedor;
        this.nivel = nivel;
        this.ventasRecientes = ventasRecientes;
        this.comisionesGeneradas = comisionesGeneradas;
        this.cumplimientoMetas = cumplimientoMetas;
        this.estado = estado;
        this.comentarioRechazo = comentarioRechazo;
    }

    // Getters para cada campo

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public double getVentasRecientes() {
        return ventasRecientes;
    }

    public void setVentasRecientes(double ventasRecientes) {
        this.ventasRecientes = ventasRecientes;
    }

    public double getComisionesGeneradas() {
        return comisionesGeneradas;
    }

    public void setComisionesGeneradas(double comisionesGeneradas) {
        this.comisionesGeneradas = comisionesGeneradas;
    }

    public String getCumplimientoMetas() {
        return cumplimientoMetas;
    }

    public void setCumplimientoMetas(String cumplimientoMetas) {
        this.cumplimientoMetas = cumplimientoMetas;
    }

    public String getEstado() {
        return estado;
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
