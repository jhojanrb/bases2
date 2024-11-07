package co.uniquindio.multimodal;

import co.uniquindio.multimodal.conexionBD.Producto;
import co.uniquindio.multimodal.conexionBD.ProductoDetalles;
import co.uniquindio.multimodal.conexionBD.VerificarLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

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
    private ComboBox<String> categoriaFiltroComboBox;

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
    private TextField idField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField precioField;

    @FXML
    private TextField stockField;

    @FXML
    private ComboBox<String> categoriaComboBox;

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

    @FXML
    private String rutaImagenSeleccionada; // Para almacenar la ruta de la imagen seleccionada


    private VerificarLogin verificarLogin = new VerificarLogin();
    private String rutaImagenPorDefecto = "file:/C:/2024-2/bases 2/PROYECTO/imagenes/image.jpg";

    private Map<String, Integer> categoriaMap = new HashMap<>();


    @FXML
    public void initialize() {

        // Mapear nombres de categoría a sus IDs
        categoriaMap.put("Electrónica", 1);
        categoriaMap.put("Ropa", 2);
        categoriaMap.put("Hogar", 3);
        categoriaMap.put("Belleza", 4);
        categoriaMap.put("Deportes", 5);
        categoriaMap.put("Moda", 6);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        cargarProductos();

        productosTable.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Producto producto = row.getItem();
                    mostrarDetallesProducto(producto.getId());
                }
            });
            return row;
        });
    }

    public void cargarProductos() {
        List<Producto> productos = verificarLogin.obtenerProductos();
        ObservableList<Producto> data = FXCollections.observableArrayList(productos);
        productosTable.setItems(data);
    }

    private void mostrarDetallesProducto(int idProducto) {
        ProductoDetalles detalles = verificarLogin.obtenerDetallesProducto(idProducto);

        if (detalles != null) {
            idField.setText(String.valueOf(detalles.getId()));  // Convertir a String
            nombreField.setText(detalles.getNombre());
            categoriaComboBox.setValue(detalles.getCategoria());
            precioField.setText(String.valueOf(detalles.getPrecio()));  // Convertir a String
            stockField.setText(String.valueOf(detalles.getStock()));  // Convertir a String
            descripcionArea.setText(detalles.getDescripcion());

            String rutaImagen = detalles.getRutaImagen();
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                File file;
                if (rutaImagen.startsWith("file:")) {
                    file = new File(rutaImagen.substring(5));
                } else {
                    file = new File(rutaImagen);
                }
                if (file.exists()) {
                    imagenProductoView.setImage(new Image(file.toURI().toString()));
                } else {
                    imagenProductoView.setImage(new Image(rutaImagenPorDefecto));
                }
            } else {
                imagenProductoView.setImage(new Image(rutaImagenPorDefecto));
            }
        } else {
            mostrarAlerta("Error", "No se encontraron detalles del producto.", AlertType.ERROR);
        }
    }


    @FXML
    void agregarProducto(ActionEvent event) {
        try {
            // Validar campos antes de proceder
            if (nombreField.getText().isEmpty() || categoriaComboBox.getValue() == null || precioField.getText().isEmpty() || stockField.getText().isEmpty()) {
                mostrarAlerta("Campos requeridos", "Todos los campos son obligatorios.", AlertType.WARNING);
                return;
            }
            String nombre = nombreField.getText();
            String categoriaNombre = categoriaComboBox.getValue();
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String descripcion = descripcionArea.getText();

            Integer categoriaId = categoriaMap.get(categoriaNombre);

            if (rutaImagenSeleccionada == null || rutaImagenSeleccionada.isEmpty()) {
                rutaImagenSeleccionada = rutaImagenPorDefecto;
            }

            verificarLogin.agregarProducto(nombre, categoriaId, precio, stock, descripcion, rutaImagenSeleccionada);

            cargarProductos();
            mostrarAlerta("Éxito", "Producto agregado exitosamente.", AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Asegúrate de que el precio y el stock tengan valores numéricos válidos.", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al agregar el producto.", AlertType.ERROR);
            e.printStackTrace();
        }
    }


    // Método para obtener el ID de la categoría basado en el nombre
    private int obtenerCategoriaIdPorNombre(String categoria) {
        switch (categoria.toLowerCase()) {
            case "electrónica":
                return 1;
            case "ropa":
                return 2;
            case "hogar":
                return 3;
            case "belleza":
                return 4;
            case "deportes":
                return 5;
            default:
                return 0; // O algún valor para indicar error
        }
    }

    private void limpiarCampos() {
        idField.clear();
        nombreField.clear();
        categoriaComboBox.setValue(null);
        precioField.clear();
        stockField.clear();
        descripcionArea.clear();
        imagenProductoView.setImage(null);
        rutaImagenSeleccionada = null;
    }

    @FXML
    void buscarProductos(ActionEvent event) {

        String filtroNombreId = busquedaField.getText();
        String categoria = categoriaFiltroComboBox.getValue();

        // Si la categoría es "Todas", asignarla a null para que no filtre específicamente por categoría
        if ("Todas".equals(categoria)) {
            categoria = null;
        }

        // Ejecutar búsqueda y mostrar resultados
        List<Producto> resultados = verificarLogin.buscarProductos(filtroNombreId, categoria);
        ObservableList<Producto> data = FXCollections.observableArrayList(resultados);
        productosTable.setItems(data);

        if (resultados.isEmpty()) {
            mostrarAlerta("Sin Resultados", "No se encontraron productos que coincidan con la búsqueda.", Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    void editarProducto(ActionEvent event) {
        try {
            int idProducto = Integer.parseInt(idField.getText());
            String nombre = nombreField.getText();
            String categoria = categoriaComboBox.getValue();
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String descripcion = descripcionArea.getText();
            String rutaImagen = (rutaImagenSeleccionada != null) ? rutaImagenSeleccionada : rutaImagenPorDefecto;

            verificarLogin.actualizarProducto(idProducto, nombre, categoria, precio, stock, descripcion, rutaImagen);

            cargarProductos();
            mostrarAlerta("Éxito", "Producto actualizado exitosamente.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Asegúrate de que el precio y el stock tengan valores numéricos válidos.", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al actualizar el producto.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void eliminarProducto(ActionEvent event) {
        // Validar que se haya seleccionado un producto
        if (idField.getText().isEmpty()) {
            mostrarAlerta("Error", "Selecciona un producto para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación de eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar este producto?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Obtener el ID del producto y eliminarlo
                    int idProducto = Integer.parseInt(idField.getText());
                    verificarLogin.eliminarProducto(idProducto);

                    // Recargar productos y limpiar campos
                    cargarProductos();
                    limpiarCampos();

                    mostrarAlerta("Éxito", "Producto eliminado exitosamente.", Alert.AlertType.INFORMATION);

                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "ID de producto no válido.", Alert.AlertType.ERROR);
                } catch (Exception e) {
                    mostrarAlerta("Error", "Ocurrió un error al eliminar el producto.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void exportarCatalogo(ActionEvent event) {

    }

    @FXML
    void limpiarFiltros(ActionEvent event) {

        busquedaField.clear();
        categoriaFiltroComboBox.getSelectionModel().select("Todas");

        limpiarCampos();
        cargarProductos();

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

    @FXML
    void cargarImagen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(imagenProductoView.getScene().getWindow());
        if (file != null) {
            Image imagenProducto = new Image(file.toURI().toString());
            imagenProductoView.setImage(imagenProducto);
            rutaImagenSeleccionada = file.toURI().toString();
            mostrarAlerta("Imagen cargada", "Imagen del producto seleccionada exitosamente.", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


}
