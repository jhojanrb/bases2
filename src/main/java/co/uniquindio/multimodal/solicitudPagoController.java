package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.SolicitudDetalles;
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
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;



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
        // Configuración de las columnas de la tabla de solicitudes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("vendedor"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configurar el evento de doble clic en la fila de la tabla
        solicitudesTable.setRowFactory(tv -> {
            TableRow<SolicitudPago> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
                    SolicitudPago solicitudSeleccionada = row.getItem();
                    cargarDetallesSolicitud(solicitudSeleccionada.getId()); // Llamamos al método para cargar los detalles
                }
            });
            return row;
        });

        // Cargar las solicitudes de pago en la tabla
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

    // Formateador para convertir la fecha al formato requerido
    //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    void buscarSolicitudes(ActionEvent event) {
        String filtroNombreId = busquedaField.getText();
        String estado = estadoFiltroComboBox.getValue();
        java.sql.Date fechaSolicitud = (fechaInicioPicker.getValue() != null)
                ? java.sql.Date.valueOf(fechaInicioPicker.getValue())
                : null;

        // Ajuste del estado a null si es "Todos"
        if ("Todos".equals(estado)) {
            estado = null;
        }

        // Realizar la consulta a través de verificarLogin
        List<SolicitudPago> resultados = verificarLogin.consultarSolicitudesPago(filtroNombreId, estado, fechaSolicitud);
        ObservableList<SolicitudPago> data = FXCollections.observableArrayList(resultados);
        solicitudesTable.setItems(data);
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
        estadoFiltroComboBox.getSelectionModel().select("Todos");

        // Recargar todas las solicitudes sin filtros
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


    private void cargarDetallesSolicitud(int idSolicitud) {
        SolicitudDetalles detalles = verificarLogin.obtenerDetallesSolicitud(idSolicitud); // Llama al método en VerificarLogin

        // Verificar que los detalles no estén vacíos antes de cargar
        if (detalles != null) {
            detalleVendedorLabel.setText("Vendedor: " + detalles.getVendedor());
            detalleNivelLabel.setText("Nivel: " + detalles.getNivel());
            detalleVentasLabel.setText("Ventas Recientes: $" + detalles.getVentasRecientes());
            detalleComisionesLabel.setText("Comisiones Generadas: $" + detalles.getComisionesGeneradas());
            detalleCumplimientoMetasLabel.setText("Cumplimiento de Metas: " + detalles.getCumplimientoMetas());
        } else {
            mostrarAlerta("Error", "No se pudieron cargar los detalles de la solicitud.");
        }
    }




}
