package co.uniquindio.multimodal.conexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


public class OracleDBConnection {

    // URL de conexión a la base de datos Oracle en localhost:1521/PROYECTO BD2.
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

    public static void main(String[] args) {
        try {
            // Obtener la conexión y realizar alguna operación
            Connection connection = OracleDBConnection.getConnection();

            // Cierra la conexión después de usarla
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        prueba();
    }

    public static void prueba(){
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Obtener la conexión
            connection = getConnection();

            // Crear un Statement
            stmt = connection.createStatement();

            // Ejecutar la consulta para obtener las tablas del esquema del usuario
            String sql = "SELECT table_name FROM user_tables";
            rs = stmt.executeQuery(sql);

            // Mostrar las tablas
            System.out.println("Tablas en el esquema PROYECTO:");
            while (rs.next()) {
                System.out.println(rs.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Cerrar ResultSet, Statement y Connection
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}

