package co.uniquindio.multimodal.conexionBD;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import co.uniquindio.multimodal.HistorialSolicitudesPagoController;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import oracle.jdbc.OracleTypes;

//imports email
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.sendgrid.*;
import java.io.IOException;


public class VerificarLogin {

    // URL de conexión a la base de datos Oracle con el SID xe
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "PROYECTO";  // Usuario de la base de datos
    private static final String PASSWORD = "root";  // Contraseña del usuario

    // Método para obtener una conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Cargar el driver de Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos Oracle.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de Oracle.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos Oracle.");
            e.printStackTrace();
            throw e;
        }

        return connection;
    }

    // Método para comprobar el procedimiento login_administrador

    /**
     * LOGIN ADMINISTRADOR
     *
     * @param email
     * @param contrasena
     * @return
     */
    public String loginAdministrador(String email, String contrasena) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String respuesta = null;

        try {
            // Obtener la conexión
            connection = getConnection();

            // Preparar la llamada al procedimiento almacenado
            String sql = "{ call login_administrador(?, ?, ?) }";
            callableStatement = connection.prepareCall(sql);

            // Establecer los parámetros de entrada (IN) y salida (OUT)
            callableStatement.setString(1, email);       // Parámetro p_email
            callableStatement.setString(2, contrasena);  // Parámetro p_contrasena
            callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR); // Parámetro p_respuesta (OUT)

            // Ejecutar el procedimiento
            callableStatement.execute();

            // Obtener la respuesta del parámetro OUT
            respuesta = callableStatement.getString(3);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Cerrar el CallableStatement y la conexión
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return respuesta;  // Devolver la respuesta obtenida del procedimiento
    }

    // Método para comprobar el procedimiento login_vendedor

    /**
     * LOGIN VENDEDOR
     *
     * @param email
     * @param idVendedor
     * @return
     */

    public String loginVendedor(String email, int idVendedor) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String respuesta = null;

        try {
            connection = getConnection();
            String sql = "{ call login_vendedor(?, ?, ?) }";
            callableStatement = connection.prepareCall(sql);

            callableStatement.setString(1, email);
            callableStatement.setInt(2, idVendedor);
            callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);

            callableStatement.execute();
            respuesta = callableStatement.getString(3);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return respuesta;
    }

    /**
     * LOGIN CLIENTE
     *
     * @param email
     * @param idCliente
     * @return
     */

    public String loginCliente(String email, int idCliente) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        String respuesta = null;

        try {
            connection = getConnection();
            String sql = "{ call login_cliente(?, ?, ?) }";
            callableStatement = connection.prepareCall(sql);

            callableStatement.setString(1, email);
            callableStatement.setInt(2, idCliente);
            callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);

            callableStatement.execute();
            respuesta = callableStatement.getString(3);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return respuesta;
    }

    /**
     * RESUMEN VENTAS EN EL TABLEVIEW ADMIN
     *
     * @return
     */

    // Método para obtener el resumen de ventas por nivel
    public List<SalesSummary> obtenerResumenVentasNivel() {
        List<SalesSummary> data = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call obtener_resumen_ventas_nivel(?)}");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                data.add(new SalesSummary(
                        rs.getString("nivel"),
                        rs.getInt("numero_vendedores"),
                        rs.getInt("ventas_totales")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    // Métodos para obtener los datos

    public int obtenerTotalVendedores() {
        int totalVendedores = 0;
        String sql = "SELECT COUNT(*) AS total FROM Vendedor";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                totalVendedores = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalVendedores;
    }

    public int obtenerVentasTotales() {
        int ventasTotales = 0;
        String sql = "SELECT SUM(num_ventas) AS total FROM Vendedor";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                ventasTotales = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventasTotales;
    }

    public int obtenerRegistrosMes() {
        int registrosMes = 0;
        String sql = "SELECT COUNT(*) AS total FROM Vendedor WHERE EXTRACT(MONTH FROM fecha_ingreso) = EXTRACT(MONTH FROM SYSDATE)";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                registrosMes = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registrosMes;
    }

    public String obtenerNombreCliente(int clienteId) {
        String nombreCliente = null;
        String sql = "SELECT nombre FROM Cliente WHERE id_cliente = ?"; // nombre correcto de la columna

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Establecer el parámetro en la consulta
            pstmt.setInt(1, clienteId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    nombreCliente = rs.getString("nombre"); // nombre de la columna correspondiente
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreCliente;
    }


    /**
     * OBTENER SOLICITUDES PENDIENTES DE LOS VENDEDORES
     *
     * @return
     */

    public List<Vendedor> obtenerSolicitudesPendientes() {
        List<Vendedor> solicitudes = new ArrayList<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_solicitudes_pendientes(?)}")) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                Vendedor vendedor = new Vendedor(
                        rs.getInt("id_vendedor"),               // ID del vendedor
                        rs.getString("nombre"),                 // Nombre del vendedor
                        rs.getString("apellido"),               // Apellido del vendedor
                        rs.getString("email"),                  // Email del vendedor
                        null,                                   // Nivel (opcional si no se devuelve)
                        rs.getString("estado_vendedor")         // Estado del vendedor
                );
                solicitudes.add(vendedor);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }


    /**
     * APROBAR VENDEDOR
     *
     * @param idVendedor
     */

    public void aprobarVendedor(int idVendedor) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call aprobar_vendedor(?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * RECHAZAR VENDEDOR
     *
     * @param idVendedor
     */
    public void rechazarVendedor(int idVendedor) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call rechazar_vendedor(?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * cargarPromociones
     *
     * @return
     */

    public List<Promocion> obtenerPromocionesActivas() {
        List<Promocion> promociones = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = getConnection();
            System.out.println("Conexión establecida.");

            stmt = connection.prepareCall("{call cargar_promociones(?)}");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            System.out.println("Procedimiento ejecutado.");

            ResultSet rs = (ResultSet) stmt.getObject(1);

            while (rs.next()) {
                Promocion promocion = new Promocion(
                        rs.getString("nombre"),
                        rs.getDate("inicio"),
                        rs.getDate("fin"),
                        rs.getFloat("gold_porcentaje"),
                        rs.getFloat("plate_porcentaje"),
                        rs.getFloat("bronze_porcentaje"),
                        rs.getFloat("platino_porcentaje")
                );
                promociones.add(promocion);
                System.out.println("Promoción cargada: " + promocion.getNombre());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return promociones;
    }


    /**
     * historialPromociones
     *
     * @return
     */
    public List<Promocion> obtenerHistorialPromociones() {
        List<Promocion> historialPromociones = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call cargar_historial_promociones(?)}");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);

            while (rs.next()) {
                Promocion promocion = new Promocion(
                        rs.getString("id"), // Nombre de columna ajustado para historial
                        rs.getDate("inicio"),
                        rs.getDate("fin"),
                        rs.getFloat("gold_porcentaje"),
                        rs.getFloat("plate_porcentaje"),
                        rs.getFloat("bronze_porcentaje"),
                        rs.getFloat("platino_porcentaje")
                );
                historialPromociones.add(promocion);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return historialPromociones;
    }

    /**
     * CREAR PROMOCION
     *
     * @param idPromocion
     * @param fechaInicio
     * @param fechaFin
     * @param descuentoGold
     * @param descuentoPlate
     * @param descuentoBronze
     * @param descuentoPlatino
     * @return
     */

    public boolean crearPromocion(String idPromocion, Date fechaInicio, Date fechaFin, float descuentoGold, float descuentoPlate, float descuentoBronze, float descuentoPlatino) {
        Connection connection = null;
        CallableStatement stmt = null;
        boolean success = false;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call crear_promocion(?, ?, ?, ?, ?, ?, ?)}");

            stmt.setString(1, idPromocion);
            stmt.setDate(2, fechaInicio);
            stmt.setDate(3, fechaFin);
            stmt.setFloat(4, descuentoGold);
            stmt.setFloat(5, descuentoPlate);
            stmt.setFloat(6, descuentoBronze);
            stmt.setFloat(7, descuentoPlatino);

            stmt.execute();
            success = true; // Si no hubo excepciones, el proceso fue exitoso

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    /**
     * EDITAR PROMOCION DE LA TABLA
     *
     * @param idPromocion
     * @param fechaInicio
     * @param fechaFin
     * @param descuentoGold
     * @param descuentoPlate
     * @param descuentoBronze
     * @param descuentoPlatino
     * @return
     */

    public boolean editarPromocion(String idPromocion, Date fechaInicio, Date fechaFin, float descuentoGold, float descuentoPlate, float descuentoBronze, float descuentoPlatino) {
        Connection connection = null;
        CallableStatement stmt = null;
        boolean success = false;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call editar_promocion(?, ?, ?, ?, ?, ?, ?)}");

            stmt.setString(1, idPromocion);
            stmt.setDate(2, fechaInicio);
            stmt.setDate(3, fechaFin);
            stmt.setFloat(4, descuentoGold);
            stmt.setFloat(5, descuentoPlate);
            stmt.setFloat(6, descuentoBronze);
            stmt.setFloat(7, descuentoPlatino);

            stmt.execute();
            success = true; // Indica que la edición fue exitosa

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    /**
     * ELIMINAR UNA PROMOCION DE LA TABLA
     *
     * @param idPromocion
     * @return
     */

    public boolean eliminarPromocion(String idPromocion) {
        Connection connection = null;
        CallableStatement stmt = null;
        boolean success = false;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call eliminar_promocion(?)}");

            stmt.setString(1, idPromocion);
            stmt.execute();

            success = true; // Si no hubo excepciones, la eliminación fue exitosa

        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    /**
     * CARGAR SOLICITUDES A LA TABLA
     *
     * @return
     */

    public List<SolicitudPago> obtenerSolicitudesPago() {
        List<SolicitudPago> solicitudes = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call cargar_solicitudes_pago(?)}");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);

            while (rs.next()) {
                SolicitudPago solicitud = new SolicitudPago(
                        rs.getInt("id"),
                        rs.getString("vendedor"),
                        rs.getDate("fecha"),
                        rs.getDouble("monto"),
                        rs.getString("estado"),
                        null
                );
                solicitudes.add(solicitud);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return solicitudes;
    }

    /**
     * CARGAR TODOS LOS VENDEDORES
     *
     * @return
     */

    public List<Vendedor> obtenerVendedores() {
        List<Vendedor> vendedores = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call cargar_vendedores(?)}");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);

            while (rs.next()) {
                Vendedor vendedor = new Vendedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("nivel"),
                        rs.getString("estado")
                );
                vendedores.add(vendedor);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return vendedores;
    }

    /**
     * BSUCAR VENDEDORES
     *
     * @param nombre
     * @param nivel
     * @param estado
     * @return
     */
    // Método para buscar vendedores
    public List<Vendedor> buscarVendedores(String nombre, String nivel, String estado) {
        List<Vendedor> vendedores = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call buscar_vendedores(?, ?, ?, ?)}");

            // Configura los parámetros de entrada
            stmt.setString(1, nombre);
            stmt.setString(2, nivel);
            stmt.setString(3, estado);

            // Configura el parámetro de salida (cursor)
            stmt.registerOutParameter(4, OracleTypes.CURSOR);

            // Ejecuta el procedimiento
            stmt.execute();

            // Procesa el cursor de resultados
            ResultSet rs = (ResultSet) stmt.getObject(4);
            while (rs.next()) {
                Vendedor vendedor = new Vendedor(
                        rs.getInt("id_vendedor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("nivel"),
                        rs.getString("estado")
                );
                vendedores.add(vendedor);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return vendedores;
    }

    /**
     * REGISTRAR VENDEDOR
     *
     * @param nombre
     * @param apellido
     * @param email
     * @param contrasena
     * @param nivel
     * @param estado
     * @return
     */

    public boolean registrarVendedor(String nombre, String apellido, String email, String contrasena, String nivel, String estado) {
        Connection connection = null;
        CallableStatement stmt = null;
        boolean success = false;

        try {
            connection = getConnection();
            stmt = connection.prepareCall("{call registrar_vendedor(?, ?, ?, ?, ?, ?)}");

            // Configura los parámetros de entrada
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, email);
            stmt.setString(4, contrasena);
            stmt.setString(5, nivel);
            stmt.setString(6, estado);

            // Ejecuta el procedimiento
            stmt.execute();
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    /**
     * Actualizar el nivel de un vendedor
     *
     * @param idVendedor ID del vendedor
     * @param nivel      Nuevo nivel del vendedor
     * @return true si la operación fue exitosa
     */
    public boolean actualizarNivelVendedor(int idVendedor, String nivel) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call actualizar_nivel_vendedor(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.setString(2, nivel);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualizar el estado de un vendedor
     *
     * @param idVendedor ID del vendedor
     * @param estado     Nuevo estado del vendedor
     * @return true si la operación fue exitosa
     */
    public boolean actualizarEstadoVendedor(int idVendedor, String estado) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call actualizar_estado_vendedor(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.setString(2, estado);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Eliminar un vendedor por su ID
     *
     * @param idVendedor ID del vendedor a eliminar
     * @return true si la operación fue exitosa
     */
    public boolean eliminarVendedor(int idVendedor) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call eliminar_vendedor(?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * OBTIENE LOS DETALLES TOTALES DEL VENDEDOR
     * @param idVendedor
     * @return
     */

    public Map<String, Object> obtenerDetallesVendedor(int idVendedor) {
        Map<String, Object> detalles = new HashMap<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_detalles_vendedor(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (rs.next()) {
                    detalles.put("idVendedor", rs.getInt("id_vendedor"));
                    detalles.put("nombre", rs.getString("nombre"));
                    detalles.put("email", rs.getString("email"));
                    detalles.put("nivel", rs.getString("nivel"));
                    detalles.put("estado", rs.getString("estado"));
                    detalles.put("fecha_ingreso", rs.getDate("fecha_ingreso")); // Nombre de columna corregido
                    detalles.put("ventasTotales", rs.getDouble("ventas_totales"));
                    detalles.put("comisionesGanadas", rs.getDouble("comisiones_ganadas"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }

    /**
     * ACTUALIZAR DATOS DEL VENDEDOR
     * @param idVendedor
     * @param nombre
     * @param apellido
     * @param email
     * @param nivel
     * @param estado
     * @return
     */

    public boolean actualizarVendedor(int idVendedor, String nombre, String apellido, String email, String nivel, String estado) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call actualizar_vendedor(?, ?, ?, ?, ?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido);
            stmt.setString(4, email);
            stmt.setString(5, nivel);
            stmt.setString(6, estado);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * APROBAR SOLICITUD DE PAGO
     * @param solicitudId
     * @return
     */

    // Método para aprobar solicitud de pago
    public boolean aprobarSolicitudPago(int solicitudId) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call aprobar_solicitud_pago(?)}")) { // Verifica que el nombre sea exacto

            stmt.setInt(1, solicitudId);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * RECHAZAR SOLICITUD PAGO
     * @param idSolicitud
     * @param comentario
     * @return
     */

    public boolean rechazarSolicitudPago(int idSolicitud, String comentario) {
        Connection connection = null;
        CallableStatement callableStatement = null;
        boolean success = false;

        try {
            connection = getConnection();
            String sql = "{ call rechazar_solicitud_pago(?, ?) }";
            callableStatement = connection.prepareCall(sql);

            // Establecer parámetros
            callableStatement.setInt(1, idSolicitud);
            callableStatement.setString(2, comentario);

            // Ejecutar el procedimiento
            callableStatement.execute();
            success = true;
        } catch (SQLException e) {
            System.out.println("Error al rechazar la solicitud de pago: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    /**
     * HISTORIAL DE SOLICITUDES DE PAGO
     * @return
     */

    public List<SolicitudPago> obtenerHistorialSolicitudesPago() {
        List<SolicitudPago> historial = new ArrayList<>();
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = getConnection();
            String sql = "{ call ver_historial_solicitudes_pago(?) }";
            callableStatement = connection.prepareCall(sql);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            // Ejecutar el procedimiento
            callableStatement.execute();

            // Obtener el resultado
            ResultSet rs = (ResultSet) callableStatement.getObject(1);
            while (rs.next()) {
                SolicitudPago solicitud = new SolicitudPago(
                        rs.getInt("id_solicitud"),
                        rs.getString("vendedor"),
                        rs.getDate("fecha"),
                        rs.getDouble("monto"),
                        rs.getString("estado"),
                        rs.getString("comentario_rechazo")
                );
                historial.add(solicitud);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener el historial de solicitudes de pago: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return historial;
    }

    /**
     *
     * @param idSolicitud
     * @return
     */


    public SolicitudDetalles obtenerDetallesSolicitud(int idSolicitud) {
        SolicitudDetalles detalles = null;

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{ call obtener_detalles_solicitud(?, ?) }")) {

            stmt.setInt(1, idSolicitud);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                if (rs.next()) {
                    detalles = new SolicitudDetalles(
                            rs.getInt("id_solicitud"),
                            rs.getString("vendedor"),
                            rs.getString("nivel"),
                            rs.getDouble("ventas_recientes"),
                            rs.getDouble("comisiones_generadas"),
                            rs.getString("cumplimiento_metas"),
                            rs.getString("estado"),
                            rs.getString("comentario_rechazo")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }

    /**
     * CONSULTAR O BUSCAR SOLICITUDES DE PAGO EN EL TABLEVIEW
     * @param filtroNombreId
     * @param estado
     * @param fechaSolicitud
     * @return
     */

    public List<SolicitudPago> consultarSolicitudesPago(String nombreId, String estado, java.sql.Date fecha) {
        List<SolicitudPago> solicitudes = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call consultar_solicitudes_pago(?, ?, ?, ?)}")) {

            stmt.setString(1, nombreId);
            stmt.setString(2, estado);
            if (fecha != null) {
                stmt.setDate(3, fecha);
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.registerOutParameter(4, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(4);
            while (rs.next()) {
                SolicitudPago solicitud = new SolicitudPago(
                        rs.getInt("id_solicitud"),
                        rs.getString("vendedor"),
                        rs.getDate("fecha"),
                        rs.getDouble("monto"),
                        rs.getString("estado"),
                        rs.getString("comentario_rechazo")
                );
                solicitudes.add(solicitud);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    /**
     * CONSULTA BUSCA EN LA TABLA DE HISTORIAL DE SOLICITUDES
     * @param nombreId
     * @param estado
     * @param fecha
     * @return
     */

    public List<SolicitudPago> consultarHistorialSolicitudes(String nombreId, String estado, java.sql.Date fecha) {
        List<SolicitudPago> solicitudes = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call consultar_historial_solicitudes(?, ?, ?, ?)}")) {

            stmt.setString(1, nombreId);
            stmt.setString(2, estado);
            if (fecha != null) {
                stmt.setDate(3, fecha);
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.registerOutParameter(4, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(4);
            while (rs.next()) {
                SolicitudPago solicitud = new SolicitudPago(
                        rs.getInt("id_solicitud"),
                        rs.getString("vendedor"),
                        rs.getDate("fecha"),
                        rs.getDouble("monto"),
                        rs.getString("estado"),
                        rs.getString("comentario_rechazo")
                        //rs.getDate("fecha_resolucion")  // Agregar si has añadido esta fecha en SolicitudPago
                );
                solicitudes.add(solicitud);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    /**
     * CARGA LOS PRODUCTOS A LA TABLA
     * @return
     */

    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_productos(?)}")) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                productos.add(producto);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * CARGAR LOS DETALLES EN LOS LABELS ADMIN
     * @param idProducto
     * @return
     */

    public ProductoDetalles obtenerDetallesProductoAdmin(int idProducto) {
        ProductoDetalles detalles = null;
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_detalles_producto_admin(?, ?, ?, ?, ?, ?, ?, ?)}")) {

            // Parámetro de entrada
            stmt.setInt(1, idProducto);

            // Parámetros de salida
            stmt.registerOutParameter(2, java.sql.Types.INTEGER);  // p_id
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);  // p_nombre
            stmt.registerOutParameter(4, java.sql.Types.VARCHAR);  // p_categoria
            stmt.registerOutParameter(5, java.sql.Types.NUMERIC);  // p_precio
            stmt.registerOutParameter(6, java.sql.Types.INTEGER);  // p_stock
            stmt.registerOutParameter(7, java.sql.Types.VARCHAR);  // p_descripcion
            stmt.registerOutParameter(8, java.sql.Types.VARCHAR);  // p_imagen (ruta de imagen como VARCHAR2)

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener los valores de salida
            int id = stmt.getInt(2);
            String nombre = stmt.getString(3);
            String categoria = stmt.getString(4);
            double precio = stmt.getDouble(5);
            int stock = stmt.getInt(6);
            String descripcion = stmt.getString(7);
            String rutaImagen = stmt.getString(8);

            // Crear el objeto ProductoDetalles con los datos obtenidos
            detalles = new ProductoDetalles(id, nombre, categoria, precio, stock, descripcion, rutaImagen);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }




    /**
     * METODO PARA AGREGAR UN PRODUCTO
     * @param nombre
     * @param categoriaId
     * @param precio
     * @param stock
     * @param descripcion
     * @param rutaImagen
     */


    public void agregarProducto(String nombre, int categoriaId, double precio, int stock, String descripcion, String rutaImagen) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call agregar_producto(?, ?, ?, ?, ?, ?)}")) {

            stmt.setString(1, nombre);
            stmt.setInt(2, categoriaId);
            stmt.setDouble(3, precio);
            stmt.setInt(4, stock);
            stmt.setString(5, descripcion);
            stmt.setString(6, rutaImagen);

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * METODO QUE ACTUALIZA LOS CAMPOS DE UN PRODUCTO
     * @param idProducto
     * @param nombre
     * @param categoria
     * @param precio
     * @param stock
     * @param descripcion
     * @param rutaImagen
     */

    public void actualizarProducto(int idProducto, String nombre, String categoria, double precio, int stock, String descripcion, String rutaImagen) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call actualizar_producto(?, ?, ?, ?, ?, ?, ?)}")) {

            stmt.setInt(1, idProducto);
            stmt.setString(2, nombre);
            stmt.setString(3, categoria);
            stmt.setDouble(4, precio);
            stmt.setInt(5, stock);
            stmt.setString(6, descripcion);
            stmt.setString(7, rutaImagen);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ELIMINA UN PRODUCTO DE LA BD
     * @param idProducto
     */

    public void eliminarProducto(int idProducto) {
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call eliminar_producto(?)}")) {

            stmt.setInt(1, idProducto);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * BUSCAR PRODUCTOS DEL CRUD ADMIN
     * @param nombreId
     * @param categoria
     * @return
     */

    public List<Producto> buscarProductos(String nombreId, String categoria) {
        List<Producto> productos = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call buscar_productos(?, ?, ?)}")) {

            stmt.setString(1, nombreId);
            stmt.setString(2, categoria);
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(3);
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                productos.add(producto);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * METODOS DEL LABEL HOME VENDEDOR
     * @param idVendedor
     * @return
     */

    public int obtenerVentasMes(int idVendedor) {
        int ventasMes = 0;
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_ventas_mes_vendedor(?, ?)}")) {
            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, java.sql.Types.INTEGER); // Cambiado a INTEGER
            stmt.execute();
            ventasMes = stmt.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventasMes;
    }

    public int obtenerComisionesMes(int idVendedor) {
        int comisionesMes = 0;
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_comisiones_mes_vendedor(?, ?)}")) {
            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, java.sql.Types.INTEGER); // Cambiado a INTEGER
            stmt.execute();
            comisionesMes = stmt.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comisionesMes;
    }

    public int obtenerAfiliadosDirectos(int idVendedor) {
        int afiliadosDirectos = 0;
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_afiliados_directos(?, ?)}")) {
            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, java.sql.Types.INTEGER); // Cambiado a INTEGER
            stmt.execute();
            afiliadosDirectos = stmt.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return afiliadosDirectos;
    }

    /**
     * METODO QUE OBTIENE EL PROGRESO DEL VENDEDOR
     * @param idVendedor
     * @return
     */

    public Map<String, Object> obtenerProgresoNivel(int idVendedor) {
        Map<String, Object> progresoData = new HashMap<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_progreso_nivel(?, ?, ?, ?, ?)}")) {

            // Parámetros de entrada y salida
            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR); // p_nivel_actual
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR); // p_siguiente_nivel
            stmt.registerOutParameter(4, Types.INTEGER);  // p_progreso
            stmt.registerOutParameter(5, java.sql.Types.VARCHAR); // p_detalle_progreso

            // Ejecutar el procedimiento
            stmt.execute();

            // Almacenar los resultados en el mapa
            progresoData.put("nivelActual", stmt.getString(2));
            progresoData.put("siguienteNivel", stmt.getString(3));
            progresoData.put("progreso", stmt.getDouble(4));
            progresoData.put("detalleProgreso", stmt.getString(5));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return progresoData;
    }

    /**
     * CARGAR RENDIMIENTO VENTAS HOME VENDEDOR
     * @param idVendedor
     * @return
     */

    public Map<String, Double> obtenerRendimientoVentas(int idVendedor) {
        Map<String, Double> rendimientoVentas = new HashMap<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_rendimiento_ventas(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            while (rs.next()) {
                String mes = rs.getString("mes");
                Double ventasMensuales = rs.getDouble("ventas_mensuales");
                rendimientoVentas.put(mes, ventasMensuales);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendimientoVentas;
    }

    /**
     * CARGAR VENTAS RECIENTES EN LA TABLA HOME VENDEDOR
     */


    // Clase auxiliar para representar una venta reciente
    public static class VentaReciente {
        private String fecha;
        private String producto;
        private int cantidad;
        private double total;

        public VentaReciente(String fecha, String producto, int cantidad, double total) {
            this.fecha = fecha;
            this.producto = producto;
            this.cantidad = cantidad;
            this.total = total;
        }

        public String getFecha() { return fecha; }
        public String getProducto() { return producto; }
        public int getCantidad() { return cantidad; }
        public double getTotal() { return total; }
    }

    // Método para obtener las últimas ventas de un vendedor
    public List<VentaReciente> obtenerUltimasVentas(int idVendedor) {
        List<VentaReciente> ultimasVentas = new ArrayList<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_ultimas_ventas(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            while (rs.next()) {
                String fecha = rs.getString("fecha");
                String producto = rs.getString("producto");
                int cantidad = rs.getInt("cantidad");
                double total = rs.getDouble("total");

                ultimasVentas.add(new VentaReciente(fecha, producto, cantidad, total));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ultimasVentas;
    }

    /**
     * METODO Y CLASE AUXILIAR PARA CARGAR LOS PRODUCTOS EN EL TABLE DEL CATALOGO DEL VENDEDOR
     */

    // Clase auxiliar para representar la información de un producto con comisión
    public static class ProductoConComision {
        private int id;
        private String nombre;
        private String categoria;
        private double precio;
        private double comisionPromedio;
        private int stock;

        public ProductoConComision(int id, String nombre, String categoria, double precio, double comisionPromedio, int stock) {
            this.id = id;
            this.nombre = nombre;
            this.categoria = categoria;
            this.precio = precio;
            this.comisionPromedio = comisionPromedio;
            this.stock = stock;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getCategoria() { return categoria; }
        public double getPrecio() { return precio; }
        public double getComisionPromedio() { return comisionPromedio; }
        public int getStock() { return stock; }
    }

    // Método para obtener la información de productos con comisión
    public List<ProductoConComision> obtenerProductosConComision() {
        List<ProductoConComision> productos = new ArrayList<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_informacion_productos_con_comision(?)}")) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                double precio = rs.getDouble("precio");
                double comisionPromedio = rs.getDouble("comision_promedio");
                int stock = rs.getInt("stock");

                productos.add(new ProductoConComision(id, nombre, categoria, precio, comisionPromedio, stock));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * CARGAR DETALLES PRODUCTO VENDEDOR
     * @param idProducto
     * @return
     */

    public ProductoVendedor obtenerDetallesProducto(int idProducto) {
        ProductoVendedor detalles = null;

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_detalles_producto(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

            // Parámetro de entrada
            stmt.setInt(1, idProducto);

            // Parámetros de salida
            stmt.registerOutParameter(2, java.sql.Types.INTEGER);  // p_id
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);  // p_nombre
            stmt.registerOutParameter(4, java.sql.Types.VARCHAR);  // p_categoria
            stmt.registerOutParameter(5, java.sql.Types.NUMERIC);  // p_precio
            stmt.registerOutParameter(6, java.sql.Types.NUMERIC);  // p_comision
            stmt.registerOutParameter(7, java.sql.Types.INTEGER);  // p_stock
            stmt.registerOutParameter(8, java.sql.Types.VARCHAR);  // p_descripcion
            stmt.registerOutParameter(9, Types.VARCHAR);           // p_imagen como VARCHAR

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener valores de los parámetros de salida
            int id = stmt.getInt(2);
            String nombre = stmt.getString(3);
            String categoria = stmt.getString(4);
            double precio = stmt.getDouble(5);
            double comision = stmt.getDouble(6);
            int stock = stmt.getInt(7);
            String descripcion = stmt.getString(8);
            String rutaImagen = stmt.getString(9);  // Ahora obtenemos la ruta como VARCHAR directamente

            // Crear objeto ProductoVendedor
            detalles = new ProductoVendedor(id, nombre, categoria, precio, comision, stock, descripcion, rutaImagen);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detalles;
    }

    /**
     * METODO PARA BUSCAR LOS PRODUCTOS DE LA TABLE EN EL CATALOGO DEL VENDEDOR
     * @param filtroNombreId
     * @param categoria
     * @return
     */

    public List<ProductoConComision> buscarProductosVendedor(String filtroNombreId, String categoria) {
        List<ProductoConComision> productos = new ArrayList<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call buscar_productos_vendedor(?, ?, ?)}")) {

            // Establece los parámetros del procedimiento
            stmt.setString(1, filtroNombreId != null && !filtroNombreId.isEmpty() ? filtroNombreId : null);
            stmt.setString(2, categoria != null && !categoria.isEmpty() ? categoria : null);
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

            // Ejecuta el procedimiento
            stmt.execute();

            // Procesa los resultados
            ResultSet rs = (ResultSet) stmt.getObject(3);
            while (rs.next()) {
                ProductoConComision producto = new ProductoConComision(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getDouble("comision_promedio"),  // Ajusta si el nombre de columna en la BD es distinto
                        rs.getInt("stock")
                );
                productos.add(producto);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    /**
     * CARGA LOS AFILIADOS A LA TABLA DE MI RED DE AFILIADOS DEL VENDEDOR
     * @param idVendedor
     * @return
     */

    public List<Afiliado> obtenerAfiliadosVendedor(int idVendedor) {
        List<Afiliado> afiliados = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_afiliados_vendedor(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);

            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String nivel = rs.getString("nivel");
                Date fechaAfiliacion = rs.getDate("fecha_afiliacion");
                double ventasTotales = rs.getDouble("ventas_totales");
                double comisionesGeneradas = rs.getDouble("comisiones_generadas");

                Afiliado afiliado = new Afiliado(id, nombre, nivel, fechaAfiliacion, ventasTotales, comisionesGeneradas);
                afiliados.add(afiliado);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Afiliados obtenidos: " + afiliados.size());
        return afiliados;
    }

    /**
     * CARGA LA INFORMACION EN EL DIAGRAMA DE PASTE EN RED AFILIADOS DEL VENDEDOR
     * @param idVendedor
     * @return
     */

    public List<PieChart.Data> obtenerDistribucionNivelesAfiliados(int idVendedor) {
        List<PieChart.Data> data = new ArrayList<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_distribucion_niveles_afiliados(?, ?)}")) {

            stmt.setInt(1, idVendedor);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);

            // Depuración: verifica los resultados de la consulta
            System.out.println("Datos de distribución de niveles para ID de vendedor: " + idVendedor);

            while (rs.next()) {
                String nivel = rs.getString("nivel");
                int cantidadAfiliados = rs.getInt("cantidad_afiliados");
                System.out.println("Nivel: " + nivel + ", Cantidad de Afiliados: " + cantidadAfiliados);
                data.add(new PieChart.Data(nivel, cantidadAfiliados));
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * CARGAR INFORMACION DE LA RED DEL VENDEDOR EN LOS LABELS
     * @param idVendedor
     * @return
     */

    public Map<String, Double> obtenerTotalesVendedor(int idVendedor) {
        Map<String, Double> resultados = new HashMap<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_totales_vendedor(?, ?, ?, ?)}")) {

            stmt.setInt(1, idVendedor);

            stmt.registerOutParameter(2, Types.NUMERIC); // p_total_afiliados
            stmt.registerOutParameter(3, Types.NUMERIC); // p_ventas_totales_red
            stmt.registerOutParameter(4, Types.NUMERIC); // p_comisiones_totales

            stmt.execute();

            resultados.put("totalAfiliados", stmt.getDouble(2));
            resultados.put("ventasTotalesRed", stmt.getDouble(3));
            resultados.put("comisionesTotales", stmt.getDouble(4));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    /**
     * BUSCAR AFILIADOS EN LA VISTA DE RED DEL VENDEDOR
     * @param idVendedor
     * @param nombreAfiliado
     * @param nivelFiltro
     * @return
     */

    public List<Afiliado> buscarAfiliados(int idVendedor, String nombreAfiliado, String nivelFiltro) {
        List<Afiliado> afiliados = new ArrayList<>();

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call buscar_afiliados(?, ?, ?, ?)}")) {

            // Configurar los parámetros de entrada
            stmt.setInt(1, idVendedor);
            stmt.setString(2, nombreAfiliado);
            stmt.setString(3, nivelFiltro);

            // Configurar el cursor de salida
            stmt.registerOutParameter(4, OracleTypes.CURSOR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el cursor de resultados
            ResultSet rs = (ResultSet) stmt.getObject(4);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String nivel = rs.getString("nivel");
                Date fechaAfiliacion = rs.getDate("fecha_afiliacion");
                double ventasTotales = rs.getDouble("ventas_totales");
                double comisionesGeneradas = rs.getDouble("comisiones_generadas");

                // Crear un nuevo objeto Afiliado y añadirlo a la lista
                Afiliado afiliado = new Afiliado(id, nombre, nivel, fechaAfiliacion, ventasTotales, comisionesGeneradas);
                afiliados.add(afiliado);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return afiliados;
    }

    /**
     * EXPORTAR PDF DEL HISOTRIAL DE SOLICITUDES DE LA VISTA ADMINISTRADOR
     * @param filePath
     * @throws DocumentException
     * @throws IOException
     */

    public void exportarHistorialPDF(String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        document.add(new Paragraph("Historial de Solicitudes de Pago", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(Chunk.NEWLINE);

        // Crear tabla PDF con las columnas de solicitud
        PdfPTable table = new PdfPTable(5); // Número de columnas
        table.setWidthPercentage(100);

        // Añadir encabezados de columna
        table.addCell("ID");
        table.addCell("Vendedor");
        table.addCell("Fecha");
        table.addCell("Monto");
        table.addCell("Estado");

        // Llenar la tabla con los datos de solicitudes
        List<SolicitudPago> historial = obtenerHistorialSolicitudesPago();
        for (SolicitudPago solicitud : historial) {
            table.addCell(String.valueOf(solicitud.getId()));
            table.addCell(solicitud.getVendedor());
            table.addCell(solicitud.getFecha().toString());
            table.addCell(String.valueOf(solicitud.getMonto()));
            table.addCell(solicitud.getEstado());
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }

    /**
     * OBTIENE EL NOMBRE DEL CLIENTE PARA SETEARLO EN SU VISTA
     * @param idCliente
     * @return
     */

    public String obtenerNombreClienteVista(int idCliente) {
        String nombreCliente = null;

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_nombre_cliente(?, ?)}")) {

            // Parámetro de entrada
            stmt.setInt(1, idCliente);

            // Parámetro de salida
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el resultado
            nombreCliente = stmt.getString(2);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombreCliente;
    }

    /**
     * OBTENER PRODUCTOS DESTACADOS EN LA VISTA DEL CLIENTE
     * @return
     */


    public List<ProductoCliente> obtenerProductosDestacados() {
        List<ProductoCliente> productosDestacados = new ArrayList<>();
        System.out.println("Obteniendo productos destacados...");

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_productos_destacados(?)}")) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                ProductoCliente producto = new ProductoCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("imagen")
                );
                productosDestacados.add(producto);
                System.out.println("Producto obtenido: " + producto.getNombre());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productosDestacados;
    }

    /**
     * Obtiene los pedidos recientes del cliente para cargarlos en su vista principal
     * @param clienteId
     * @return
     */

    public List<PedidoReciente> obtenerPedidosRecientesCliente(int clienteId) {
        List<PedidoReciente> pedidosRecientes = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call Obtener_Pedidos_Recientes_Cliente(?, ?)}")) {

            stmt.setInt(1, clienteId);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            while (rs.next()) {
                PedidoReciente pedido = new PedidoReciente(
                        rs.getInt("Nº Pedido"),
                        rs.getString("Fecha"),
                        rs.getDouble("Total"),
                        rs.getString("Estado")
                );
                pedidosRecientes.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidosRecientes;
    }


    /**
     * OBTIENE EL ESTADO DE LOS PEDIDOS ACTUALES DEL CLIENTE EN EL HOME DE EL MISMO
     * @param clienteId
     * @return
     */
    public List<PedidoActual> obtenerEstadoPedidosActualesCliente(int idCliente) {
        List<PedidoActual> pedidosActuales = new ArrayList<>();

        if (idCliente == 0) {
            idCliente = 1099682; // ID de prueba
        }
        System.out.println("Cliente ID enviado al procedimiento: " + idCliente);
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call Obtener_Estado_Pedidos_Actuales_Cliente(?, ?)}")) {

            stmt.setInt(1, idCliente);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);

            int count = 0; // Contador para depuración
            while (rs.next()) {
                int numeroPedido = rs.getInt("Nº Pedido");
                double progreso = rs.getDouble("Progreso");
                String estado = rs.getString("Estado");
                System.out.println("Pedido obtenido: " + numeroPedido + ", Progreso: " + progreso + ", Estado: " + estado);

                pedidosActuales.add(new PedidoActual(numeroPedido, progreso, estado));
                count++;
            }
            System.out.println("Total pedidos obtenidos del ResultSet: " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return pedidosActuales;
    }

    /**
     * OBTIENE LOS PRODUCTOS EN LA PARTE DEL CATALOGO DEL CLIENTE
     * @return
     */

    public List<ProductoCliente> obtenerProductosTendencia() {
        List<ProductoCliente> productos = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_productos_tendencia(?)}")) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String nombre = rs.getString("Nombre");
                    double precio = rs.getDouble("Precio");
                    String rutaImagen = rs.getString("Ruta_Imagen");
                    productos.add(new ProductoCliente(id, nombre, precio, rutaImagen));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * OBTIENE LOS PRODUCTOS EN LA TABLA DE CATALOGO DEL CLIENTE
     * @return
     */

    public List<ProductoCatalogo> obtenerProductosCatalogoCliente() {
        List<ProductoCatalogo> productos = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call OBTENER_PRODUCTOS_CATALOGO_CLIENTE(?)}")) {

            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Integer productoId = rs.getInt("ID");
                    String nombre = rs.getString("Nombre");
                    String categoria = rs.getString("Categoría");
                    double precio = rs.getDouble("Precio");
                    int stock = rs.getInt("Stock");
                    productos.add(new ProductoCatalogo(nombre, categoria, precio, stock, productoId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * METODO PARA BUSCAR PRODUCTOS DE LA TABLA EN LA VISTA DEL CLIENTE CATALOGO
     * @param busqueda
     * @param categoria
     * @return
     */

    public List<ProductoCatalogo> buscarProductosCliente(String busqueda, String categoria) {
        List<ProductoCatalogo> productos = new ArrayList<>();
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call BUSCAR_PRODUCTOS_CLIENTE(?, ?, ?)}")) {

            // Configurar parámetros de entrada
            stmt.setString(1, busqueda != null && !busqueda.isEmpty() ? busqueda : null);
            stmt.setString(2, categoria != null && !categoria.isEmpty() ? categoria : "Todas");

            // Configurar parámetro de salida
            stmt.registerOutParameter(3, OracleTypes.CURSOR);

            // Ejecutar procedimiento
            stmt.execute();

            // Leer el cursor de resultados
            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                while (rs.next()) {
                    String nombre = rs.getString("Nombre");
                    String categoriaResultado = rs.getString("Categoría");
                    double precio = rs.getDouble("Precio");
                    int stock = rs.getInt("Stock");
                    productos.add(new ProductoCatalogo(nombre, categoriaResultado, precio, stock, null));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * METODO PARA AGREGAR PRODUCTO AL CARRITO VISTA CLIENTE
     * @param clienteId
     * @param productoId
     * @param cantidad
     * @return
     */


    public String agregarProductoCarrito(int clienteId, int productoId, int cantidad) {
        String mensaje = "";
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call Agregar_Producto_Carrito(?, ?, ?, ?)}")) {

            // Configurar los parámetros del procedimiento
            stmt.setInt(1, clienteId);
            stmt.setInt(2, productoId);
            stmt.setInt(3, cantidad);

            // Parámetro de salida para el mensaje
            stmt.registerOutParameter(4, Types.VARCHAR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el mensaje de salida
            mensaje = stmt.getString(4);

        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = "Error al agregar el producto al carrito.";
        }
        return mensaje;
    }

    /**
     * CARGA LOS PRODUCTOS EXISTENTES DEL CARRITO DE COMPRAS
     * @param clienteId
     * @return
     */

    public List<CarritoItem> cargarCarritoCompras(int clienteId) {
        List<CarritoItem> carritoItems = new ArrayList<>();

        String query = "{ call CargarCarritoCompras(?, ?) }";

        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {

            stmt.setInt(1, clienteId);
            stmt.registerOutParameter(2, Types.REF_CURSOR);

            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                while (rs.next()) {
                    Integer productoId = rs.getInt("ProductoID"); // Asegúrate de usar el alias correcto
                    String nombre = rs.getString("Nombre");
                    Integer cantidad = rs.getInt("Cantidad");
                    Double total = rs.getDouble("Total");

                    carritoItems.add(new CarritoItem(productoId, nombre, cantidad, total));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carritoItems;
    }


    /**
     * METODO PARA ELIMINAR UN PRODUCTO DEL CARRITO DE COMPRAS
     * @param clienteId
     * @param productoId
     * @return
     */

    public String eliminarProductoCarrito(int clienteId, int productoId) {
        String mensaje = "";
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call Eliminar_Producto_Carrito(?, ?, ?)}")) {

            // Parámetros de entrada
            stmt.setInt(1, clienteId);
            stmt.setInt(2, productoId);

            // Parámetro de salida
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el mensaje de salida
            mensaje = stmt.getString(3);

        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = "Error al eliminar el producto del carrito.";
        }
        return mensaje;
    }

    /**
     * REALIZA EL PEDIDO DE LOS PRODUCTOS EN EL CARRITO DE COMPRAS
     * @param clienteId
     * @param vendedorId
     * @return
     */

    public String realizarPedido(int clienteId, int vendedorId) {
        String mensaje = "";
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call Realizar_Pedido_Cliente(?, ?, ?)}")) {

            stmt.setInt(1, clienteId);
            stmt.setInt(2, vendedorId);
            stmt.registerOutParameter(3, Types.VARCHAR);

            stmt.execute();
            mensaje = stmt.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = "Error al realizar el pedido.";
        }
        return mensaje;
    }

    /**
     * OBTIENE LOS PEDIDOS DEL CLIENTE EN LA TABLA DE MIS PEDIDOS
     * @param vendedorId
     * @return
     */

    public List<Pedido> obtenerMisPedidos(int vendedorId) {
        List<Pedido> pedidos = new ArrayList<>();
        Connection connection = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        try {
            // Cambiar a OracleDBConnection
            connection = OracleDBConnection.getConnection();
            callableStatement = connection.prepareCall("{CALL Obtener_Pedidos_Cliente(?, ?)}");
            callableStatement.setInt(1, vendedorId);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.execute();

            resultSet = (ResultSet) callableStatement.getObject(2);

            while (resultSet.next()) {
                int idPedido = resultSet.getInt("ID Pedido");
                Date fecha = resultSet.getDate("Fecha");
                double total = resultSet.getDouble("Total");
                String estado = resultSet.getString("Estado");

                pedidos.add(new Pedido(idPedido, fecha, total, estado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return pedidos;
    }

    /**
     * METODO PARA CANCELAR UN PEDIDO DE LA TABLA MIS PEDIDOS DEL CLIENTE
     * @param idVenta
     * @return
     */

    public String cancelarPedido(int idVenta) {
        String mensaje = "";
        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = OracleDBConnection.getConnection();
            callableStatement = connection.prepareCall("{CALL Cancelar_Pedido(?, ?)}");
            callableStatement.setInt(1, idVenta); // Parámetro del ID de la venta
            callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR); // Mensaje de salida

            callableStatement.execute();
            mensaje = callableStatement.getString(2); // Obtener el mensaje de salida
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = "Error al conectar con la base de datos.";
        } finally {
            try {
                if (callableStatement != null) callableStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return mensaje;
    }

    /**
     * BUSCAR PEDIDOS DE LA TABLA MISPEDIDOS
     * @param clienteId
     * @param busqueda
     * @param estado
     * @param fecha
     * @return
     */

    public List<Pedido> buscarPedidos(int clienteId, String busqueda, String estado, Date fecha) {
        List<Pedido> pedidos = new ArrayList<>();
        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = OracleDBConnection.getConnection();
            String sql = "{CALL Buscar_MisPedidos(?, ?, ?, ?, ?)}";
            stmt = connection.prepareCall(sql);

            stmt.setInt(1, clienteId);
            stmt.setString(2, busqueda);
            stmt.setString(3, estado);
            if (fecha != null) {
                stmt.setDate(4, new java.sql.Date(fecha.getTime()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            stmt.registerOutParameter(5, OracleTypes.CURSOR);

            stmt.execute();
            rs = (ResultSet) stmt.getObject(5);

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("ID"),
                        rs.getDate("Fecha"),
                        rs.getDouble("Total"),
                        rs.getString("Estado")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pedidos;
    }

    /**
     * EXPORTAR LISTA DE VENDEDORES A PDF
     * @param filePath
     * @param vendedores Lista de vendedores a exportar
     * @throws DocumentException
     * @throws IOException
     */
    public void exportarVendedoresPDF(String filePath, List<Vendedor> vendedores) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        document.add(new Paragraph("Lista de Vendedores", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(Chunk.NEWLINE);

        // Crear tabla PDF con las columnas de vendedor
        PdfPTable table = new PdfPTable(5); // Número de columnas
        table.setWidthPercentage(100);

        // Añadir encabezados de columna
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Email");
        table.addCell("Nivel");
        table.addCell("Estado");

        // Llenar la tabla con los datos de vendedores
        for (Vendedor vendedor : vendedores) {
            table.addCell(String.valueOf(vendedor.getIdVendedor()));
            table.addCell(vendedor.getNombre());
            table.addCell(vendedor.getEmail());
            table.addCell(vendedor.getNivel());
            table.addCell(vendedor.getEstadoVendedor());
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }

    /**
     * EXPORTAR HISTORIAL DE PROMOCIONES A PDF
     * @param filePath
     * @param promociones Lista de promociones a exportar
     * @throws DocumentException
     * @throws IOException
     */
    public void exportarHistorialPromocionesPDF(String filePath, List<Promocion> promociones) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        document.add(new Paragraph("Historial de Promociones", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(Chunk.NEWLINE);

        // Crear tabla PDF con las columnas de promociones
        PdfPTable table = new PdfPTable(7); // Número de columnas
        table.setWidthPercentage(100);

        // Añadir encabezados de columna
        table.addCell("ID");
        table.addCell("Inicio");
        table.addCell("Fin");
        table.addCell("GOLD %");
        table.addCell("PLATE %");
        table.addCell("BRONZE %");
        table.addCell("PLATINO %");

        // Llenar la tabla con los datos de promociones
        for (Promocion promocion : promociones) {
            table.addCell(String.valueOf(promocion.getNombre()));
            table.addCell(promocion.getInicio().toString());
            table.addCell(promocion.getFin().toString());
            table.addCell(String.valueOf(promocion.getGoldPorcentaje()));
            table.addCell(String.valueOf(promocion.getPlatePorcentaje()));
            table.addCell(String.valueOf(promocion.getBronzePorcentaje()));
            table.addCell(String.valueOf(promocion.getPlatinoPorcentaje()));
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }

    /**
     * EXPORTAR LISTA DE PRODUCTOS A PDF
     * @param filePath Ruta del archivo de destino
     * @param productos Lista de productos a exportar
     * @throws DocumentException
     * @throws IOException
     */
    public void exportarProductosPDF(String filePath, List<Producto> productos) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        document.add(new Paragraph("Lista de Productos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(Chunk.NEWLINE);

        // Crear tabla PDF con las columnas de productos
        PdfPTable table = new PdfPTable(5); // Número de columnas
        table.setWidthPercentage(100);

        // Añadir encabezados de columna
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Categoría");
        table.addCell("Precio");
        table.addCell("Stock");

        // Llenar la tabla con los datos de productos
        for (Producto producto : productos) {
            table.addCell(String.valueOf(producto.getId()));
            table.addCell(producto.getNombre());
            table.addCell(producto.getCategoria());
            table.addCell(String.format("$ %.2f", producto.getPrecio()));
            table.addCell(String.valueOf(producto.getStock()));
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }


    /**
     * EXPORTAR LISTA DE PRODUCTOS CON COMISIÓN A PDF
     * @param filePath Ruta del archivo de destino
     * @param productos Lista de productos a exportar
     * @throws DocumentException
     * @throws IOException
     */
    public void exportarProductosConComisionPDF(String filePath, List<ProductoConComision> productos) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        document.add(new Paragraph("Lista de Productos con Comisión", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(Chunk.NEWLINE);

        // Crear tabla PDF con las columnas de productos
        PdfPTable table = new PdfPTable(6); // Número de columnas
        table.setWidthPercentage(100);

        // Añadir encabezados de columna
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Categoría");
        table.addCell("Precio");
        table.addCell("Comisión");
        table.addCell("Stock Disponible");

        // Llenar la tabla con los datos de productos
        for (ProductoConComision producto : productos) {
            table.addCell(String.valueOf(producto.getId()));
            table.addCell(producto.getNombre());
            table.addCell(producto.getCategoria());
            table.addCell(String.format("$ %.2f", producto.getPrecio()));
            table.addCell(String.format("%.2f%%", producto.getComisionPromedio()));
            table.addCell(String.valueOf(producto.getStock()));
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }


    /**
     * EXPORTAR LISTA DE AFILIADOS A PDF
     * @param filePath Ruta del archivo de destino
     * @param afiliados Lista de afiliados a exportar
     * @throws DocumentException
     * @throws IOException
     */
    public void exportarAfiliadosPDF(String filePath, List<Afiliado> afiliados) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Título
        document.add(new Paragraph("Lista de Afiliados", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(Chunk.NEWLINE);

        // Crear tabla PDF con las columnas de afiliados
        PdfPTable table = new PdfPTable(6); // Número de columnas
        table.setWidthPercentage(100);

        // Añadir encabezados de columna
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Nivel");
        table.addCell("Fecha de Afiliación");
        table.addCell("Ventas Totales");
        table.addCell("Comisiones Generadas");

        // Llenar la tabla con los datos de afiliados
        for (Afiliado afiliado : afiliados) {
            table.addCell(String.valueOf(afiliado.getId()));
            table.addCell(afiliado.getNombre());
            table.addCell(afiliado.getNivel());
            table.addCell(afiliado.getFechaAfiliacion().toString());
            table.addCell(String.format("$ %.2f", afiliado.getVentasTotales()));
            table.addCell(String.format("$ %.2f", afiliado.getComisionesGeneradas()));
        }

        // Agregar la tabla al documento
        document.add(table);
        document.close();
    }





    /**
     * MAIN PRUEBAS
     * @param args
     */

    public static void main(String[] args) {
        // Crear una instancia de VerificarLogin para probar el procedimiento
        VerificarLogin verificarLogin = new VerificarLogin();

        // Probar el procedimiento con un ejemplo
        /**String email = "jhojan@gmail.com";
        String contrasena = "aleja4523";
        String resultado = verificarLogin.loginAdministrador(email, contrasena);**/

        // Prueba login de vendedor
        /**String vendedorEmail = "samuel@gmail.com";
        int vendedorId = 1005319; // Ejemplo de ID de vendedor
        System.out.println(verificarLogin.loginVendedor(vendedorEmail, vendedorId));**/

        // Prueba login de CLIENTE
        String clienteEmail = "lore@gmail.com";
        int clienteId = 1099682; // Ejemplo de ID de vendedor
        System.out.println(verificarLogin.loginCliente(clienteEmail, clienteId));






        // Mostrar el resultado
        //System.out.println(resultado);
    }
}
