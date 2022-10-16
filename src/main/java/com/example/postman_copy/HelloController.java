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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.layout.Pane;

public class HelloController implements Initializable {

    @FXML
    private TextField endpoint;

    @FXML
    private ChoiceBox<String> requestType;

    @FXML
    private TextArea responseText;

    @FXML
    private TextField key1;

    @FXML
    private TextField value1;

    @FXML
    private Pane postPanel;


    @FXML
    protected void sendHttpRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if(!endpoint.getText().isEmpty()) {
            if (requestType.getValue() == "Get") {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText()))
                        .build();
            } else if (requestType.getValue() == "Post") {
                // Get key value pairs from the user if the request type is post
                HashMap values = new HashMap<String, String>() {{
                    put(key1.getText(), value1.getText());
                    put("occupation", "gardener");
                }};

                var objectMapper = new ObjectMapper();
                String requestBody = objectMapper
                        .writeValueAsString(values);

                request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText()))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();
            } else if(requestType.getValue() == "Delete") {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText()))
                        .DELETE().build();
            }
        }

        // Public api endpoint for get and post requests
        // http://webcode.me
        // https://httpbin.org/post
        if(request != null) {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            responseText.setText("HTTP response is: \n" + response.body());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> requestTypes = FXCollections.observableArrayList(
                "Get", "Post", "Delete");

        requestType.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(t1 == "Post") {
                    postPanel.setVisible(true);
                } else {
                    postPanel.setVisible(false);
                }
            }
        });
        requestType.setItems(requestTypes);
        requestType.setValue("Get");
    }
}