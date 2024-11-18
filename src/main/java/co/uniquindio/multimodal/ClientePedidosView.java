package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Pedido;
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
import java.sql.Date;
import java.util.List;
import javafx.scene.input.MouseEvent;

public class ClientePedidosView {

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnCancelarPedido;

    @FXML
    private Button btnLimpiarFiltros;

    @FXML
    private Button btnRealizarNuevoPedido;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField busquedaField;

    @FXML
    private TableColumn<Pedido, String> estadoColumn;

    @FXML
    private ComboBox<String> estadoFiltroComboBox;

    @FXML
    private Label estadoPedidoLabel;

    @FXML
    private TableColumn<Pedido, Date> fechaColumn;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private Label fechaPedidoLabel;

    @FXML
    private TableColumn<Pedido, Integer> idColumn;

    @FXML
    private Label idPedidoLabel;

    @FXML
    private TableView<Pedido> pedidosTable;

    @FXML
    private TableColumn<?, ?> productoCantidadColumn;

    @FXML
    private TableColumn<?, ?> productoNombreColumn;

    @FXML
    private TableColumn<?, ?> productoPrecioColumn;

    @FXML
    private TableColumn<?, ?> productoTotalColumn;

    @FXML
    private TableView<?> productosPedidoTable;

    @FXML
    private TableColumn<Pedido, Double> totalColumn;

    @FXML
    private Label totalPedidoLabel;

    VerificarLogin verificarLogin = new VerificarLogin();
    private int vendedorID = 1005319;
    private int clienteID = 1099682;

    @FXML
    public void initialize() {
        // Configurar columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configurar listener para el doble clic
        pedidosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Detecta doble clic
                Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();
                if (pedidoSeleccionado != null) {
                    cargarDetallesPedido(pedidoSeleccionado);
                }
            }
        });


        estadoFiltroComboBox.setValue("Todos");
        buscarPedidos();

        cargarPedidos();
    }

    // Método para cargar detalles del pedido seleccionado
    private void cargarDetallesPedido(Pedido pedido) {
        idPedidoLabel.setText(String.valueOf(pedido.getIdPedido()));
        fechaPedidoLabel.setText(pedido.getFecha().toString());
        estadoPedidoLabel.setText(pedido.getEstado());
        totalPedidoLabel.setText(String.format("$ %.2f", pedido.getTotal()));
    }

    private void cargarPedidos() {
        List<Pedido> pedidos = verificarLogin.obtenerMisPedidos(vendedorID);
        ObservableList<Pedido> pedidosData = FXCollections.observableArrayList(pedidos);
        pedidosTable.setItems(pedidosData);
    }

    @FXML
    void Cancelar(ActionEvent event) {

        Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado == null) {
            mostrarAlerta("Error", "Por favor selecciona un pedido para cancelar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cancelación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas cancelar el pedido con ID: " + pedidoSeleccionado.getIdPedido() + "?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Llamar al método para cancelar el pedido
                String mensaje = verificarLogin.cancelarPedido(pedidoSeleccionado.getIdPedido());

                // Mostrar resultado
                mostrarAlerta("Resultado", mensaje, Alert.AlertType.INFORMATION);

                // Refrescar la tabla
                cargarPedidos();
            }
        });

    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    @FXML
    void buscarPedidos(ActionEvent event) {

        String busqueda = busquedaField.getText().trim();
        String estado = estadoFiltroComboBox.getValue();
        Date fecha = null;

        if (fechaInicioPicker.getValue() != null) {
            fecha = java.sql.Date.valueOf(fechaInicioPicker.getValue());
        }

        List<Pedido> pedidos = verificarLogin.buscarPedidos(
                clienteID,
                busqueda.isEmpty() ? null : busqueda,
                estado,
                fecha
        );
        ObservableList<Pedido> pedidosData = FXCollections.observableArrayList(pedidos);
        pedidosTable.setItems(pedidosData);
    }

    void buscarPedidos() {
        buscarPedidos(null);
    }


    @FXML
    void limpiarFiltros(ActionEvent event) {

        busquedaField.clear();
        estadoFiltroComboBox.setValue("Todos");
        fechaInicioPicker.setValue(null);
        cargarPedidos();

    }

    @FXML
    void realizarNuevoPedido(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("catalogoCliente-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Catalogo");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnRealizarNuevoPedido.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }

    }

    @FXML
    void volver(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homeCliente-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Home CLiente");
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
