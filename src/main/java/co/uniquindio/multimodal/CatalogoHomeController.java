package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Producto;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CatalogoHomeController {

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEditarProducto;

    @FXML
    private Button btnAgregarImagen;

    @FXML
    private Button btnEliminarProducto;

    @FXML
    private Button btnExportar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField busquedaField;

    @FXML
    private TableColumn<?, ?> categoriaColumn;

    @FXML
    private ComboBox<?> categoriaFiltroComboBox;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private Label detalleCategoriaLabel;

    @FXML
    private Label detalleComisionLabel;

    @FXML
    private Label detalleDescripcionLabel;

    @FXML
    private Label detalleFechaCreacionLabel;

    @FXML
    private Label detalleIdLabel;

    @FXML
    private Label detalleNombreLabel;

    @FXML
    private Label detallePrecioLabel;

    @FXML
    private Label detalleStockLabel;

    @FXML
    private Label detalleUltimaActualizacionLabel;

    @FXML
    private ImageView imagenProductoView;

    @FXML
    private TableColumn<Producto, Integer> idColumn;

    @FXML
    private TableColumn<Producto, String> nombreColumn;

    @FXML
    private TableColumn<Producto, Double> precioColumn;

    @FXML
    private TableView<Producto> productosTable;

    @FXML
    private TableColumn<Producto, Integer> stockColumn;

    private VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    public void initialize() {
        // Configurar las columnas de la TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Cargar los productos en la tabla
        cargarProductos();
    }

    public void cargarProductos() {
        List<Producto> productos = verificarLogin.obtenerProductos();
        ObservableList<Producto> data = FXCollections.observableArrayList(productos);
        productosTable.setItems(data);
    }

    @FXML
    void agregarProducto(ActionEvent event) {

    }

    @FXML
    void buscarProductos(ActionEvent event) {

    }

    @FXML
    void editarProducto(ActionEvent event) {

    }

    @FXML
    void eliminarProducto(ActionEvent event) {

    }

    @FXML
    void exportarCatalogo(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

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

    public void cargarImagen(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );

        // Abre el cuadro de diálogo para seleccionar la imagen
        File file = fileChooser.showOpenDialog(imagenProductoView.getScene().getWindow());

        if (file != null) {
            // Carga y muestra la imagen seleccionada en el ImageView
            Image imagenProducto = new Image(file.toURI().toString());
            imagenProductoView.setImage(imagenProducto);

            // Aquí podrías agregar la lógica para guardar la ruta o el archivo de imagen

            // asociado con el producto en la base de datos o en el modelo del producto
        }
    }
}
