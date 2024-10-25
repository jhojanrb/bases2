package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    VerificarLogin vl = new VerificarLogin();

    @FXML
    void login(ActionEvent event) {

        String correo = txtCorreo.getText();
        String contrasenia = txtPassword.getText();

        VerificarLogin v = new VerificarLogin();

        // Llamar al método loginAdministrador y obtener la respuesta
        String resultado = v.loginAdministrador(correo, contrasenia);

        System.out.println(resultado);

        if (resultado != null && resultado.equals("Login exitoso. Bienvenido Administrador")) {
            try {
                // Mostrar mensaje de login correcto
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login correcto.");
                alert.showAndWait();

                // Obtener el Stage actual
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Cargar la nueva vista
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                // Establecer el título y cambiar la escena
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Manejo de excepciones
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la nueva vista.");
                alert.showAndWait();
            }
        } else {
            // Manejar el caso de error en el login
            Alert alert = new Alert(Alert.AlertType.WARNING, "Login incorrecto.");
            alert.showAndWait();
        }
    }

}
