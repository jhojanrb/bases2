package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.ProductoCatalogo;
import co.uniquindio.multimodal.conexionBD.ProductoCliente;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CatalogoProductosClienteController {



    @FXML
    private Button añadir1;

    @FXML
    private Button añadir10;

    @FXML
    private Button añadir11;

    @FXML
    private Button añadir12;

    @FXML
    private Button añadir2;

    @FXML
    private Button añadir3;

    @FXML
    private Button añadir4;

    @FXML
    private Button añadir5;

    @FXML
    private Button añadir6;

    @FXML
    private Button añadir7;

    @FXML
    private Button añadir8;

    @FXML
    private Button añadir9;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnVolver;

    @FXML
    private Button btnRealizarPedido;

    @FXML
    private Button btnVerPedidos;

    @FXML
    private TextField busquedaField;

    @FXML
    private ListView<?> carritoListView;

    @FXML
    private TableColumn<ProductoCatalogo, String> categoriaColumn;

    @FXML
    private ComboBox<?> categoriaFiltroComboBox;

    @FXML
    private ImageView imagePro1;

    @FXML
    private ImageView imagePro10;

    @FXML
    private ImageView imagePro11;

    @FXML
    private ImageView imagePro12;

    @FXML
    private ImageView imagePro2;

    @FXML
    private ImageView imagePro3;

    @FXML
    private ImageView imagePro4;

    @FXML
    private ImageView imagePro5;

    @FXML
    private ImageView imagePro6;

    @FXML
    private ImageView imagePro7;

    @FXML
    private ImageView imagePro8;

    @FXML
    private ImageView imagePro9;

    @FXML
    private Label namePro1;

    @FXML
    private Label namePro10;

    @FXML
    private Label namePro11;

    @FXML
    private Label namePro12;

    @FXML
    private Label namePro2;

    @FXML
    private Label namePro3;

    @FXML
    private Label namePro4;

    @FXML
    private Label namePro5;

    @FXML
    private Label namePro6;

    @FXML
    private Label namePro7;

    @FXML
    private Label namePro8;

    @FXML
    private Label namePro9;

    @FXML
    private TableColumn<ProductoCatalogo, String> nombreColumn;

    @FXML
    private Label precio1;

    @FXML
    private Label precio10;

    @FXML
    private Label precio11;

    @FXML
    private Label precio12;

    @FXML
    private Label precio2;

    @FXML
    private Label precio3;

    @FXML
    private Label precio4;

    @FXML
    private Label precio5;

    @FXML
    private Label precio6;

    @FXML
    private Label precio7;

    @FXML
    private Label precio8;

    @FXML
    private Label precio9;

    @FXML
    private TableColumn<ProductoCatalogo, Double> precioColumn;

    @FXML
    private TitledPane productosDestacados;

    @FXML
    private TableView<ProductoCatalogo> productosTable;

    @FXML
    private TableColumn<ProductoCatalogo, Integer> stockColumn;

    @FXML
    private TitledPane todosLosProductos;

    @FXML
    private Label totalLabel;

    private VerificarLogin verificarLogin = new VerificarLogin();

    public void initialize() {

        // Configurar columnas
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        cargarProductosDestacados();
        cargarProductosCatalogo();
    }

    private void cargarProductosCatalogo() {
        List<ProductoCatalogo> productos = verificarLogin.obtenerProductosCatalogoCliente();
        ObservableList<ProductoCatalogo> productosData = FXCollections.observableArrayList(productos);
        productosTable.setItems(productosData);
    }

    private void cargarProductosDestacados() {
        List<ProductoCliente> productos = verificarLogin.obtenerProductosTendencia();

        // Cargar los productos en la vista
        if (productos.size() > 0) setProductoEnVista(productos.get(0), imagePro1, namePro1, precio1);
        if (productos.size() > 1) setProductoEnVista(productos.get(1), imagePro2, namePro2, precio2);
        if (productos.size() > 2) setProductoEnVista(productos.get(2), imagePro3, namePro3, precio3);
        if (productos.size() > 3) setProductoEnVista(productos.get(3), imagePro4, namePro4, precio4);
        if (productos.size() > 4) setProductoEnVista(productos.get(4), imagePro5, namePro5, precio5);
        if (productos.size() > 5) setProductoEnVista(productos.get(5), imagePro6, namePro6, precio6);
        if (productos.size() > 6) setProductoEnVista(productos.get(6), imagePro7, namePro7, precio7);
        if (productos.size() > 7) setProductoEnVista(productos.get(7), imagePro8, namePro8, precio8);
        if (productos.size() > 8) setProductoEnVista(productos.get(8), imagePro9, namePro9, precio9);
        if (productos.size() > 9) setProductoEnVista(productos.get(9), imagePro10, namePro10, precio10);
        if (productos.size() > 10) setProductoEnVista(productos.get(10), imagePro11, namePro11, precio11);
        if (productos.size() > 11) setProductoEnVista(productos.get(11), imagePro12, namePro12, precio12);
    }

    private void setProductoEnVista(ProductoCliente producto, ImageView imageView, Label nameLabel, Label priceLabel) {
        nameLabel.setText(producto.getNombre());
        priceLabel.setText(String.format("$ %.2f", producto.getPrecio()));

        // Cargar imagen desde la ruta proporcionada
        if (producto.getRutaImagen() != null && !producto.getRutaImagen().isEmpty()) {
            imageView.setImage(new Image(producto.getRutaImagen()));
        } else {
            imageView.setImage(new Image("file:default-image.png")); // Imagen por defecto
        }
    }

    @FXML
    void añadirCarro1(ActionEvent event) {

    }

    @FXML
    void añadirCarro2(ActionEvent event) {

    }

    @FXML
    void añadirCarro3(ActionEvent event) {

    }

    @FXML
    void buscarProductos(ActionEvent event) {

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

    @FXML
    void realizarPedido(ActionEvent event) {

    }

    @FXML
    void verPedidos(ActionEvent event) {

    }

}
