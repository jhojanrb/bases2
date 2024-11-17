package co.uniquindio.multimodal.conexionBD;

public class PedidoActual {

    private int numeroPedido;
    private double progreso;
    private String estado;

    public PedidoActual(int numeroPedido, double progreso, String estado) {
        this.numeroPedido = numeroPedido;
        this.progreso = progreso;
        this.estado = estado;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public double getProgreso() {
        return progreso;
    }

    public void setProgreso(double progreso) {
        this.progreso = progreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
