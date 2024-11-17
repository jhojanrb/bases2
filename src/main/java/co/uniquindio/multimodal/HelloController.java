package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.SessionData;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class HelloController {

    @FXML
    private ComboBox<String> userTypeComboBox; // ComboBox para tipo de usuario

    @FXML
    private TextField usernameField; // Campo de texto para el nombre de usuario

    @FXML
    private PasswordField passwordField; // Campo de texto para la contraseña

    @FXML
    private Text errorText; // Texto para mostrar errores

    // Método de login
    @FXML
    void login(ActionEvent event) {
        String correo = usernameField.getText();
        String contrasena = passwordField.getText();

        // Validar que los campos no estén vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            errorText.setText("Por favor, complete todos los campos.");
            return;
        }

        // Validar selección en el ComboBox
        String userType = userTypeComboBox.getValue();
        if (userType == null) {
            errorText.setText("Por favor, seleccione el tipo de usuario");
            return;
        }

        // Crear instancia de VerificarLogin
        VerificarLogin verificarLogin = new VerificarLogin();
        String resultado;

        try {
            // Verificar el tipo de usuario seleccionado
            if (userType.equals("Administrador")) {
                // Intento de login para Administrador
                resultado = verificarLogin.loginAdministrador(correo, contrasena);

                // Evaluar respuesta para administrador
                if ("Login exitoso. Bienvenido Administrador".equals(resultado)) {
                    cargarVista("home-view.fxml", "Home - Administrador", null, null, null, null);
                } else {
                    errorText.setText(resultado != null ? resultado : "Error: no se pudo completar el login.");
                }

            } else if (userType.equals("Vendedor")) {
                // Intento de login para Vendedor (requiere ID de vendedor)
                try {
                    int idVendedor = Integer.parseInt(passwordField.getText());
                    resultado = verificarLogin.loginVendedor(correo, idVendedor);

                    if ("Login exitoso. Bienvenido".equals(resultado)) {

                        SessionData.setVendorData(correo, idVendedor);
                        cargarVista("homeVendedor-view.fxml", "Home - Vendedor", correo, idVendedor, null, null);  // Pasa el nombre del vendedor
                    } else {
                        errorText.setText(resultado != null ? resultado : "Error: no se pudo completar el login.");
                    }
                } catch (NumberFormatException e) {
                    errorText.setText("El ID del vendedor debe ser numérico.");
                }

            } else if (userType.equals("Cliente")) {
                // Intento de login para Cliente (requiere ID de cliente)
                try {
                    int idCliente = Integer.parseInt(passwordField.getText()); // Ejemplo simple para capturar ID como contraseña
                    resultado = verificarLogin.loginCliente(correo, idCliente);

                    String nombreCliente = verificarLogin.obtenerNombreClienteVista(idCliente);

                    // Evaluar respuesta para cliente
                    if ("Login exitoso. Bienvenido cliente".equals(resultado)) {
                        SessionData.setClienteData(correo, idCliente);
                        cargarVista("homeCliente-view.fxml", "Home - Cliente", null, null, nombreCliente, idCliente);
                    } else {
                        errorText.setText(resultado != null ? resultado : "Error: no se pudo completar el login.");
                    }

                } catch (NumberFormatException e) {
                    errorText.setText("El ID del cliente debe ser numérico.");
                }
            }

        } catch (Exception e) {
            errorText.setText("Error en el proceso de login.");
            e.printStackTrace();
        }
    }


    // Método auxiliar para cargar y mostrar una vista específica
    private void cargarVista(String vista, String titulo, String nombreVendedor, Integer idVendedor, String nombreUsuario, Integer idUsuario) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(vista));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtén el controlador de la vista cargada
            if (vista.equals("homeVendedor-view.fxml")) {
                HomeVendedorController vendedorController = fxmlLoader.getController();
                // Solo pasa datos de vendedor si nombreVendedor e idVendedor son válidos
                if (nombreVendedor != null && idVendedor != null) {
                    vendedorController.setVendorData(nombreVendedor, idVendedor);
                }
            }else if (vista.equals("redAfiliadosVendedor-view.fxml")) {  // Cuando la vista es de catálogo
                RedAfiliadosVendedorController afiliadosVendedorController = fxmlLoader.getController();
                if (idVendedor != null) {
                    afiliadosVendedorController.setIdVendedor(idVendedor);  // Pasa el idVendedor
                }
            } else if (vista.equals("homeCliente-view.fxml")) {
                // Cuando la vista es del cliente
                HomeClienteController clienteController = fxmlLoader.getController();
                if (nombreUsuario != null && idUsuario != null) {
                    clienteController.setClientData(nombreUsuario, idUsuario);
                }
            }


            // Ajustar la escena al tamaño de la pantalla completa
            Stage newStage = new Stage();
            newStage.setTitle(titulo);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.show();

            // Cierra la ventana actual de inicio de sesión
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            errorText.setText("Error al cargar la interfaz de " + titulo.toLowerCase() + ".");
            e.printStackTrace();
        }
    }


    // Método para el enlace "¿Olvidó su contraseña?"
    @FXML
    void forgot(ActionEvent event) {
        errorText.setText("Función de recuperación de contraseña no implementada aún.");
    }
}
