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

    @FXML
    private Label txtRegistrosMes;

    @FXML
    private Label txtTotalVendedores;

    @FXML
    private Label txtTotalVentas;

    @FXML
    private void initialize(){
        loadSalesSummary();
    }

    @FXML
    void aprobarVendedor(ActionEvent event) {

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

            // Obtener la ventana actual y establecer la nueva escena
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
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

    }

    @FXML
    void gestionVendedores(ActionEvent event) {

    }

    @FXML
    void gestionarPromocion(ActionEvent event) {

    }

    @FXML
    void inventario(ActionEvent event) {

    }

    @FXML
    void reportes(ActionEvent event) {

    }

}




