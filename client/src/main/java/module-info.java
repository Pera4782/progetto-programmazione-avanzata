module it.unipi.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.base;

    opens it.unipi.client to javafx.fxml;
    exports it.unipi.client;
}
