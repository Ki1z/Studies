package com.eiousee;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;

public class ServerThread extends Thread{
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String username;
    private Map<Socket, String> socketMap;

    public ServerThread(Socket socket, Map<Socket, String> socketMap) {
        this.socket = socket;
        this.socketMap = socketMap;
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            while(true) {
                int type = dis.readInt();
                switch (type) {
                    // 用户名检查
                    case 0:
                        String username = dis.readUTF();
                        isUsernameExist(username);
                        break;
                    // 接收群聊消息
                    case 1:
                        String message = dis.readUTF();
                        System.out.println("收到群聊消息：" + message);
                        sendMessageToAll(message);
                        break;
                }
            }
        } catch (Exception e) {
            socketMap.remove(socket);
            if (username != null) {
                System.out.println("用户" + username + "已下线");
                try {
                    getAllOnlineUsers();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void isUsernameExist(String username) throws Exception {
        if (socketMap.containsValue(username)) {
            dos.writeInt(1);
        } else {
            dos.writeInt(0);
            socketMap.put(socket, username);
            this.username = username;
            System.out.println("用户" + username + "已上线");
            socketMap.put(socket, username);
            getAllOnlineUsers();
        }
    }

    private void getAllOnlineUsers() throws Exception {
        for (Socket socket : socketMap.keySet()) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(1);
            dos.writeInt(socketMap.size());
            for (String username : socketMap.values()) {
                dos.writeUTF(username);
            }
            dos.flush();
        }
    }

    private void sendMessageToAll(String message) throws Exception {
        for (Socket socket : socketMap.keySet()) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(2);
            dos.writeUTF(message);
            dos.flush();
        }
    }
}
