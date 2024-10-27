package co.uniquindio.multimodal.conexionBD;

public class SalesSummary {
    private String nivel;
    private int numeroVendedores;
    private int ventasTotales;

    // Constructor
    public SalesSummary(String nivel, int numeroVendedores, int ventasTotales) {
        this.nivel = nivel;
        this.numeroVendedores = numeroVendedores;
        this.ventasTotales = ventasTotales;
    }

    // Getters y Setters
    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getNumeroVendedores() {
        return numeroVendedores;
    }

    public void setNumeroVendedores(int numeroVendedores) {
        this.numeroVendedores = numeroVendedores;
    }

    public int getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(int ventasTotales) {
        this.ventasTotales = ventasTotales;
    }
}
