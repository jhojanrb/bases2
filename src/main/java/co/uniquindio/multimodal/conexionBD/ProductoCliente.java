package co.uniquindio.multimodal.conexionBD;

public class ProductoCliente {

    private int id;
    private String nombre;
    private double precio;
    private String rutaImagen;

    public ProductoCliente(int id, String nombre, double precio, String rutaImagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}
