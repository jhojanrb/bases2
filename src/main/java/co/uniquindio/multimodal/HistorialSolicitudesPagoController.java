package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.SolicitudPago;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class HistorialSolicitudesPagoController {

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnExportar;

    @FXML
    private Button btnGenerarInforme;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField busquedaField;

    @FXML
    private TextArea comentariosArea;

    @FXML
    private Label detalleComisionesLabel;

    @FXML
    private Label detalleCumplimientoMetasLabel;

    @FXML
    private Label detalleEstadoLabel;

    @FXML
    private Label detalleFechaResolucionLabel;

    @FXML
    private Label detalleFechaSolicitudLabel;

    @FXML
    private Label detalleIdLabel;

    @FXML
    private Label detalleMontoLabel;

    @FXML
    private Label detalleNivelLabel;

    @FXML
    private Label detalleVendedorLabel;

    @FXML
    private Label detalleVentasLabel;

    @FXML
    private TableColumn<SolicitudPago, String> estadoColumn;

    @FXML
    private ComboBox<?> estadoFiltroComboBox;

    @FXML
    private TableColumn<SolicitudPago, Date> fechaColumn;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private TableColumn<SolicitudPago, String> fechaResolucionColumn;

    @FXML
    private TableView<SolicitudPago> historialSolicitudesTable;

    @FXML
    private TableColumn<SolicitudPago, Integer> idColumn;

    @FXML
    private TableColumn<SolicitudPago, Double> montoColumn;

    @FXML
    private TableColumn<SolicitudPago, String> vendedorColumn;

    private VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    public void cargarHistorialSolicitudes() {
        // Obtener el historial de solicitudes del método de VerificarLogin
        List<SolicitudPago> historial = verificarLogin.obtenerHistorialSolicitudesPago();
        ObservableList<SolicitudPago> data = FXCollections.observableArrayList(historial);
        historialSolicitudesTable.setItems(data);
    }

    @FXML
    public void initialize() {
        // Configurar las columnas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("vendedor"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        fechaResolucionColumn.setCellValueFactory(new PropertyValueFactory<>("comentarioRechazo")); // Muestra el comentario de rechazo en lugar de la fecha de resolución

        // Cargar el historial de solicitudes
        cargarHistorialSolicitudes();
    }

    @FXML
    void buscarSolicitudes(ActionEvent event) {

    }

    @FXML
    void exportarHistorial(ActionEvent event) {

    }

    @FXML
    void generarInforme(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

    }

    @FXML
    void volverAGestionSolicitudes(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("solicitudPago-view.fxml"));
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

}