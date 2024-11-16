package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.ProductoCliente;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class HomeClienteController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label clientNameLabel;

    private VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    private TableView<?> recentOrdersTable;

    @FXML
    private ListView<?> specialOffersListView;

    @FXML
    private Button añadir1;

    @FXML
    private Button añadir2;

    @FXML
    private Button añadir3;

    @FXML
    private ImageView imagePro1;

    @FXML
    private ImageView imagePro2;

    @FXML
    private ImageView imagePro3;

    @FXML
    private Label namePro1;

    @FXML
    private Label namePro2;

    @FXML
    private Label namePro3;

    @FXML
    private Label precio1;

    @FXML
    private Label precio2;

    @FXML
    private Label precio3;

    @FXML
    private TitledPane productosDestacados;

    private int idCliente;
    private String nombreCliente;

    public void initialize() {
        System.out.println("Método initialize ejecutado.");
        cargarProductosDestacados();
    }


    private void cargarProductosDestacados() {
        List<ProductoCliente> productos = verificarLogin.obtenerProductosDestacados();

        System.out.println("Productos destacados obtenidos: " + productos.size());
        productos.forEach(producto -> System.out.println("Producto: " + producto.getNombre()));

        if (productos.size() >= 1) {
            cargarProductoEnVista(productos.get(0), imagePro1, namePro1, precio1);
        }
        if (productos.size() >= 2) {
            cargarProductoEnVista(productos.get(1), imagePro2, namePro2, precio2);
        }
        if (productos.size() >= 3) {
            cargarProductoEnVista(productos.get(2), imagePro3, namePro3, precio3);
        }
    }



    private void cargarProductoEnVista(ProductoCliente producto, ImageView imageView, Label nameLabel, Label priceLabel) {
        nameLabel.setText(producto.getNombre());
        priceLabel.setText("$" + producto.getPrecio());
        if (producto.getRutaImagen() != null && !producto.getRutaImagen().isEmpty()) {
            try {
                System.out.println("Cargando imagen: " + producto.getRutaImagen());
                imageView.setImage(new Image(producto.getRutaImagen()));
            } catch (Exception e) {
                System.out.println("Error cargando la imagen: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró ruta de imagen para el producto: " + producto.getNombre());
        }
    }


    public void setClientData(String nombreCliente, int idCliente) {
        this.nombreCliente = nombreCliente;
        this.idCliente = idCliente;

        // Actualizar el label con el nombre del cliente
        if (clientNameLabel != null) {
            clientNameLabel.setText("Hola, " + nombreCliente);
        }
        System.out.println("Datos del cliente establecidos: ID = " + idCliente + ", Nombre = " + nombreCliente);
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
    void cerrarSesion(ActionEvent event) {
        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Login");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnCerrarSesion.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }
    }
}

