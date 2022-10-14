package com.example.postman_copy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HelloController implements Initializable {

    @FXML
    private TextField endpoint;

    @FXML
    private ChoiceBox<String> requestType;

    @FXML
    private Button sendRequestButton;

    @FXML
    private TextArea responseText;

    @FXML
    protected void sendHttpRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if(requestType.getValue() == "Get") {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint.getText()))
                    .build();
        } else if(requestType.getValue() == "Post"){
            // Get key value pairs from the user if the request type is post
            var values = new HashMap<String, String>() {{
                put("name", "John Doe");
                put ("occupation", "gardener");
            }};

            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writeValueAsString(values);

            request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint.getText()))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        }

        // Public api endpoint for get and post requests
        // http://webcode.me
        // https://httpbin.org/post
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        responseText.setText("HTTP response is: " + response.body());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> requestTypes = FXCollections.observableArrayList(
                "Get", "Post", "Delete");

        requestType.setItems(requestTypes);
        requestType.setValue("Get");
    }
}