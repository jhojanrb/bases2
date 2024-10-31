package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.SalesSummary;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

/*------------------- PANTALLA COMPLETA*/

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/*--------------------*/

public class HomeController {

    @FXML
    private Button btnAprobarVendedores;

    @FXML
    private Button btnCatalogoProductos;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnComisiones;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnGestionVendedores;

    @FXML
    private Button btnGestionarPromocion;

    @FXML
    private Button btnInventario;

    @FXML
    private Button btnRevisarSolicitudesPago;

    @FXML
    private Button btnReportes;

    @FXML
    private ListView<?> notificationListView;

    @FXML
    private TableView<SalesSummary> salesSummaryTable;

    private VerificarLogin verificarLogin = new VerificarLogin(); // Instancia de VerificarLogin

    public void loadSalesSummary() {
        ObservableList<SalesSummary> data = FXCollections.observableArrayList(verificarLogin.obtenerResumenVentasNivel());

        // Configuración de columnas del TableView
        TableColumn<SalesSummary, String> nivelColumn = new TableColumn<>("Nivel");
        nivelColumn.setCellValueFactory(new PropertyValueFactory<>("nivel"));

        TableColumn<SalesSummary, Integer> vendedoresColumn = new TableColumn<>("Número de Vendedores");
        vendedoresColumn.setCellValueFactory(new PropertyValueFactory<>("numeroVendedores"));

        TableColumn<SalesSummary, Integer> ventasColumn = new TableColumn<>("Ventas Totales");
        ventasColumn.setCellValueFactory(new PropertyValueFactory<>("ventasTotales"));

        salesSummaryTable.getColumns().clear();
        salesSummaryTable.getColumns().addAll(nivelColumn, vendedoresColumn, ventasColumn);
        salesSummaryTable.setItems(data);
    }

    private void actualizarEstadisticas() {
        txtTotalVendedores.setText(String.valueOf(verificarLogin.obtenerTotalVendedores()));
        txtTotalVentas.setText(String.valueOf(verificarLogin.obtenerVentasTotales()));
        txtRegistrosMes.setText(String.valueOf(verificarLogin.obtenerRegistrosMes()));

    }

    @FXML
    private Label txtRegistrosMes;

    @FXML
    private Label txtTotalVendedores;

    @FXML
    private Label txtTotalVentas;

    @FXML
    private void initialize(){
        loadSalesSummary();
        actualizarEstadisticas();
    }

    @FXML
    void aprobarVendedor(ActionEvent event) {
        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PopUpAprobarVend-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Aprobar Vendedores");
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
    void catalogoProductos(ActionEvent event) {

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

    @FXML
    void comisiones(ActionEvent event) {

    }

    @FXML
    void dashboard(ActionEvent event) {

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
            Stage currentStage = (Stage) btnCerrarSesion.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz Home Admin.");
        }

    }

    @FXML
    void gestionVendedores(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gestionVendedores-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Gestionar Vendedores");
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
            System.out.println("Error al cargar la interfaz Gestion de Vendedores.");
        }

    }

    @FXML
    void gestionarPromocion(ActionEvent event) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gestionPromo-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Gestionar Promociones");
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
    void inventario(ActionEvent event) {

    }

    @FXML
    void reportes(ActionEvent event) {

    }

    public void gestionarSolicitudes(ActionEvent actionEvent) {

        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("solicitudPago-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Gestionar Solicitudes");
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
            System.out.println("Error al cargar la interfaz de solicitud pagos.");
        }
    }
}




