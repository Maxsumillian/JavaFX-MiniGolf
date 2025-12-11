module com.example.golf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;
    requires annotations;
    requires com.google.gson;

    opens com.example.golf to javafx.fxml, com.google.gson;
    exports com.example.golf;
}