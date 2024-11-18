package co.uniquindio.multimodal.conexionBD;

public class CarritoItem {
    private Integer productoId;
    private String nombre;
    private Integer cantidad;
    private Double total;

    public CarritoItem(Integer productoId, String nombre, Integer cantidad, Double total) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.total = total;
    }

    // Getters y setters
    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return nombre + " x" + cantidad + " - Total: $" + String.format("%.2f", total);
    }
}
