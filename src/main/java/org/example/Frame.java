package org.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Frame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panel;
    private String userLogin;
    private Connection connection;


    public Frame() {
        setTitle("Aplikasi Pemesanan Kamar");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/uts", "postgres", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        panel.add(new HalamanLogin(this, connection), "Login");
        panel.add(new DaftarKamar(this, connection), "DaftarKamar");
        panel.add(new KamarReservasi(this, connection), "KamarReservasi");
        panel.add(new HalamanAdmin(this, connection), "HalamanAdmin");

        add(panel);
        switchScreen("Login");
    }

    public void switchScreen(String screen) {
        cardLayout.show(panel, screen);
    }

    public void setLoginUser(String username) {
        this.userLogin = username;
    }

    public String getLoginUser() {
        return userLogin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame();
            frame.setVisible(true);
        });
    }
}

