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
import org.json.JSONObject;

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
                }};

                var objectMapper = new ObjectMapper();
                String requestBody = objectMapper
                        .writeValueAsString(values);

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(body.getText());
                String json2 = json.replace("\\", "");
                String res = json2.substring(1, json2.length() - 1);

                request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText()))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(res))
                        .build();

            } else if(requestType.getValue() == "Delete") {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint.getText()))
                        .DELETE().build();
            }
        }

        // Public api endpoint for get and post requests
        // Get: https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita
        // http://webcode.me
        // Post: https://reqres.in/api/users
        // {"name": "MyName"}
        // https://httpbin.org/post
        if(request != null) {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(response.body());
            String prettyJsonString = gson.toJson(je);

            responseText.setText("HTTP response is: \n" + prettyJsonString);
            //responseText.setText("HTTP response is: \n" + response.body());
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
                    bodyPanel.setVisible(true);
                } else {
                    postPanel.setVisible(false);
                    bodyPanel.setVisible(false);
                }
            }
        });
        requestType.setItems(requestTypes);
        requestType.setValue("Get");
    }
}