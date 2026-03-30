package com.eiousee;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JButton loginButton;
    private JButton exitButton;
    private DataOutputStream dos;
    private DataInputStream dis;

    public LoginFrame() {
        // 1. 窗口基本设置
        setTitle("用户登录");
        setSize(300, 130); // 高度稍微调小一点以适应单行输入
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 2. 初始化组件
        usernameField = new JTextField(15); // 设置列数为15，控制宽度
        loginButton = new JButton("登录");
        exitButton = new JButton("退出");

        // 3. 布局设计
        
        // --- 顶部面板：放置用户名输入 ---
        // 使用 BorderLayout 可以让 TextField 高度自动适应文字高度
        JPanel topPanel = new JPanel(new BorderLayout(10, 0)); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // 添加外边距
        topPanel.add(new JLabel("用户名:"), BorderLayout.WEST); // 标签靠左
        topPanel.add(usernameField, BorderLayout.CENTER);     // 输入框填满剩余空间

        // --- 底部面板：放置按钮 ---
        // FlowLayout 默认就是居中对齐的
        JPanel bottomPanel = new JPanel(); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // 底部留白
        bottomPanel.add(loginButton);
        bottomPanel.add(exitButton);

        // 4. 将面板添加到主窗口
        add(topPanel, BorderLayout.CENTER); // 输入区域在中间
        add(bottomPanel, BorderLayout.SOUTH); // 按钮区域在底部

        // 5. 事件监听
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty() || username.length() > 20 || username.contains(" ")) {
                JOptionPane.showMessageDialog(this, "非法用户名", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    // 建立与服务器的连接
                    Socket socket = new Socket(Config.SERVER_IP, Config.SERVER_PORT);
                    // 用户名验证
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    dos.writeInt(0);
                    dos.writeUTF(username);
                    dos.flush();

                    int result = dis.readInt();
                    if (result == 1) {
                        JOptionPane.showMessageDialog(this, "用户名已存在", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                    else if (result == 0){
                        // 登录成功，进入聊天界面
                        ChatInterface chatInterface = new ChatInterface(socket, username);
                        chatInterface.setVisible(true);
                        this.dispose(); // 关闭当前窗口
                    } else {
                        JOptionPane.showMessageDialog(this, "未知错误", "提示", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "服务器连接失败", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }
}