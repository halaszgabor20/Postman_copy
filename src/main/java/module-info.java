module com.example.postman_copy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.example.postman_copy to javafx.fxml;
    exports com.example.postman_copy;
}