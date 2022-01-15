package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;


public class ChatController {
    private static PrintWriter outServer;
    private static BufferedReader inServer;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField msg_field;

    @FXML
    private Button sendMsgBtn;

    @FXML
    private TextArea chatMsg_field;

    @FXML
    void initialize() {

        String serverIp = "127.0.0.1";
        int serverPort = 1234;

        Socket server;

        try {
            server = new Socket(serverIp, serverPort);
            inServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
            outServer = new PrintWriter(server.getOutputStream(), true);

            outServer.println(LoginController.userName); //Отправляем первое сообщение серверу,
            // сервер принимает первое сообщение за имя пользователя

            Runnable readMsg = () -> {
                try {
                    while (true) {

                        String msgFromServer = inServer.readLine();
                        chatMsg_field.appendText(msgFromServer + "\n");
                    }

                } catch (IOException e) {
                    //errorMsgWindow("Связь с сервером потеряна"); error Not on FX application thread;
                    System.out.println("ошибка подключения");
                    System.exit(-1);
                }
            };

            Thread readerMsgFromServer = new Thread(readMsg);
            readerMsgFromServer.start();

        } catch (IOException e) {
            errorMsgWindow("Ошибка подключения к серверу");
            System.exit(-1);
        }


        sendMsgBtn.setOnAction(actionEvent -> {
            enterMSg();
        });

        msg_field.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                enterMSg();
            }
        });
    }

    void enterMSg() {
        String dataFromUser;
        if ((dataFromUser = msg_field.getText()) != null) {
            outServer.println(dataFromUser);
            msg_field.setText("");
        }
    }

    void errorMsgWindow(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(errorMsg);
        alert.showAndWait();
    }
}
