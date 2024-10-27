package co.uniquindio.multimodal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeVendedorController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label currentLevelLabel;

    @FXML
    private Label directAffiliatesLabel;

    @FXML
    private ProgressBar levelProgressBar;

    @FXML
    private Label monthCommissionsLabel;

    @FXML
    private Label monthSalesLabel;

    @FXML
    private Label nextLevelLabel;

    @FXML
    private ListView<?> notificationListView;

    @FXML
    private Label progressDetailsLabel;

    @FXML
    private TableView<?> recentSalesTable;

    @FXML
    private LineChart<?, ?> salesPerformanceChart;

    @FXML
    private Label vendorNameLabel;

    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
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


