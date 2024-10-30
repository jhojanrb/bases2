package co.uniquindio.multimodal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Obtiene el tamaño de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    // Método para abrir una nueva ventana ocupando toda la pantalla
    public static void openFullScreenWindow(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        // Obtiene el tamaño de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Stage newStage = new Stage();

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        newStage.setTitle(title);
        newStage.setScene(scene);
        newStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
