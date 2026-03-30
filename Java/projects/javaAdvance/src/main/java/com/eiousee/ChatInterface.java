package com.eiousee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatInterface extends JFrame {

    private JTextArea messageArea;      // 聊天记录显示区
    private JTextArea inputField;       // 消息输入框
    private JButton sendButton;
    private JList<String> userList;     // 成员列表
    private DefaultListModel<String> userListModel;
    private Socket socket;
    private String username;

    public ChatInterface(Socket socket, String username) {
        this();
        this.socket = socket;
        this.username = username;
        this.setTitle(username + " - 聊天室");
        new ClientReaderThread(socket, username, this).start();
    }

    public void appendMessage(String message) {
        messageArea.append(message);
    }

    public ChatInterface() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setupListeners();
    }

    private void initComponents() {
        messageArea = new JTextArea();
        messageArea.setEditable(false); 
        messageArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        messageArea.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        inputField = new JTextArea();
        inputField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputField.setLineWrap(true); 
        JScrollPane inputScrollPane = new JScrollPane(inputField);

        sendButton = new JButton("发送");
        sendButton.setPreferredSize(new Dimension(80, 30));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userListScrollPane = new JScrollPane(userList);
        userListScrollPane.setBorder(BorderFactory.createTitledBorder("在线成员"));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(messageScrollPane, BorderLayout.CENTER);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(150, 0)); 
        rightPanel.add(userListScrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void setupListeners() {
        sendButton.addActionListener(e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                onSendMessage(text);
                inputField.setText("");
            }
        });

        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    onUserSelected(selectedUser);
                }
            }
        });
    }

    protected void onSendMessage(String message) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(1);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd   HH:mm:ss");
            String content = username + "   " + now.format(formatter) + "\r\n     " + message + "\r\n\n";
            System.out.println(username + "发送了一条群聊消息");
            dos.writeUTF(content);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onUserSelected(String username) {}

    protected void setUserList(String[] users) {
        userList.setListData(users);
    }
}