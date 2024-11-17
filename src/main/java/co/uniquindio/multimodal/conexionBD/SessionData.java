package co.uniquindio.multimodal.conexionBD;

public class SessionData {
    private static String nombreVendedor;
    private static Integer idVendedor;

    private static String nombreCliente;
    private static Integer idCliente;

    public static void setVendorData(String nombre, int id) {
        nombreVendedor = nombre;
        idVendedor = id;
    }

    public static  void setClienteData(String nombre, int id){
        nombreCliente = nombre;
        idCliente = id;
    }

    public static String getNombreVendedor() {
        return nombreVendedor;
    }

    public static String getNombreCliente(){
        return nombreCliente;
    }

    public static Integer getIdVendedor() {
        return idVendedor;
    }

    public static Integer getIdCliente(){
        return idCliente;
    }

    public static void clearSession() {
        nombreVendedor = null;
        idVendedor = null;
    }

    public static void clearCliente(){
        nombreCliente = null;
        idVendedor = null;
    }
}
