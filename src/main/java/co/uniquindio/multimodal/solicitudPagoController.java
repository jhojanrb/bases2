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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class solicitudPagoController {

    @FXML
    private Button btnAprobar;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnCerrar;

    @FXML
    private Button btnGenerarInforme;

    @FXML
    private Button btnHistorial;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnRechazar;

    @FXML
    private TextField busquedaField;

    @FXML
    private TextArea comentarioRechazoArea;

    @FXML
    private Label detalleComisionesLabel;

    @FXML
    private Label detalleCumplimientoMetasLabel;

    @FXML
    private Label detalleNivelLabel;

    @FXML
    private Label detalleVendedorLabel;

    @FXML
    private Label detalleVentasLabel;

    @FXML
    private TableColumn<SolicitudPago, String> estadoColumn;

    @FXML
    private ComboBox<String> estadoFiltroComboBox;

    @FXML
    private TableColumn<SolicitudPago, Date> fechaColumn;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private TableColumn<SolicitudPago, Integer> idColumn;

    @FXML
    private TableColumn<SolicitudPago, Double> montoColumn;

    @FXML
    private TableView<SolicitudPago> solicitudesTable;

    @FXML
    private TableColumn<SolicitudPago, String> vendedorColumn;

    private VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    public void initialize() {
        // Configuración de las columnas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("vendedor"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Cargar las solicitudes de pago
        cargarSolicitudes();
    }

    private void cargarSolicitudes() {
        ObservableList<SolicitudPago> solicitudes = FXCollections.observableArrayList(verificarLogin.obtenerSolicitudesPago());
        solicitudesTable.setItems(solicitudes);
    }

    @FXML
    void aprobarSolicitud(ActionEvent event) {

        SolicitudPago solicitudSeleccionada = solicitudesTable.getSelectionModel().getSelectedItem();

        if (solicitudSeleccionada != null) {
            int solicitudId = solicitudSeleccionada.getId();
            System.out.println("ID de Solicitud Seleccionada: " + solicitudId); // Agregar impresión para depuración

            boolean aprobado = verificarLogin.aprobarSolicitudPago(solicitudId);

            if (aprobado) {
                mostrarAlerta("Éxito", "La solicitud de pago ha sido aprobada.");
                solicitudSeleccionada.setEstado("Aprobado");
                cargarSolicitudes();
                solicitudesTable.refresh();
            } else {
                mostrarAlerta("Error", "No se pudo aprobar la solicitud de pago.");
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione una solicitud para aprobar.");
        }

    }

    // Método para mostrar alertas en la interfaz
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void buscarSolicitudes(ActionEvent event) {

    }

    @FXML
    void cerrarVentana(ActionEvent event) {

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
            Stage currentStage = (Stage) btnCerrar.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz home admin.");
        }
    }



    @FXML
    void filtrar(ActionEvent event) {

    }

    @FXML
    void generarInforme(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

        busquedaField.clear();
        fechaInicioPicker.setValue(null);
        fechaFinPicker.setValue(null);
        estadoFiltroComboBox.getSelectionModel().select("Todos");
        cargarSolicitudes();



    }

    @FXML
    void rechazarSolicitud(ActionEvent event) {

        // Obtener la solicitud seleccionada en la TableView
        SolicitudPago solicitudSeleccionada = solicitudesTable.getSelectionModel().getSelectedItem();
        if (solicitudSeleccionada != null) {
            // Obtener el comentario de rechazo desde el área de texto
            String comentarioRechazo = comentarioRechazoArea.getText();

            // Validar que el comentario no esté vacío
            if (comentarioRechazo.isEmpty()) {
                mostrarAlerta("Advertencia", "Por favor ingrese un comentario de rechazo.");
                return;
            }

            // Llamar al método de VerificarLogin para rechazar la solicitud
            boolean exito = verificarLogin.rechazarSolicitudPago(solicitudSeleccionada.getId(), comentarioRechazo);

            if (exito) {
                mostrarAlerta("Éxito", "La solicitud ha sido rechazada correctamente.");
                cargarSolicitudes();  // Refrescar la lista de solicitudes en la tabla
            } else {
                mostrarAlerta("Error", "No se pudo rechazar la solicitud.");
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor seleccione una solicitud para rechazar.");
        }

    }

    @FXML
    void verHistorialSolicitudes(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("historialSolicitudesPago-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Historial");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnHistorial.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }

    }

}
