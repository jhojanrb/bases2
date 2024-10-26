package co.uniquindio.multimodal;

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
                    cargarVista("home-view.fxml", "Home - Administrador");
                } else {
                    errorText.setText(resultado != null ? resultado : "Error: no se pudo completar el login.");
                }

            } else if (userType.equals("Vendedor")) {
                // Intento de login para Vendedor (requiere ID de vendedor)
                try {
                    int idVendedor = Integer.parseInt(passwordField.getText()); // Ejemplo simple para capturar ID como contraseña
                    resultado = verificarLogin.loginVendedor(correo, idVendedor);

                    // Evaluar respuesta para vendedor
                    if ("Login exitoso. Bienvenido".equals(resultado)) {
                        cargarVista("homeVendedor-view.fxml", "Home - Vendedor");
                    } else {
                        errorText.setText(resultado != null ? resultado : "Error: no se pudo completar el login.");
                    }

                } catch (NumberFormatException e) {
                    errorText.setText("El ID del vendedor debe ser numérico.");
                }
            }
        } catch (Exception e) {
            errorText.setText("Error en el proceso de login.");
            e.printStackTrace();
        }
    }

    // Método auxiliar para cargar y mostrar una vista específica
    private void cargarVista(String vista, String titulo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(vista));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Obtener la ventana actual
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
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
