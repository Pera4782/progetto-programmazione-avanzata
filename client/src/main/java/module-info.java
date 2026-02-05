module it.unipi.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.base;
    requires java.net.http;
    requires com.google.gson;
    
    opens it.unipi.client to javafx.fxml, com.google.gson;
    opens it.unipi.client.model to com.google.gson, javafx.base;
    opens it.unipi.client.model.requests to com.google.gson;
    opens it.unipi.client.model.responses to com.google.gson;
    exports it.unipi.client;
}
