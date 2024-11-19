package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Vendedor;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;



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
    private Button btnRegistrar;

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

    /**
     * REGISTRA O ACTUALIZA EL VENDEDOR
     * @param event
     */
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

        // Si estamos en modo edición, actualizar el vendedor existente
        if (vendedorEditando != null) {
            boolean exito = verificarLogin.actualizarVendedor(vendedorEditando.getIdVendedor(), nombre, apellido, email, nivel, estado);
            if (exito) {
                mostrarAlerta("Éxito", "Vendedor actualizado exitosamente.");
                limpiarFormularioRegistro();
                cargarVendedores();
            } else {
                mostrarAlerta("Error", "Error al actualizar el vendedor.");
            }
        } else {
            // Modo de registro de nuevo vendedor
            boolean exito = verificarLogin.registrarVendedor(nombre, apellido, email, contrasena, nivel, estado);
            if (exito) {
                mostrarAlerta("Éxito", "Vendedor registrado exitosamente.");
                limpiarFormularioRegistro();
                cargarVendedores();
            } else {
                mostrarAlerta("Error", "Error al registrar el vendedor.");
            }
        }

        // Restablecer modo de formulario
        vendedorEditando = null;
        btnRegistrar.setText("Registrar Vendedor");

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

    /**
     * Método para cambiar el estado de un vendedor seleccionado
     */
    @FXML
    void cambiarEstado(ActionEvent event) {
        Vendedor vendedorSeleccionado = vendedoresTable.getSelectionModel().getSelectedItem();
        if (vendedorSeleccionado != null) {
            String nuevoEstado = mostrarDialogoSeleccionEstado();
            if (nuevoEstado != null) {
                boolean exito = verificarLogin.actualizarEstadoVendedor(vendedorSeleccionado.getIdVendedor(), nuevoEstado);
                if (exito) {
                    mostrarAlerta("Éxito", "Estado actualizado correctamente.");
                    cargarVendedores();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar el estado del vendedor.");
                }
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un vendedor para cambiar el estado.");
        }
    }

    private Vendedor vendedorEditando = null; // Para saber si estamos en modo de edición

    @FXML
    void editarVendedor(ActionEvent event) {

        Vendedor vendedorSeleccionado = vendedoresTable.getSelectionModel().getSelectedItem();
        if (vendedorSeleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un vendedor para editar.");
            return;
        }

        // Cargar datos del vendedor seleccionado en los campos de edición
        nombreField.setText(vendedorSeleccionado.getNombre());
        apellidoField.setText(vendedorSeleccionado.getApellido());
        emailField.setText(vendedorSeleccionado.getEmail());
        nivelRegistroComboBox.setValue(vendedorSeleccionado.getNivel());
        estadoRegistroComboBox.setValue(vendedorSeleccionado.getEstadoVendedor());

        // Cambiar el botón para indicar modo edición
        btnRegistrar.setText("Guardar Cambios");
        vendedorEditando = vendedorSeleccionado;

    }

    @FXML
    void exportarListaVendedores(ActionEvent event) {

        // Ruta de guardado
        String rutaArchivo = "C:\\2024-2\\bases 2\\PROYECTO\\reportes\\Administrador\\lista_vendedores.pdf";

        try {
            // Obtener la lista de vendedores desde la tabla
            ObservableList<Vendedor> vendedoresObservable = vendedoresTable.getItems();
            List<Vendedor> vendedores = new ArrayList<>(vendedoresObservable);

            // Llama al método de exportación en VerificarLogin
            VerificarLogin verificarLogin = new VerificarLogin();
            verificarLogin.exportarVendedoresPDF(rutaArchivo, vendedores);

            // Mostrar alerta de éxito
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Exportación Exitosa");
            alerta.setHeaderText(null);
            alerta.setContentText("La lista de vendedores se ha exportado correctamente.");

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
            mostrarAlerta("Error", "Ocurrió un error al exportar la lista: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
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
            // Crear la alerta de confirmación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de eliminación");
            confirmacion.setHeaderText("¿Está seguro que desea eliminar este vendedor?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            // Mostrar la alerta y esperar respuesta del usuario
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Si el usuario confirma, proceder a eliminar
                boolean exito = verificarLogin.eliminarVendedor(vendedorSeleccionado.getIdVendedor());
                if (exito) {
                    mostrarAlerta("Éxito", "Vendedor eliminado correctamente.");
                    cargarVendedores();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el vendedor.");
                }
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

        // Configurar el evento de doble clic en la fila de la tabla
        vendedoresTable.setRowFactory(tv -> {
            TableRow<Vendedor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
                    Vendedor vendedorSeleccionado = row.getItem();
                    cargarDetallesVendedor(vendedorSeleccionado.getIdVendedor());
                }
            });
            return row;
        });
    }

    /**
     * Cargar la información detallada del vendedor seleccionado en los labels de detalles
     */
    private void cargarDetallesVendedor(int idVendedor) {
        Map<String, Object> detalles = verificarLogin.obtenerDetallesVendedor(idVendedor);
        if (detalles.isEmpty()) {
            mostrarAlerta("Error", "No se pudo cargar los detalles del vendedor.");
            return;
        }

        detalleIdLabel.setText(String.valueOf(detalles.get("idVendedor")));
        detalleNombreLabel.setText((String) detalles.get("nombre"));
        detalleEmailLabel.setText((String) detalles.get("email"));
        detalleNivelLabel.setText((String) detalles.get("nivel"));
        detalleEstadoLabel.setText((String) detalles.get("estado"));

        // Corrige el uso de fecha_ingreso en lugar de fechaRegistro
        detalleFechaRegistroLabel.setText(detalles.get("fecha_ingreso").toString());
        detalleVentasTotalesLabel.setText(String.valueOf(detalles.get("ventasTotales")));
        detalleComisionesLabel.setText(String.valueOf(detalles.get("comisionesGanadas")));
    }

    private void cargarVendedores() {
        ObservableList<Vendedor> vendedores = FXCollections.observableArrayList(verificarLogin.obtenerVendedores());
        vendedoresTable.setItems(vendedores);
    }

    }


