package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


public class Server {

    public static LinkedList<Handler> handlerList = new LinkedList<>();

    static class Handler extends Thread {

        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                start();
            } catch (IOException e) {
                System.out.println("Не могу установить соединение");
                //System.exit(-1);
            }
        }

        @Override
        public void run() {

            String clientMessage, userName;
            // цикл ожидания сообщений от клиента

            try {

                if ((userName = in.readLine()) != null) {
                    System.out.println(userName + " подключен, ожидаем сообщений");
                    for (Handler h : handlerList) {
                        h.send(userName + " теперь в чате");
                    }
                }

                while ((clientMessage = in.readLine()) != null) {

                    Date dateNow = new Date();
                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("[dd.MM.yyyy k:mm]");
                    clientMessage = formatForDateNow.format(dateNow) + " " + userName + ": " + clientMessage;

                    for (Handler h : handlerList) {
                        h.send(clientMessage);
                        //out.println(clientMessage);
                    }
                }
            } catch (IOException e) {
                System.out.println("связь с пользователем потеряна");
                //System.exit(-1);
                //socket.close();
            }

        }
        private void send(String clientMessage) {
            out.println(clientMessage);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 1234;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Сервер запущен на порту : " + port);
        //ExecutorService threadPool = Executors.newFixedThreadPool(10);
        System.out.println("Ожидаем клиентов");
        // В цикле ждем запроса клиента
        while (true) {

            //threadPool.submit(new Handler(clientSocket));
            try {
                Socket clientSocket = serverSocket.accept();
                handlerList.add(new Handler(clientSocket));

            } catch (IOException e) {

                serverSocket.close();
            }
        }
    }
}