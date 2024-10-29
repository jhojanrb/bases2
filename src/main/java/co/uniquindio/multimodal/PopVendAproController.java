package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Vendedor;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


import java.io.IOException;
import java.util.List;

public class PopVendAproController {

    @FXML
    private Button btnAprobar;

    @FXML
    private Button btnCerrar;

    @FXML
    private Button btnRechazar;

    @FXML
    private TableColumn<Vendedor, String> emailColumn;

    @FXML
    private TableColumn<Vendedor, String> estadoColumn; // Cambiado a String para el nombre del estado

    @FXML
    private TableColumn<Vendedor, Integer> idColumn;

    @FXML
    private TableColumn<Vendedor, String> nombreColumn;

    @FXML
    private TableView<Vendedor> vendedoresTable;

    private VerificarLogin verificarLogin = new VerificarLogin(); // Instancia de VerificarLogin

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idVendedor"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estadoVendedor")); // Muestra el nombre del estado

        // Cargar las solicitudes pendientes en la tabla
        cargarSolicitudesPen();
    }

    public void cargarSolicitudesPen() {
        // Obtener las solicitudes pendientes y agregarlas al TableView
        List<Vendedor> solicitudesPendientes = verificarLogin.obtenerSolicitudesPendientes();
        ObservableList<Vendedor> data = FXCollections.observableArrayList(solicitudesPendientes);
        vendedoresTable.setItems(data);
    }

    @FXML
    void aprobarSeleccionados(ActionEvent event) {
        // Aprobar cada vendedor seleccionado en la tabla
        ObservableList<Vendedor> seleccionados = vendedoresTable.getSelectionModel().getSelectedItems();
        for (Vendedor vendedor : seleccionados) {
            verificarLogin.aprobarVendedor(vendedor.getIdVendedor());

            // Mostrar JOptionPane de confirmación para cada vendedor aprobado
            JOptionPane.showMessageDialog(null,
                    "Vendedor aprobado:\nID: " + vendedor.getIdVendedor() + "\nNombre: " + vendedor.getNombre(),
                    "Aprobación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        cargarSolicitudesPen(); // Refrescar la tabla después de aprobar
    }

    @FXML
    void rechazarSeleccionados(ActionEvent event) {
        // Rechazar (eliminar) cada vendedor seleccionado en la tabla
        ObservableList<Vendedor> seleccionados = vendedoresTable.getSelectionModel().getSelectedItems();
        for (Vendedor vendedor : seleccionados) {
            verificarLogin.rechazarVendedor(vendedor.getIdVendedor());

            // Mostrar JOptionPane de confirmación para cada vendedor rechazado
            JOptionPane.showMessageDialog(null,
                    "Vendedor rechazado:\nID: " + vendedor.getIdVendedor() + "\nNombre: " + vendedor.getNombre(),
                    "Rechazo Exitoso",
                    JOptionPane.WARNING_MESSAGE);
        }
        cargarSolicitudesPen(); // Refrescar la tabla después de rechazar
    }

    @FXML
    void cerrarPopup(ActionEvent event) {
        try {
            // Cargar la interfaz de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Obtener la ventana actual y establecer la nueva escena
            Stage stage = (Stage) btnCerrar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la interfaz de inicio de sesión.");
        }
    }
}
