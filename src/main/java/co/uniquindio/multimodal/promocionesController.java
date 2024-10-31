package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Promocion;
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
import java.time.LocalDate;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

public class promocionesController {

    @FXML
    private TableColumn<Promocion, Float> bronzeColumna;

    @FXML
    private TableColumn<Promocion, Float> bronzeHistorialColumna;

    @FXML
    private Button btnCerrar;

    @FXML
    private Button btnCrearPromocion;

    @FXML
    private Button btnEditarPromo;

    @FXML
    private Button btnEliminar;

    @FXML
    private TextField descuentoBronzeField;

    @FXML
    private TextField descuentoGoldField;

    @FXML
    private TextField descuentoPlateField;

    @FXML
    private TextField descuentoPlatinoField;

    @FXML
    private DatePicker fechaFinDatePicker;

    @FXML
    private DatePicker fechaInicioDatePicker;

    @FXML
    private TableColumn<Promocion, java.sql.Date> finColumna;

    @FXML
    private TableColumn<Promocion, java.sql.Date> finHistorialColumna;

    @FXML
    private TableColumn<Promocion, Float> goldColumna;

    @FXML
    private TableColumn<Promocion, Float> goldHistorialColumna;

    @FXML
    private TableView<Promocion> historialPromocionesTable;

    @FXML
    private TableColumn<Promocion, java.sql.Date> inicioColumna;

    @FXML
    private TableColumn<Promocion, java.sql.Date> inicioHistorialColumna;

    @FXML
    private TableColumn<Promocion, String> nombreColumna;

    @FXML
    private TableColumn<Promocion, String> nombreHistorialColumna;

    @FXML
    private TextField nombrePromocionField;

    @FXML
    private TableColumn<Promocion, Float> plateColumna;

    @FXML
    private TableColumn<Promocion, Float> plateHistorialColumna;

    @FXML
    private TableColumn<Promocion, Float> platinoColumna;

    @FXML
    private TableColumn<Promocion, Float> platinoHistorialColumna;

    @FXML
    private TableView<Promocion> promocionesActivasTable;

    private VerificarLogin verificarLogin = new VerificarLogin();

    private Promocion promocionSeleccionada;

    private void cargarDatosPromocion(Promocion promocion) {
        // Cargar los datos de la promoción seleccionada en los campos de entrada
        nombrePromocionField.setText(promocion.getNombre());
        fechaInicioDatePicker.setValue(promocion.getInicio().toLocalDate());
        fechaFinDatePicker.setValue(promocion.getFin().toLocalDate());
        descuentoGoldField.setText(String.valueOf(promocion.getGoldPorcentaje()));
        descuentoPlateField.setText(String.valueOf(promocion.getPlatePorcentaje()));
        descuentoBronzeField.setText(String.valueOf(promocion.getBronzePorcentaje()));
        descuentoPlatinoField.setText(String.valueOf(promocion.getPlatinoPorcentaje()));
    }

