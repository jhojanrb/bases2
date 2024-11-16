module co.uniquindio.multimodal {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires ojdbc8;
    requires java.desktop;
    requires itextpdf;
    requires java.mail;
    requires java.http.client;


    opens co.uniquindio.multimodal to javafx.fxml;
    exports co.uniquindio.multimodal;

    // Abre el paquete conexionBD para javafx.base, permitiendo el acceso a la clase SalesSummary
    opens co.uniquindio.multimodal.conexionBD to javafx.base;
}
