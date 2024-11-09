package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;
import java.util.Vector;

public class DaftarKamar extends JPanel {
    private JTable table;
    private JComboBox<String> dropdownFilter;
    private Connection connection;
    private Frame frame;

    public DaftarKamar(Frame frame, Connection connection) {
        this.frame = frame;
        this.connection = connection;
        setLayout(new BorderLayout());

        JPanel dropdown = new JPanel();

        JLabel pilih = new JLabel("Pilih Tipe Kamar : ");
        dropdown.add(pilih);

        dropdownFilter = new JComboBox<>(new String[] {"Semua", "Kamar Single", "Kamar Standard", "Kamar Deluxe", "Kamar Luxury"});
        dropdownFilter.addActionListener(e -> listKamar());
        dropdown.add(dropdownFilter);

        add(dropdown, BorderLayout.NORTH);

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel tombol = new JPanel(new FlowLayout());

        JButton logout = new JButton("Keluar");
        logout.addActionListener(e -> frame.switchScreen("Login"));
        tombol.add(logout);

        JButton pesanKamar = new JButton("Pesan Kamar");
        pesanKamar.addActionListener(e -> pesanKamar());
        tombol.add(pesanKamar);

        JButton daftarPesanan = new JButton("Daftar Reservasi");
        daftarPesanan.addActionListener(e -> frame.switchScreen("KamarReservasi"));
        tombol.add(daftarPesanan);

        add(tombol, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                listKamar();
            }
        });
    }

    private void listKamar() {
        String filter = (String) dropdownFilter.getSelectedItem();
        String filterQuery = "";
        if (!filter.equals("Semua")) {
            filterQuery = " AND tipeKamar = '" + filter + "'";
        }
        String query = "SELECT id, tipeKamar FROM public.kamar WHERE status = 'Tersedia'" + filterQuery + "ORDER BY id ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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

    private void pesanKamar() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih kamar untuk dipesan");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String user = frame.getLoginUser();

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE public.kamar SET tamu = ?, status = 'Terpesan' WHERE id = ?")) {
            stmt.setString(1, user);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Reservasi kamar berhasil!");
            listKamar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

