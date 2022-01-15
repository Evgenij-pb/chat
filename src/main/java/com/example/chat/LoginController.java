package com.example.chat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class LoginController {
    static String userName;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField name_field;

    @FXML
    private Button connectBtn;

    @FXML
    void initialize() {

        name_field.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                openChatWindow();
            }
        });

        connectBtn.setOnAction(actionEvent -> {
            openChatWindow();
        });

    }
    void openChatWindow(){
        userName = name_field.getText(); //.trim();
        if (!userName.equals("")){

            connectBtn.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("chat.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(Login.class.getResourceAsStream("icon.png")));
            stage.setTitle("Сhat");
            stage.showAndWait();

        } //else
        //System.out.println("Введите имя");
    }
}
