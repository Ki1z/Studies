package com.eiousee;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<Socket, String> socketMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(Config.SERVER_PORT);

        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("用户" + socket.getInetAddress().getHostAddress() + "已连接");
            new ServerThread(socket, socketMap).start();
        }
    }
}
