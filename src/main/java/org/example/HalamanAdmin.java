package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class HalamanAdmin extends JPanel {
    private Frame frame;
    private Connection connection;
    private JTable table;

    public HalamanAdmin(Frame frame, Connection connection) {
        this.frame = frame;
        this.connection = connection;

        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel tombol = new JPanel(new FlowLayout());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                listReservasi();
            }
        });

        JButton daftarPesanan = new JButton("Keluar");
        daftarPesanan.addActionListener(e -> frame.switchScreen("Login"));
        tombol.add(daftarPesanan);

        JButton batalPesanan = new JButton("Batalkan Reservasi");
        batalPesanan.addActionListener(e -> batalReservasi());
        tombol.add(batalPesanan);

        add(tombol, BorderLayout.SOUTH);

    }

    private void listReservasi() {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT id, tipeKamar, tamu FROM public.kamar WHERE status = 'Terpesan'")) {

            ResultSet rs = stmt.executeQuery();

            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> columnNames = new Vector<>();
            columnNames.add("No. Kamar");
            columnNames.add("Tipe Kamar");
            columnNames.add("Tamu");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("tipeKamar"));
                row.add(rs.getString("tamu"));
                data.add(row);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void batalReservasi() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih reservasi yang ingin dibatalkan");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE public.kamar SET tamu = '', status = 'Tersedia' WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pembatalan reservasi berhasil!");
            listReservasi();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
