package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class HomeClienteController {

    @FXML
    public TableColumn<PedidoReciente, Integer> pedidoColumn;
    @FXML
    public TableColumn<PedidoReciente, String> fechaColumn;
    @FXML
    public TableColumn<PedidoReciente, Double> totalColumn;
    @FXML
    public TableColumn<PedidoReciente, String> estadoColumn;
    @FXML
    public VBox estadoPedidosVBox;
    @FXML
    public Button btninicio;
    @FXML
    public Button btnCatalogo;
    @FXML
    public Button btnMisPedidos;
    @FXML
    public Button btnPerfil;
    @FXML
    public Button btnContacto;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label clientNameLabel;

    private VerificarLogin verificarLogin = new VerificarLogin();

    @FXML
    private TableView<PedidoReciente> recentOrdersTable;

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

        if (SessionData.getNombreCliente() != null && SessionData.getIdCliente() != null) {
            setClientData(SessionData.getNombreCliente(), SessionData.getIdCliente());
        }

        pedidoColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPedido"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarProductosDestacados();
        cargarEstadoPedidosActuales();
    }

    private void cargarEstadoPedidosActuales() {
        List<PedidoActual> pedidosActuales = verificarLogin.obtenerEstadoPedidosActualesCliente(idCliente);

        if (pedidosActuales.isEmpty()) {
            System.out.println("No hay pedidos actuales para este cliente.");
        } else {
            System.out.println("Pedidos actuales cargados: " + pedidosActuales.size());
        }

        estadoPedidosVBox.getChildren().clear(); // Limpia el VBox

        for (PedidoActual pedido : pedidosActuales) {
            System.out.println("Cargando pedido: " + pedido.getNumeroPedido()); // Depuración

            HBox pedidoBox = new HBox(10);
            pedidoBox.setAlignment(Pos.CENTER_LEFT);

            Label pedidoLabel = new Label("Pedido #" + pedido.getNumeroPedido());
            pedidoLabel.setStyle("-fx-font-weight: bold;");

            ProgressBar progressBar = new ProgressBar(pedido.getProgreso());
            progressBar.setPrefWidth(200);

            Label estadoLabel = new Label(pedido.getEstado());

            pedidoBox.getChildren().addAll(pedidoLabel, progressBar, estadoLabel);
            estadoPedidosVBox.getChildren().add(pedidoBox);
        }
    }


    private void cargarPedidosRecientes() {
        List<PedidoReciente> pedidosRecientes = verificarLogin.obtenerPedidosRecientesCliente(idCliente);
        System.out.println("Pedidos encontrados: " + pedidosRecientes.size()); // Depuración
        ObservableList<PedidoReciente> pedidosData = FXCollections.observableArrayList(pedidosRecientes);
        recentOrdersTable.setItems(pedidosData);
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

        cargarPedidosRecientes();
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
    void inicio(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homeCliente-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Home Cliente");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btninicio.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }

    }

    @FXML
    void catalogo(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("catalogoCliente-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Catalogo Cliente");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnCatalogo.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }

    }

    @FXML
    void pedidos(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientePedidos-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Mis Pedidos");
            newStage.setScene(scene);
            newStage.setX(screenBounds.getMinX());
            newStage.setY(screenBounds.getMinY());
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            // Mostrar el nuevo Stage en pantalla completa
            newStage.show();

            // Cerrar el Stage actual si es necesario
            Stage currentStage = (Stage) btnMisPedidos.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }

    }

    @FXML
    void perfil(ActionEvent event) {

    }


    @FXML
    void cerrarSesion(ActionEvent event) {

        SessionData.clearCliente();

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


    @FXML
    public void contacto(ActionEvent event) {
    }
}

