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
        // Obtener los valores ingresados por el usuario
        String correo = usernameField.getText();
        String contrasena = passwordField.getText();

        // Validar que los campos no estén vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            errorText.setText("Por favor, complete todos los campos.");
            return;
        }

        if(userTypeComboBox.getValue() == null){
            errorText.setText("Por favor, seleccione el tipo de usuario");
            return;
        }

        // Crear una instancia de VerificarLogin y llamar al método de verificación
        VerificarLogin verificarLogin = new VerificarLogin();
        String resultado = verificarLogin.loginAdministrador(correo, contrasena);

        // Evaluar la respuesta del procedimiento almacenado
        if ("Login exitoso. Bienvenido Administrador".equals(resultado)) {
            try {
                // Cargar y mostrar la nueva interfaz si el login es exitoso
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) usernameField.getScene().getWindow(); // Obtener la ventana actual
                stage.setScene(scene);
                stage.setTitle("Home");
                stage.show();
            } catch (IOException e) {
                errorText.setText("Error al cargar la interfaz de inicio.");
                e.printStackTrace();
            }
        } else {
            // Mostrar el mensaje de error si el login falla
            errorText.setText(resultado != null ? resultado : "Error: no se pudo completar el login.");
        }
    }

    // Método para el enlace "¿Olvidó su contraseña?"
    @FXML
    void forgot(ActionEvent event) {
        errorText.setText("Función de recuperación de contraseña no implementada aún.");
    }
}
