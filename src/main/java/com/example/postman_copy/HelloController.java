package com.example.postman_copy;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
import com.google.gson.Gson;

public class HelloController implements Initializable {

    @FXML
    private TextArea body;

    @FXML
    private Pane bodyPanel;

    @FXML
    private TextField endpoint;

    @FXML
    private ChoiceBox<String> requestType;

    @FXML
    private TextArea responseText;

    @FXML
    private TextField headerKey1;

    @FXML
    private TextField headerValue1;

    @FXML
    private TextField headerKey2;

    @FXML
    private TextField headerValue2;

    @FXML
    private TextField headerKey3;

    @FXML
    private TextField headerValue3;

    @FXML
    private TextField headerKey4;

    @FXML
    private TextField headerValue4;


    @FXML
    protected void sendHttpRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if(!endpoint.getText().isEmpty()) {
            // Public endpoints for testing get request
            // https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita
            // https://api2.binance.com/api/v3/ticker/24hr
            if (requestType.getValue() == "Get") {
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText().trim()));

                if(headerKey1.getText().length() > 0 && headerValue1.getText().length() > 0) {
                    requestBuilder.header(headerKey1.getText(), headerValue1.getText());
                }
                if(headerKey2.getText().length() > 0 && headerValue2.getText().length() > 0) {
                    requestBuilder.header(headerKey2.getText(), headerValue2.getText());
                }
                if(headerKey3.getText().length() > 0 && headerValue3.getText().length() > 0) {
                    requestBuilder.header(headerKey3.getText(), headerValue3.getText());
                }
                if(headerKey4.getText().length() > 0 && headerValue4.getText().length() > 0) {
                    requestBuilder.header(headerKey4.getText(), headerValue4.getText());
                }

                request = requestBuilder.build();

            } else if (requestType.getValue() == "Post") {
                // Get key value pairs from the user
                /*HashMap values = new HashMap<String, String>() {{
                    put(key1.getText(), value1.getText());
                }};

                var objectMapper = new ObjectMapper();
                String requestBody = objectMapper
                        .writeValueAsString(values); */

                // Public endpoints for testing post request
                // https://reqres.in/api/users
                // Body: { "name": "MyName", "name2": "MyName2" }
                // Headers: Content-Type, application/json

                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText().trim()));

                if(headerKey1.getText().length() > 0 && headerValue1.getText().length() > 0) {
                    requestBuilder.header(headerKey1.getText(), headerValue1.getText());
                }
                if(headerKey2.getText().length() > 0 && headerValue2.getText().length() > 0) {
                    requestBuilder.header(headerKey2.getText(), headerValue2.getText());
                }
                if(headerKey3.getText().length() > 0 && headerValue3.getText().length() > 0) {
                    requestBuilder.header(headerKey3.getText(), headerValue3.getText());
                }
                if(headerKey4.getText().length() > 0 && headerValue4.getText().length() > 0) {
                    requestBuilder.header(headerKey4.getText(), headerValue4.getText());
                }

                request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body.getText())).build();

            } else if(requestType.getValue() == "Delete") {
                HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText().trim()));

                if(headerKey1.getText().length() > 0 && headerValue1.getText().length() > 0) {
                    requestBuilder.header(headerKey1.getText(), headerValue1.getText());
                }
                if(headerKey2.getText().length() > 0 && headerValue2.getText().length() > 0) {
                    requestBuilder.header(headerKey2.getText(), headerValue2.getText());
                }
                if(headerKey3.getText().length() > 0 && headerValue3.getText().length() > 0) {
                    requestBuilder.header(headerKey3.getText(), headerValue3.getText());
                }
                if(headerKey4.getText().length() > 0 && headerValue4.getText().length() > 0) {
                    requestBuilder.header(headerKey4.getText(), headerValue4.getText());
                }

                request = requestBuilder.DELETE().build();
            }
        }

        if(request != null) {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if(response.body().startsWith("{") || response.body().startsWith("[")){
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                JsonElement je = JsonParser.parseString(response.body());
                String prettyJsonString = gson.toJson(je);

                responseText.setText("HTTP response is: \n" + prettyJsonString);
            } else {
                responseText.setText("HTTP response is: \n" + response.body());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> requestTypes = FXCollections.observableArrayList(
                "Get", "Post", "Delete");

        requestType.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(t1 == "Post") {
                    bodyPanel.setVisible(true);
                    cleanUp();
                } else {
                    bodyPanel.setVisible(false);
                    cleanUp();
                }
            }
        });
        requestType.setItems(requestTypes);
        requestType.setValue("Get");
    }

    public void cleanUp() {
        body.setText("");
        headerKey1.setText("");
        headerKey2.setText("");
        headerValue1.setText("");
        headerValue2.setText("");
        endpoint.setText("");
    }
}