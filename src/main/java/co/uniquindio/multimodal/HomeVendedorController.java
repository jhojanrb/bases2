package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.SessionData;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HomeVendedorController {

    @FXML
    public Button btnDashboard;

    @FXML
    public Button btnPedidos;
    @FXML
    public Button btnComisiones;
    @FXML
    public Button btnCatalogo;
    @FXML
    public Button btnRed;
    @FXML
    public Button btnHistorial;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Label currentLevelLabel;

    @FXML
    private Label directAffiliatesLabel;

    @FXML
    private ProgressBar levelProgressBar;

    @FXML
    private Label monthCommissionsLabel;

    @FXML
    private Label monthSalesLabel;

    @FXML
    private Label nextLevelLabel;

    @FXML
    private ListView<?> notificationListView;

    @FXML
    private Label progressDetailsLabel;

    @FXML
    private TableView<VerificarLogin.VentaReciente> recentSalesTable;

    @FXML
    private TableColumn<VerificarLogin.VentaReciente, String> fechaColumn;
    @FXML
    private TableColumn<VerificarLogin.VentaReciente, String> productoColumn;
    @FXML
    private TableColumn<VerificarLogin.VentaReciente, Integer> cantidadColumn;
    @FXML
    private TableColumn<VerificarLogin.VentaReciente, Double> totalColumn;

    @FXML
    private LineChart<String, Number> salesPerformanceChart;

    @FXML
    private Label vendorNameLabel;

    private int idVendedor;

    private VerificarLogin verificarLogin = new VerificarLogin();

    public void initialize() {

        if (SessionData.getNombreVendedor() != null && SessionData.getIdVendedor() != null) {
            setVendorData(SessionData.getNombreVendedor(), SessionData.getIdVendedor());
        }

        // Configurar columnas de la tabla
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        productoColumn.setCellValueFactory(new PropertyValueFactory<>("producto"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        cargarRendimientoVentas();
        cargarUltimasVentas();
    }

    // Método para configurar el nombre y el ID del vendedor logueado
    public void setVendorData(String nombreVendedor, int idVendedor) {
        this.vendorNameLabel.setText("Bienvenido, " + nombreVendedor);
        this.idVendedor = idVendedor;

        // Cargar estadísticas después de establecer el ID del vendedor
        cargarEstadisticasVendedor();
        cargarProgresoNivel();
        cargarRendimientoVentas();
        cargarUltimasVentas();
    }

    private void cargarUltimasVentas() {
        List<VerificarLogin.VentaReciente> ventas = verificarLogin.obtenerUltimasVentas(idVendedor);
        ObservableList<VerificarLogin.VentaReciente> ventasData = FXCollections.observableArrayList(ventas);
        recentSalesTable.setItems(ventasData);
    }

    // Método para cargar y mostrar el progreso del nivel
    private void cargarProgresoNivel() {
        Map<String, Object> progresoData = verificarLogin.obtenerProgresoNivel(idVendedor);

        // Actualizar etiquetas y barra de progreso con los datos obtenidos
        currentLevelLabel.setText("Nivel Actual: " + progresoData.get("nivelActual"));
        nextLevelLabel.setText("Siguiente Nivel: " + progresoData.get("siguienteNivel"));
        levelProgressBar.setProgress((Double) progresoData.get("progreso"));
        progressDetailsLabel.setText((String) progresoData.get("detalleProgreso"));
    }

    // Cargar las estadísticas de ventas, comisiones y afiliados directos
    private void cargarEstadisticasVendedor() {
        monthSalesLabel.setText(String.valueOf(verificarLogin.obtenerVentasMes(idVendedor)));
        monthCommissionsLabel.setText(String.valueOf(verificarLogin.obtenerComisionesMes(idVendedor)));
        directAffiliatesLabel.setText(String.valueOf(verificarLogin.obtenerAfiliadosDirectos(idVendedor)));
    }

    private void cargarRendimientoVentas() {
        Map<String, Double> rendimientoVentas = verificarLogin.obtenerRendimientoVentas(idVendedor);

        // Crear una serie para el gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas Mensuales");

        for (Map.Entry<String, Double> entry : rendimientoVentas.entrySet()) {
            String mes = entry.getKey();
            Double ventasMensuales = entry.getValue();
            series.getData().add(new XYChart.Data<>(mes, ventasMensuales));
        }

        // Limpiar cualquier serie previa y cargar la nueva
        salesPerformanceChart.getData().clear();
        salesPerformanceChart.getData().add(series);
    }

    @FXML
    void cerrarSesion(ActionEvent event) {

        SessionData.clearSession();

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

    public void Dashboard(ActionEvent actionEvent) {

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
            Stage currentStage = (Stage) btnDashboard.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }
    }

    public void Catalogo(ActionEvent event){

        try {

            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("catalogoVendedor-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener el tamaño de la pantalla completa
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Crear un nuevo Stage y establecer la nueva escena con las dimensiones de la pantalla
            Stage newStage = new Stage();
            newStage.setTitle("Catalogo Vendedor");
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

    public void Red(ActionEvent event) {

    }

    public void Comisiones(ActionEvent event) {

    }

    public void Historial(ActionEvent event) {

    }

    public void Pedidos(ActionEvent event) {

    }
}




