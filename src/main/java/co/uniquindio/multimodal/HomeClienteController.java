package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeClienteController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label clientNameLabel;

    private VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    private TableView<?> recentOrdersTable;

    @FXML
    private ListView<?> specialOffersListView;

    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Login");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnCerrarSesion.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }
    }
}

