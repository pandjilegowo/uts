package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HalamanLogin extends JPanel {
    private JTextField fieldNama;
    private JPasswordField fieldPassword;
    private Connection connection;
    private Frame frame;

    public HalamanLogin(Frame frame, Connection connection) {
        this.frame = frame;
        this.connection = connection;

        setLayout(new GridLayout(3, 2, 10, 50));
        setBorder(BorderFactory.createEmptyBorder(50, 30,60,30));

        add(new JLabel("Nama :"));
        fieldNama = new JTextField();
        add(fieldNama);

        add(new JLabel("Password :"));
        fieldPassword = new JPasswordField();
        add(fieldPassword);

        JButton tombolDaftar = new JButton("Daftar");
        tombolDaftar.addActionListener(e -> daftar());
        add(tombolDaftar);

        JButton tombolLogin = new JButton("Login");
        tombolLogin.addActionListener(e -> login());
        add(tombolLogin);
    }

    private void daftar(){
        String username = fieldNama.getText();
        String password = new String(fieldPassword.getPassword());

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO public.user (username, password) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User berhasil terdaftar!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "User sudah terdaftar, pendaftaran gagal.");
            e.printStackTrace();
        }
    }

    private void login(){
        String username = fieldNama.getText();
        String password = new String(fieldPassword.getPassword());

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM public.user WHERE username=? AND password=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                frame.setLoginUser(username);
                JOptionPane.showMessageDialog(this, "Login berhasil!");
                if(username.equals("admin")){
                    frame.switchScreen("HalamanAdmin");
                } else {
                    frame.switchScreen("DaftarKamar");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal, pastikan username dan password sesuai.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

