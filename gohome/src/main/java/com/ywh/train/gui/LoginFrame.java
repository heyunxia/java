package com.ywh.train.gui;

import com.ywh.train.Config;
import com.ywh.train.ResManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-12-09 下午6:03
 */
public class LoginFrame extends JFrame {

    public LoginFrame() throws HeadlessException {
        super("12306登录");

        setSize(500, 500);
        setLocationByPlatform(true);

        Panel panel = new Panel();
        panel.setLayout(null);

        JLabel txtUsername = new JLabel(ResManager.getString("RobTicket.txtUsername")); //用户名:
        txtUsername.setBounds(20, 20, 60, 25);
        panel.add(txtUsername);
        txtUsername.setHorizontalAlignment(4);

        JTextField userName = new JTextField(Config.getAccount());
        userName.setBounds(90, 20, 300, 25);
        panel.add(userName);
        userName.setColumns(10);

        JLabel txtPassword = new JLabel(ResManager.getString("RobTicket.txtPassword")); //密码:
        txtPassword.setBounds(20, 60, 60, 25);
        panel.add(txtPassword);
        txtPassword.setHorizontalAlignment(4);


        JPasswordField password = new JPasswordField(Config.getPassword());
        password.setBounds(90, 60, 300, 25);
        panel.add(password);
        password.setColumns(10);

        JLabel labelCode = new JLabel("验证码:"); //密码:
        labelCode.setBounds(20, 100, 60, 25);
        panel.add(labelCode);
        labelCode.setHorizontalAlignment(4);


        ZPanel zPanel = new ZPanel();
        zPanel.setBounds(90, 100, 293, 190);
        panel.add(zPanel);





        this.getContentPane().add(panel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new LoginFrame().setVisible(true);
    }
}
