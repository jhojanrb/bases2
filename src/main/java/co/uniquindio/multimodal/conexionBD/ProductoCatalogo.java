package co.uniquindio.multimodal.conexionBD;

public class ProductoCatalogo {

    private Integer productoId;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;

    public ProductoCatalogo(String nombre, String categoria, double precio, int stock, Integer productoId) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
