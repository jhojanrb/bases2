module co.uniquindio.multimodal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens co.uniquindio.multimodal to javafx.fxml;
    exports co.uniquindio.multimodal;
}