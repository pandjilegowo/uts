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

public class KamarReservasi extends JPanel {

    private Frame frame;
    private Connection connection;
    private JTable table;

    public KamarReservasi(Frame frame, Connection connection) {
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

        JButton daftarPesanan = new JButton("Kembali");
        daftarPesanan.addActionListener(e -> frame.switchScreen("DaftarKamar"));
        tombol.add(daftarPesanan);

        JButton batalPesanan = new JButton("Batalkan Reservasi");
        batalPesanan.addActionListener(e -> batalReservasi());
        tombol.add(batalPesanan);

        add(tombol, BorderLayout.SOUTH);

    }

    private void listReservasi() {
        String user = frame.getLoginUser();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT id, tipeKamar FROM public.kamar WHERE tamu = ? AND status = 'Terpesan'")) {

            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();

            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> columnNames = new Vector<>();
            columnNames.add("No. Kamar");
            columnNames.add("Tipe Kamar");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("tipeKamar"));
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
