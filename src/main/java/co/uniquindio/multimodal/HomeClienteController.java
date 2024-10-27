package co.uniquindio.multimodal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeClienteController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label clientNameLabel;

    @FXML
    private TableView<?> recentOrdersTable;

    @FXML
    private ListView<?> specialOffersListView;

    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            //JOptionPane.showMessageDialog(null, "");
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener la ventana actual y establecer la nueva escena
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }
    }
}

