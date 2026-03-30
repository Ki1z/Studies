package com.eiousee;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class ClientReaderThread extends Thread{
    private final ChatInterface chatInterface;
    private final Socket socket;
    private final String username;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientReaderThread(Socket socket, String username, ChatInterface chatInterface) {
        this.socket = socket;
        this.username = username;
        this.chatInterface = chatInterface;
    }

    @Override
    public void run() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            while (true) {
                int type = dis.readInt();
                switch (type) {
                    // 用户列表更新
                    case 1:
                        System.out.println("收到用户列表更新");
                        updateUserList();
                        break;
                    // 群聊消息
                    case 2:
                        System.out.println("收到群聊消息");
                        updateMessageQueue();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserList() {
        try {
            System.out.println("开始更新用户列表");
            int count = dis.readInt();
            System.out.println("用户数量：" + count);
            String[] users = new String[count];
            for (int i = 0; i < count; i++) {
                users[i] = dis.readUTF();
            }
            chatInterface.setUserList(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMessageQueue() {
        try {
            System.out.println("开始更新消息队列");
            String message = dis.readUTF();
            chatInterface.appendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
