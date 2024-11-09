package co.uniquindio.multimodal;

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
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CatalogoVendedorController {

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
    private ComboBox<?> categoriaFiltroComboBox;

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
