package co.uniquindio.multimodal.conexionBD;

import java.sql.Date;

public class Afiliado {

    private Integer id;
    private String nombre;
    private String nivel;
    private Date fechaAfiliacion;
    private Double ventasTotales;
    private Double comisionesGeneradas;

    public Afiliado(Integer id, String nombre, String nivel, Date fechaAfiliacion, Double ventasTotales, Double comisionesGeneradas) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.fechaAfiliacion = fechaAfiliacion;
        this.ventasTotales = ventasTotales;
        this.comisionesGeneradas = comisionesGeneradas;
    }

    // Getters y Setters para cada atributo

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Date getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    public void setFechaAfiliacion(Date fechaAfiliacion) {
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public Double getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(Double ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public Double getComisionesGeneradas() {
        return comisionesGeneradas;
    }

    public void setComisionesGeneradas(Double comisionesGeneradas) {
        this.comisionesGeneradas = comisionesGeneradas;
    }
}
