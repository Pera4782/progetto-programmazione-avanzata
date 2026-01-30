module it.unipi.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.base;
    requires java.net.http;
    requires com.google.gson;
    
    opens it.unipi.client to javafx.fxml, com.google.gson;
    exports it.unipi.client;
}
