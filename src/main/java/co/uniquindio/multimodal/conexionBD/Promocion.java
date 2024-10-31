package co.uniquindio.multimodal.conexionBD;


import java.sql.Date;

public class Promocion {
    private String nombre;
    private Date inicio;
    private Date fin;
    private float goldPorcentaje;
    private float platePorcentaje;
    private float bronzePorcentaje;
    private float platinoPorcentaje;

    public Promocion(String nombre, Date inicio, Date fin, float goldPorcentaje, float platePorcentaje, float bronzePorcentaje, float platinoPorcentaje) {
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.goldPorcentaje = goldPorcentaje;
        this.platePorcentaje = platePorcentaje;
        this.bronzePorcentaje = bronzePorcentaje;
        this.platinoPorcentaje = platinoPorcentaje;
    }

    // Getters y Setters para los campos
    public String getNombre() { return nombre; }
    public Date getInicio() { return inicio; }
    public Date getFin() { return fin; }
    public float getGoldPorcentaje() { return goldPorcentaje; }
    public float getPlatePorcentaje() { return platePorcentaje; }
    public float getBronzePorcentaje() { return bronzePorcentaje; }
    public float getPlatinoPorcentaje() { return platinoPorcentaje; }
}
