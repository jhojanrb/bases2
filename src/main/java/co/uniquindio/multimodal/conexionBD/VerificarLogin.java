package co.uniquindio.multimodal.conexionBD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public static void main(String[] args) {
        // Crear una instancia de VerificarLogin para probar el procedimiento
        VerificarLogin verificarLogin = new VerificarLogin();

        // Probar el procedimiento con un ejemplo
        /**String email = "jhojan@gmail.com";
        String contrasena = "aleja4523";
        String resultado = verificarLogin.loginAdministrador(email, contrasena);**/

        // Prueba login de vendedor
        String vendedorEmail = "samuel@gmail.com";
        int vendedorId = 1005319; // Ejemplo de ID de vendedor
        System.out.println(verificarLogin.loginVendedor(vendedorEmail, vendedorId));

        // Mostrar el resultado
        //System.out.println(resultado);
    }
}
