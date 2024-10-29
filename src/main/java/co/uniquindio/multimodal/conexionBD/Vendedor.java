package co.uniquindio.multimodal.conexionBD;

public class Vendedor {
    private int idVendedor;
    private String nombre;
    private String apellido;
    private String email;
    private String estadoVendedor; // Ahora es un String que almacena el nombre del estado

    // Constructor principal con el nombre del estado en lugar del ID
    public Vendedor(int idVendedor, String nombre, String apellido, String email, String estadoVendedor) {
        this.idVendedor = idVendedor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.estadoVendedor = estadoVendedor;
    }

    // Getters y Setters
    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstadoVendedor() {
        return estadoVendedor;
    }

    public void setEstadoVendedor(String estadoVendedor) {
        this.estadoVendedor = estadoVendedor;
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "Vendedor{" +
                "idVendedor=" + idVendedor +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", estadoVendedor='" + estadoVendedor + '\'' +
                '}';
    }
}
