package bilet;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SeferSecimEkrani extends JFrame {

    private JPanel contentPane;
    private JTable table_seferler;
    private JButton btn_ileri;
    private JComboBox<String> comboTarih;
    private JLabel lblGuzergahVeri;
    private int secilenSeferID = -1;
    
    // SABİT GÜZERGAH
    private final String SABIT_GUZERGAH = "İSTANBUL - ANKARA";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SeferSecimEkrani frame = new SeferSecimEkrani();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SeferSecimEkrani() {
        setTitle("Sefer Sorgulama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 650);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        // --- ÜST PANEL: FİLTRELEME ---
        JLabel lblGuzergah = new JLabel("Güzergah:");
        lblGuzergah.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGuzergah.setBounds(50, 30, 100, 30);
        contentPane.add(lblGuzergah);

        lblGuzergahVeri = new JLabel(SABIT_GUZERGAH);
        lblGuzergahVeri.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblGuzergahVeri.setBounds(150, 30, 250, 30);
        contentPane.add(lblGuzergahVeri);

        JLabel lblTarihSec = new JLabel("Tarih Seçiniz:");
        lblTarihSec.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTarihSec.setBounds(50, 80, 120, 30);
        contentPane.add(lblTarihSec);

        // 1 Haftalık Tarih Oluşturma
        comboTarih = new JComboBox<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            comboTarih.addItem(LocalDate.now().plusDays(i).format(dtf));
        }
        comboTarih.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboTarih.setBounds(170, 80, 150, 30);
        contentPane.add(comboTarih);

        JButton btnSorgula = new JButton("Seferleri Gör");
        btnSorgula.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSorgula.setBounds(340, 80, 150, 30);
        contentPane.add(btnSorgula);

        // --- TABLO ALANI ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 150, 700, 300);
        contentPane.add(scrollPane);

        table_seferler = new JTable();
        table_seferler.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table_seferler.setRowHeight(35);
        scrollPane.setViewportView(table_seferler);

        btn_ileri = new JButton("Koltuk Seçimi İçin İlerle >>");
        btn_ileri.setVisible(false);
        btn_ileri.setBackground(new Color(60, 179, 113));
        btn_ileri.setForeground(Color.WHITE);
        btn_ileri.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn_ileri.setBounds(250, 480, 300, 60);
        contentPane.add(btn_ileri);

        // --- EVENTLER ---
        btnSorgula.addActionListener(e -> seferleriFiltrele());

        table_seferler.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table_seferler.getSelectedRow();
                if (row >= 0) {
                    secilenSeferID = (int) table_seferler.getValueAt(row, 0);
                    btn_ileri.setVisible(true);
                }
            }
        });

        btn_ileri.addActionListener(e -> {
            KoltukSecimEkrani koltuk = new KoltukSecimEkrani(secilenSeferID);
            koltuk.setVisible(true);
            dispose();
        });
    }

    public void seferleriFiltrele() {
        String[] kolonlar = {"SeferID", "Güzergah", "Tarih", "Saat", "Fiyat"};
        DefaultTableModel model = new DefaultTableModel(kolonlar, 0);
        String secilenTarih = comboTarih.getSelectedItem().toString();

        try {
            Connection conn = seferler_sql.baglan();
            // Filtreleme sorgusu
            String sorgu = "SELECT * FROM uygun_seferler WHERE Güzergah = ? AND Tarih = ?";
            PreparedStatement pst = conn.prepareStatement(sorgu);
            pst.setString(1, SABIT_GUZERGAH);
            pst.setString(2, secilenTarih);
            
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] satir = {
                    rs.getInt("SeferID"),
                    rs.getString("Güzergah"),
                    rs.getString("Tarih"),
                    rs.getString("Saat"),
                    rs.getDouble("Fiyat")
                };
                model.addRow(satir);
            }
            
            table_seferler.setModel(model);
            
            // Tasarım ayarları
            table_seferler.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
            table_seferler.getColumnModel().getColumn(1).setPreferredWidth(200);
            
            if(model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Bu tarihe ait sefer bulunamadı.");
            }
            
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}