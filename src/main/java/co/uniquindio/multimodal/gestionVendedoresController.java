package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Vendedor;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class gestionVendedoresController {

    @FXML
    private TextField busquedaField;

    @FXML
    private Label detalleComisionesLabel;

    @FXML
    private Label detalleEmailLabel;

    @FXML
    private Label detalleEstadoLabel;

    @FXML
    private Label detalleFechaRegistroLabel;

    @FXML
    private Label detalleIdLabel;

    @FXML
    private Label detalleNivelLabel;

    @FXML
    private Label detalleNombreLabel;

    @FXML
    private Button btnVolver;

    @FXML
    private Label detalleVentasTotalesLabel;

    @FXML
    private TableColumn<Vendedor, String> emailColumn;

    @FXML
    private TableColumn<Vendedor, String> estadoColumn;

    @FXML
    private ComboBox<?> estadoFiltroComboBox;

    @FXML
    private TableColumn<Vendedor, Integer> idColumn;

    @FXML
    private TableColumn<Vendedor, String> nivelColumn;

    @FXML
    private ComboBox<?> nivelFiltroComboBox;

    @FXML
    private TableColumn<Vendedor, String> nombreColumn;

    @FXML
    private TableView<Vendedor> vendedoresTable;

    @FXML
    void aprobarVendedor(ActionEvent event) {

    }

    @FXML
    void buscarVendedores(ActionEvent event) {

    }

    @FXML
    void cambiarNivel(ActionEvent event) {

    }

    @FXML
    void editarVendedor(ActionEvent event) {

    }

    @FXML
    void exportarListaVendedores(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

    }

    @FXML
    void rechazarVendedor(ActionEvent event) {

    }

    @FXML
    void volverAlDashboard(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Home Admin");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnVolver.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }
    }

    VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    public void initialize() {
        // Configuración de las columnas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVendedor"));
        nombreColumn.setCellValueFactory(cellData -> {
            Vendedor vendedor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(vendedor.getNombre() + " " + vendedor.getApellido());
        });
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nivelColumn.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estadoVendedor"));

        // Cargar los vendedores en la tabla
        cargarVendedores();
    }

    private void cargarVendedores() {
        ObservableList<Vendedor> vendedores = FXCollections.observableArrayList(verificarLogin.obtenerVendedores());
        vendedoresTable.setItems(vendedores);
    }

    }


