package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Producto;
import co.uniquindio.multimodal.conexionBD.ProductoDetalles;
import co.uniquindio.multimodal.conexionBD.ProductoVendedor;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CatalogoVendedorController {

    @FXML
    public TextField idField;

    @FXML
    public TextField nombreField;
    @FXML
    public TextField stockField;
    @FXML
    public TextField comisionField;
    @FXML
    public TextField categoriaField;
    @FXML
    public TextField precioField;
    @FXML
    private Button btnAgregarAFavoritos;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnCompartir;

    @FXML
    private Button btnExportarCatalogo;

    @FXML
    private Button btnGenerarEnlace;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnVerFavoritos;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField busquedaField;


    @FXML
    private ComboBox<String> categoriaFiltroComboBox;

    @FXML
    private Label categoriaLabel;

    @FXML
    private Label comisionLabel;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private Label idLabel;

    @FXML
    private ImageView imagenProductoView;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label precioLabel;

    @FXML
    private TableView<VerificarLogin.ProductoConComision> productosTable;

    @FXML
    private TableColumn<VerificarLogin.ProductoConComision, Integer> idColumn;
    @FXML
    private TableColumn<VerificarLogin.ProductoConComision, String> nombreColumn;
    @FXML
    private TableColumn<VerificarLogin.ProductoConComision, String> categoriaColumn;
    @FXML
    private TableColumn<VerificarLogin.ProductoConComision, Double> precioColumn;
    @FXML
    private TableColumn<VerificarLogin.ProductoConComision, Double> comisionColumn;
    @FXML
    private TableColumn<VerificarLogin.ProductoConComision, Integer> stockColumn;

    @FXML
    private Label stockLabel;

    private VerificarLogin verificarLogin = new VerificarLogin();

    public void initialize() {
        // Configurar columnas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        comisionColumn.setCellValueFactory(new PropertyValueFactory<>("comisionPromedio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        cargarProductosConComision();

        // Doble clic para mostrar detalles del producto
        productosTable.setRowFactory(tv -> {
            TableRow<VerificarLogin.ProductoConComision> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    VerificarLogin.ProductoConComision producto = row.getItem();
                    mostrarDetallesProducto(producto.getId());
                }
            });
            return row;
        });
    }

    private void mostrarDetallesProducto(int idProducto) {
        ProductoVendedor detalles = verificarLogin.obtenerDetallesProducto(idProducto);

        if (detalles != null) {
            idField.setText(String.valueOf(detalles.getId()));
            nombreField.setText(detalles.getNombre());
            categoriaField.setText(detalles.getCategoria());
            precioField.setText(String.valueOf(detalles.getPrecio()));
            comisionField.setText(String.valueOf(detalles.getComision()));
            stockField.setText(String.valueOf(detalles.getStock()));
            descripcionArea.setText(detalles.getDescripcion());

            // Cargar la imagen
            String rutaImagen = detalles.getRutaImagen();
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                // Asegúrate de que la ruta de imagen tiene el prefijo "file:" si es local
                if (!rutaImagen.startsWith("file:")) {
                    rutaImagen = "file:/" + rutaImagen;
                }
                imagenProductoView.setImage(new Image(rutaImagen));
            } else {
                // Cargar imagen por defecto si no se encuentra la imagen
                imagenProductoView.setImage(new Image("file:/C:/2024-2/bases 2/PROYECTO/imagenes/image.jpg")); // Cambia esta ruta por la de tu imagen predeterminada
            }
        } else {
            mostrarAlerta("Error", "No se encontraron detalles del producto.", Alert.AlertType.ERROR);
        }
    }


    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void cargarProductosConComision() {
        List<VerificarLogin.ProductoConComision> productos = verificarLogin.obtenerProductosConComision();
        ObservableList<VerificarLogin.ProductoConComision> productosData = FXCollections.observableArrayList(productos);
        productosTable.setItems(productosData);
    }

    @FXML
    void agregarAFavoritos(ActionEvent event) {

    }

    @FXML
    void buscarProductos(ActionEvent event) {

    }

    @FXML
    void compartirProducto(ActionEvent event) {

    }

    @FXML
    void exportarCatalogo(ActionEvent event) {

    }

    @FXML
    void generarEnlaceReferido(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

    }

    @FXML
    void verFavoritos(ActionEvent event) {

    }

    @FXML
    void volverAlDashboard(ActionEvent event) {

        try {

            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homeVendedor-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Home Vendedor");
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
