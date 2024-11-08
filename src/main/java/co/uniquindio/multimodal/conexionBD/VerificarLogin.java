package co.uniquindio.multimodal.conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;


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
     * CARGAR LOS DETALLES EN LOS LABELS
     * @param idProducto
     * @return
     */

    public ProductoDetalles obtenerDetallesProducto(int idProducto) {
        ProductoDetalles detalles = null;
        try (Connection connection = getConnection();
             CallableStatement stmt = connection.prepareCall("{call obtener_detalles_producto(?, ?)}")) {

            stmt.setInt(1, idProducto);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            if (rs.next()) {
                detalles = new ProductoDetalles(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("descripcion"),
                        rs.getString("ruta_imagen")  // Nueva columna para la ruta de la imagen
                );
            }
            rs.close();
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
