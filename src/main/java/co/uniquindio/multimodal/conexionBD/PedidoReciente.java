package co.uniquindio.multimodal.conexionBD;

public class PedidoReciente {

    private int numeroPedido;
    private String fecha;
    private double total;
    private String estado;

    public PedidoReciente(int numeroPedido, String fecha, double total, String estado) {
        this.numeroPedido = numeroPedido;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
