package co.uniquindio.multimodal.conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * OBTENER SOLICITUDES PENDIENTES DE LOS VENDEDORES
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
                        rs.getInt("id_vendedor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("estado_vendedor") // Añade el nombre del estado como String
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
