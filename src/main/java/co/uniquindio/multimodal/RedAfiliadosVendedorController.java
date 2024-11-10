package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Afiliado;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class RedAfiliadosVendedorController {

    @FXML
    private TableView<Afiliado> afiliadosTable;

    @FXML
    private Button btnAgregarAfiliado;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnContactar;

    @FXML
    private Button btnExportarDatos;

    @FXML
    private Button btnGenerarInforme;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnVerDetalles;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField busquedaField;

    @FXML
    private TableColumn<Afiliado, Double> comisionesGeneradasColumn;

    @FXML
    private Label comisionesTotalesLabel;

    @FXML
    private PieChart distribucionNivelesPieChart;

    @FXML
    private TableColumn<Afiliado, String> fechaAfilacionColumn;

    @FXML
    private TableColumn<Afiliado, Integer> idColumn;

    @FXML
    private TableColumn<Afiliado, String> nivelColumn;

    @FXML
    private ComboBox<String> nivelFiltroComboBox;

    @FXML
    private TableColumn<Afiliado, String> nombreColumn;

    @FXML
    private Label totalAfiliadosLabel;

    @FXML
    private TableColumn<Afiliado, Double> ventasTotalesColumn;

    @FXML
    private Label ventasTotalesRedLabel;

    private VerificarLogin verificarLogin = new VerificarLogin();

    private int idVendedor;

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
        System.out.println("ID del vendedor establecido en: " + idVendedor);
        cargarAfiliados();
        cargarDistribucionNiveles();
        cargarTotalesVendedor();
    }



    public void initialize() {
        // Configurar columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nivelColumn.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        fechaAfilacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaAfiliacion"));
        ventasTotalesColumn.setCellValueFactory(new PropertyValueFactory<>("ventasTotales"));
        comisionesGeneradasColumn.setCellValueFactory(new PropertyValueFactory<>("comisionesGeneradas"));

        // Cargar datos
        cargarAfiliados();
        cargarDistribucionNiveles();
    }

    private void cargarDistribucionNiveles() {
        List<PieChart.Data> distribucionData = verificarLogin.obtenerDistribucionNivelesAfiliados(idVendedor);

        if (distribucionData != null && !distribucionData.isEmpty()) {
            distribucionNivelesPieChart.setData(FXCollections.observableArrayList(distribucionData));
            System.out.println("Datos cargados en el PieChart:");
            distribucionData.forEach(data -> System.out.println(data.getName() + ": " + data.getPieValue()));
        } else {
            System.out.println("No se encontraron datos para la distribución de niveles.");
        }
    }

    private void cargarTotalesVendedor() {
        Map<String, Double> totales = verificarLogin.obtenerTotalesVendedor(idVendedor);

        // Verifica si se recibieron datos y actualiza los Label
        if (totales != null && !totales.isEmpty()) {
            totalAfiliadosLabel.setText(String.valueOf(totales.get("totalAfiliados").intValue()));
            ventasTotalesRedLabel.setText(String.format("$%.2f", totales.get("ventasTotalesRed")));
            comisionesTotalesLabel.setText(String.format("$%.2f", totales.get("comisionesTotales")));
        } else {
            System.out.println("No se pudieron cargar los totales para el vendedor con ID: " + idVendedor);
        }
    }

    private void cargarAfiliados() {
        System.out.println("Cargando afiliados para el vendedor con ID: " + idVendedor);
        List<Afiliado> afiliados = verificarLogin.obtenerAfiliadosVendedor(idVendedor);
        if (afiliados != null) {
            System.out.println("Cantidad de afiliados encontrados: " + afiliados.size());
        } else {
            System.out.println("No se encontraron afiliados para este vendedor.");
        }
        ObservableList<Afiliado> afiliadosData = FXCollections.observableArrayList(afiliados);
        afiliadosTable.setItems(afiliadosData);
    }


    @FXML
    void agregarNuevoAfiliado(ActionEvent event) {

    }

    @FXML
    void buscarAfiliados(ActionEvent event) {

        String nombreAfiliado = busquedaField.getText();
        String nivelFiltro = nivelFiltroComboBox.getValue();

        // Si nivelFiltro está vacío o es nulo, establecerlo como "Todos"
        if (nivelFiltro == null || nivelFiltro.isEmpty()) {
            nivelFiltro = "Todos";
        }

        System.out.println("Buscando afiliados por: " + (nombreAfiliado.isEmpty() ? "nombre" : "ID o nombre")
                + " con filtro de nivel: " + nivelFiltro);

        List<Afiliado> afiliados = verificarLogin.buscarAfiliados(idVendedor, nombreAfiliado, nivelFiltro);

        // Cargar los resultados en la tabla
        ObservableList<Afiliado> afiliadosData = FXCollections.observableArrayList(afiliados);
        afiliadosTable.setItems(afiliadosData);

    }

    @FXML
    void contactarAfiliado(ActionEvent event) {

    }

    @FXML
    void exportarDatosRed(ActionEvent event) {

    }

    @FXML
    void generarInformeRed(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

        busquedaField.clear();
        nivelFiltroComboBox.getSelectionModel().select("Todos");

        cargarAfiliados();

    }

    @FXML
    void verDetallesAfiliado(ActionEvent event) {

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