    @FXML
    public void initialize() {

        // Configurar para seleccionar una promoción en la tabla
        promocionesActivasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                promocionSeleccionada = newSelection;
            }
        });

        promocionesActivasTable.setRowFactory(tv -> {
            TableRow<Promocion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    promocionSeleccionada = row.getItem();
                    cargarDatosPromocion(promocionSeleccionada);
                }
            });
            return row;
        });

        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        inicioColumna.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        finColumna.setCellValueFactory(new PropertyValueFactory<>("fin"));
        goldColumna.setCellValueFactory(new PropertyValueFactory<>("goldPorcentaje"));
        plateColumna.setCellValueFactory(new PropertyValueFactory<>("platePorcentaje"));
        bronzeColumna.setCellValueFactory(new PropertyValueFactory<>("bronzePorcentaje"));
        platinoColumna.setCellValueFactory(new PropertyValueFactory<>("platinoPorcentaje"));

        // Configuración de columnas para historial de promociones
        nombreHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        inicioHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        finHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("fin"));
        goldHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("goldPorcentaje"));
        plateHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("platePorcentaje"));
        bronzeHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("bronzePorcentaje"));
        platinoHistorialColumna.setCellValueFactory(new PropertyValueFactory<>("platinoPorcentaje"));

        // Cargar datos en tablas
        cargarPromociones();
        cargarHistorialPromociones();
    }

    public void cargarPromociones() {
        List<Promocion> listaPromociones = verificarLogin.obtenerPromocionesActivas();
        if (listaPromociones.isEmpty()) {
            System.out.println("No se encontraron promociones activas.");
        } else {
            System.out.println("Promociones encontradas: " + listaPromociones.size());
        }

        ObservableList<Promocion> promociones = FXCollections.observableArrayList(listaPromociones);
        promocionesActivasTable.setItems(promociones);
    }

    public void cargarHistorialPromociones() {
        ObservableList<Promocion> historialPromociones = FXCollections.observableArrayList(verificarLogin.obtenerHistorialPromociones());
        historialPromocionesTable.setItems(historialPromociones);
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
            System.out.println("Error al cargar la interfaz Home Admin");
        }
    }



    @FXML
    void crearPromo(ActionEvent event) {

        // Obtener los valores de los campos de entrada
        String idPromocion = nombrePromocionField.getText();
        LocalDate fechaInicio = fechaInicioDatePicker.getValue();
        LocalDate fechaFin = fechaFinDatePicker.getValue();
        float descuentoGold = Float.parseFloat(descuentoGoldField.getText());
        float descuentoPlate = Float.parseFloat(descuentoPlateField.getText());
        float descuentoBronze = Float.parseFloat(descuentoBronzeField.getText());
        float descuentoPlatino = Float.parseFloat(descuentoPlatinoField.getText());

        // Validar que las fechas y los campos de descuento no estén vacíos
        if (idPromocion.isEmpty() || fechaInicio == null || fechaFin == null) {
            mostrarAlerta("Error", "Por favor completa todos los campos de la promoción.");
            return;
        }

        if (fechaInicio.isBefore(LocalDate.now()) || fechaFin.isBefore(LocalDate.now()) || fechaInicio.isAfter(fechaFin)) {
            mostrarAlerta("Error", "Por favor ingrese una fecha correcta");
            return;
        }


        // Convertir LocalDate a Date (java.sql.Date)
        Date sqlFechaInicio = Date.valueOf(fechaInicio);
        Date sqlFechaFin = Date.valueOf(fechaFin);

        // Llamar al método para crear la promoción
        boolean success = verificarLogin.crearPromocion(idPromocion, sqlFechaInicio, sqlFechaFin, descuentoGold, descuentoPlate, descuentoBronze, descuentoPlatino);

        // Mostrar un mensaje de éxito o error
        if (success) {
            mostrarAlerta("Éxito", "La promoción ha sido creada exitosamente.");
            limpiarCampos();
            cargarPromociones();
        } else {
            mostrarAlerta("Error", "Hubo un problema al crear la promoción.");
        }

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombrePromocionField.clear();
        fechaInicioDatePicker.setValue(null);
        fechaFinDatePicker.setValue(null);
        descuentoGoldField.clear();
        descuentoPlateField.clear();
        descuentoBronzeField.clear();
        descuentoPlatinoField.clear();
    }

    @FXML
    void eliminar(ActionEvent event) {

        if (promocionSeleccionada == null) {
            mostrarAlerta("Error", "Seleccione una promoción para eliminar.");
            return;
        }

        // Confirmar eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar esta promoción?");

        // Si el usuario confirma
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = verificarLogin.eliminarPromocion(promocionSeleccionada.getNombre());

                // Mostrar un mensaje de éxito o error
                if (success) {
                    mostrarAlerta("Éxito", "La promoción ha sido eliminada exitosamente.");
                    actualizarTabla();
                } else {
                    mostrarAlerta("Error", "Hubo un problema al eliminar la promoción.");
                }
            }
        });

    }

    @FXML
    void editarPromocion(ActionEvent event) {

        if (promocionSeleccionada == null) {
            mostrarAlerta("Error", "Seleccione una promoción para editar.");
            return;
        }

        // Obtener los valores de los campos de entrada
        String idPromocion = promocionSeleccionada.getNombre();
        LocalDate fechaInicio = fechaInicioDatePicker.getValue();
        LocalDate fechaFin = fechaFinDatePicker.getValue();
        float descuentoGold = Float.parseFloat(descuentoGoldField.getText());
        float descuentoPlate = Float.parseFloat(descuentoPlateField.getText());
        float descuentoBronze = Float.parseFloat(descuentoBronzeField.getText());
        float descuentoPlatino = Float.parseFloat(descuentoPlatinoField.getText());

        // Convertir LocalDate a Date (java.sql.Date)
        Date sqlFechaInicio = Date.valueOf(fechaInicio);
        Date sqlFechaFin = Date.valueOf(fechaFin);

        // Llamar al método para editar la promoción
        boolean success = verificarLogin.editarPromocion(idPromocion, sqlFechaInicio, sqlFechaFin, descuentoGold, descuentoPlate, descuentoBronze, descuentoPlatino);

        // Mostrar un mensaje de éxito o error
        if (success) {
            mostrarAlerta("Éxito", "La promoción ha sido editada exitosamente.");
            actualizarTabla();
        } else {
            mostrarAlerta("Error", "Hubo un problema al editar la promoción.");
        }

    }

    private void actualizarTabla() {
        // Recarga las promociones activas en la tabla
        ObservableList<Promocion> promociones = FXCollections.observableArrayList(verificarLogin.obtenerPromocionesActivas());
        promocionesActivasTable.setItems(promociones);
    }
}
