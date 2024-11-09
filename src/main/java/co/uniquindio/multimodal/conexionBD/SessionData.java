package co.uniquindio.multimodal.conexionBD;

public class SessionData {
    private static String nombreVendedor;
    private static Integer idVendedor;

    public static void setVendorData(String nombre, int id) {
        nombreVendedor = nombre;
        idVendedor = id;
    }

    public static String getNombreVendedor() {
        return nombreVendedor;
    }

    public static Integer getIdVendedor() {
        return idVendedor;
    }

    public static void clearSession() {
        nombreVendedor = null;
        idVendedor = null;
    }
}
