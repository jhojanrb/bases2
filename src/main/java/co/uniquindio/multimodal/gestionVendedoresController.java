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
import java.util.List;

public class gestionVendedoresController {

    @FXML
    private TextField busquedaField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

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
    private ComboBox<String> estadoFiltroComboBox;

    @FXML
    private ComboBox<String> nivelRegistroComboBox;

    @FXML
    private ComboBox<String> estadoRegistroComboBox;

    @FXML
    private TableColumn<Vendedor, Integer> idColumn;

    @FXML
    private TableColumn<Vendedor, String> nivelColumn;

    @FXML
    private ComboBox<String> nivelFiltroComboBox;

    @FXML
    private TableColumn<Vendedor, String> nombreColumn;

    @FXML
    private TableView<Vendedor> vendedoresTable;

    @FXML
    void registrarVendedor(ActionEvent event) {

        String nombre = nombreField.getText();
        String apellido = apellidoField.getText(); // Suponiendo que puedes agregar un campo de apellido si es necesario
        String email = emailField.getText();
        String contrasena = passwordField.getText();
        String confirmarContrasena = confirmPasswordField.getText();
        String nivel = nivelRegistroComboBox.getValue();
        String estado = estadoRegistroComboBox.getValue();

        // Validaciones básicas
        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || nivel == null || estado == null) {
            mostrarAlerta("Error", "Por favor completa todos los campos obligatorios.");
            return;
        }
        if (!contrasena.equals(confirmarContrasena)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden.");
            return;
        }

        boolean success = verificarLogin.registrarVendedor(nombre, apellido, email, contrasena, nivel, estado);

        if (success) {
            mostrarAlerta("Éxito", "Vendedor registrado exitosamente.");
            limpiarFormularioRegistro();
            cargarVendedores();
        } else {
            mostrarAlerta("Error", "Error al registrar el vendedor.");
        }

    }

    /**
     * Muestra un cuadro de diálogo para seleccionar un nuevo nivel
     */
    private String mostrarDialogoSeleccionNivel() {
        ChoiceDialog<String> dialogo = new ChoiceDialog<>("Bronze", "Bronze", "Plate", "Platino", "Gold");
        dialogo.setTitle("Seleccionar Nivel");
        dialogo.setHeaderText("Seleccione el nuevo nivel para el vendedor:");
        return dialogo.showAndWait().orElse(null);
    }

    /**
     * Muestra un cuadro de diálogo para seleccionar un nuevo estado
     */
    private String mostrarDialogoSeleccionEstado() {
        ChoiceDialog<String> dialogo = new ChoiceDialog<>("Activo", "Activo", "Inactivo", "Suspendido", "Pendiente", "Rechazado");
        dialogo.setTitle("Seleccionar Estado");
        dialogo.setHeaderText("Seleccione el nuevo estado para el vendedor:");
        return dialogo.showAndWait().orElse(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarFormularioRegistro() {
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        nivelRegistroComboBox.getSelectionModel().clearSelection();
        estadoRegistroComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void buscarVendedores(ActionEvent event) {

        String nombre = busquedaField.getText();
        String nivel = nivelFiltroComboBox.getValue();
        String estado = estadoFiltroComboBox.getValue();

        // Convertir "Todos" a NULL para que el procedimiento ignore estos filtros
        if ("Todos".equals(nivel)) nivel = null;
        if ("Todos".equals(estado)) estado = null;

        List<Vendedor> vendedores = verificarLogin.buscarVendedores(nombre, nivel, estado);
        ObservableList<Vendedor> data = FXCollections.observableArrayList(vendedores);
        vendedoresTable.setItems(data);
    }

    @FXML
    void cambiarNivel(ActionEvent event) {

        Vendedor vendedorSeleccionado = vendedoresTable.getSelectionModel().getSelectedItem();
        if (vendedorSeleccionado != null) {
            String nuevoNivel = mostrarDialogoSeleccionNivel();
            if (nuevoNivel != null) {
                boolean exito = verificarLogin.actualizarNivelVendedor(vendedorSeleccionado.getIdVendedor(), nuevoNivel);
                if (exito) {
                    mostrarAlerta("Éxito", "Nivel actualizado correctamente.");
                    cargarVendedores();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar el nivel del vendedor.");
                }
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un vendedor para cambiar el nivel.");
        }

    }

    @FXML
    void editarVendedor(ActionEvent event) {

    }

    @FXML
    void exportarListaVendedores(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

        busquedaField.clear();
        nivelFiltroComboBox.getSelectionModel().select("Todos");
        estadoFiltroComboBox.getSelectionModel().select("Todos");
        cargarVendedores();

    }

    private Vendedor vendedorSeleccionado;

    @FXML
    void eliminarVendedor(ActionEvent event) {



        Vendedor vendedorSeleccionado = vendedoresTable.getSelectionModel().getSelectedItem();
        if (vendedorSeleccionado != null) {
            boolean exito = verificarLogin.eliminarVendedor(vendedorSeleccionado.getIdVendedor());
            if (exito) {
                mostrarAlerta("Éxito", "Vendedor eliminado correctamente.");
                cargarVendedores();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar el vendedor.");
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un vendedor para eliminar.");
        }

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


        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVendedor"));
        nombreColumn.setCellValueFactory(cellData -> {
            Vendedor vendedor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(vendedor.getNombre() + " " + vendedor.getApellido());
        });
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nivelColumn.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estadoVendedor"));
        cargarVendedores();
    }

    private void cargarVendedores() {
        ObservableList<Vendedor> vendedores = FXCollections.observableArrayList(verificarLogin.obtenerVendedores());
        vendedoresTable.setItems(vendedores);
    }

    }


