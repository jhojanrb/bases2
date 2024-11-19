package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.SolicitudPago;
import co.uniquindio.multimodal.conexionBD.SolicitudDetalles;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
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
    private ComboBox<String> estadoFiltroComboBox;

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

    private void cargarDetallesSolicitud(int solicitudId) {
        // Llamar al método que obtiene los detalles de la solicitud
        SolicitudDetalles detalles = verificarLogin.obtenerDetallesSolicitud(solicitudId);

        // Verificar que `detalles` no sea null y cargar la información en los labels
        if (detalles != null) {
            detalleIdLabel.setText("ID: " + detalles.getIdSolicitud());
            detalleVendedorLabel.setText("Vendedor: " + detalles.getVendedor());
            detalleFechaSolicitudLabel.setText("Fecha de Solicitud: 05/11/2024" );
            detalleMontoLabel.setText("Monto: " + detalles.getVentasRecientes());
            detalleEstadoLabel.setText("Estado: " + detalles.getEstado());
            detalleFechaResolucionLabel.setText("Fecha de Resolución: 20/11/2024" );
            detalleNivelLabel.setText("Nivel del Vendedor: " + detalles.getNivel());
            detalleVentasLabel.setText("Ventas del Período: " + detalles.getVentasRecientes());
            detalleComisionesLabel.setText("Comisiones Generadas: " + detalles.getComisionesGeneradas());
            detalleCumplimientoMetasLabel.setText("Cumplimiento de Metas: " + detalles.getCumplimientoMetas());
            comentariosArea.setText(detalles.getComentarioRechazo());
        }
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

        // Configurar evento de doble clic para la tabla
        historialSolicitudesTable.setRowFactory(tv -> {
            TableRow<SolicitudPago> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SolicitudPago solicitud = row.getItem();
                    cargarDetallesSolicitud(solicitud.getId());
                }
            });
            return row;
        });
    }


    @FXML
    void exportarHistorial(ActionEvent event) {

        // Ruta de guardado
        String rutaArchivo = "C:\\2024-2\\bases 2\\PROYECTO\\reportes\\Administrador\\historial_solicitudes_pago.pdf";

        try {
            // Llama al método de exportación en VerificarLogin
            VerificarLogin verificarLogin = new VerificarLogin();
            verificarLogin.exportarHistorialPDF(rutaArchivo);

            // Mostrar alerta de éxito
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Exportación Exitosa");
            alerta.setHeaderText(null);
            alerta.setContentText("El historial de solicitudes de pago se ha exportado correctamente.");

            // Agregar botones personalizados
            ButtonType abrirArchivo = new ButtonType("Ir al Archivo");
            ButtonType masTarde = new ButtonType("Más Tarde", ButtonBar.ButtonData.CANCEL_CLOSE);
            alerta.getButtonTypes().setAll(abrirArchivo, masTarde);

            // Manejar la respuesta del usuario
            alerta.showAndWait().ifPresent(response -> {
                if (response == abrirArchivo) {
                    try {
                        // Abrir el archivo en el explorador
                        File archivo = new File("C:\\2024-2\\bases 2\\PROYECTO\\reportes\\Administrador");
                        if (archivo.exists()) {
                            Desktop.getDesktop().open(archivo);
                        } else {
                            mostrarAlerta("Error", "La carpeta de destino no existe.", Alert.AlertType.ERROR);
                        }
                    } catch (IOException e) {
                        mostrarAlerta("Error", "No se pudo abrir la carpeta de destino.", Alert.AlertType.ERROR);
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            // Manejar errores en la exportación
            mostrarAlerta("Error", "Ocurrió un error al exportar el historial: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    void generarInforme(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {
        busquedaField.clear();
        fechaInicioPicker.setValue(null);
        estadoFiltroComboBox.getSelectionModel().select("Todos");

        // Cargar todo el historial sin filtros
        buscarSolicitudes(null);
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

    public void buscarSolicitudes(ActionEvent actionEvent) {

        String filtroNombreId = busquedaField.getText().trim();
        String estado = estadoFiltroComboBox.getValue();
        java.sql.Date fechaSolicitud = (fechaInicioPicker.getValue() != null)
                ? java.sql.Date.valueOf(fechaInicioPicker.getValue())
                : null;

        // Si el estado es "Todos", lo establecemos como null para que el procedimiento lo maneje
        if ("Todos".equals(estado)) {
            estado = null;
        }

        // Consultar el historial de solicitudes a través de verificarLogin
        List<SolicitudPago> resultados = verificarLogin.consultarHistorialSolicitudes(
                filtroNombreId.isEmpty() ? null : filtroNombreId, estado, fechaSolicitud);
        ObservableList<SolicitudPago> data = FXCollections.observableArrayList(resultados);
        historialSolicitudesTable.setItems(data);
    }
}
