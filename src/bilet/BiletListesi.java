package bilet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class BiletListesi extends JFrame {
	private JTable table;
    private DefaultTableModel model;

    public BiletListesi() {
        setTitle("Sistemdeki Tüm Onaylanmış Biletler");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248,245,242));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(215, 206, 206)); 
        headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblListeBaslik = new JLabel("ONAYLANMIŞ BİLETLER");
        lblListeBaslik.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 26));
        lblListeBaslik.setForeground(Color.BLACK);
        headerPanel.add(lblListeBaslik);

        
        getContentPane().add(headerPanel, BorderLayout.NORTH);

       
        String[] kolonlar = {"ID", "Güzergah", "Tarih", "Saat", "Koltuk", "Fiyat"};
        model = new DefaultTableModel(kolonlar, 0) {
        	@Override
        	public boolean isCellEditable(int row, int column) {
               
                return false;
        	}
        };
        	
        table = new JTable(model);
        
        //ekranla orantılı büyümesi için JScrollPane içine koyuyoruz
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        verileriGetir();
        
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(70, 70, 70)); 
        table.getTableHeader().setForeground(Color.WHITE); 
        table.setSelectionBackground(new Color(230, 240, 250)); 
        table.setGridColor(new Color(220,220,220));
        
        JButton btnSeferlereDon = new JButton("Yeni Bilet / Seferlere Dön");
        btnSeferlereDon.setPreferredSize(new Dimension(200, 35));
        btnSeferlereDon.setBackground(new Color(70, 130, 180));
        btnSeferlereDon.setForeground(Color.WHITE);
        btnSeferlereDon.setFocusPainted(false);
        btnSeferlereDon.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        
        btnSeferlereDon.addActionListener(e -> {
            new SeferSecimEkrani().setVisible(true); 
            dispose(); 
        });
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(215, 206, 206));
        buttonPanel.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnKapat = new JButton("Kapat");
        btnKapat.setPreferredSize(new Dimension(120, 35));
        btnKapat.setBackground(new Color(163, 194, 179));
        btnKapat.setForeground(new Color(0, 0, 0));
        btnKapat.setFocusPainted(false);
        btnKapat.setFont(new Font("Tahoma", Font.BOLD, 12));

        btnKapat.addActionListener(e -> dispose());
        buttonPanel.add(btnSeferlereDon);

        buttonPanel.add(btnKapat);
        
    }

    private void verileriGetir() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/bilet_sistemi", "root", "Betul.1137");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM onaylanmis_biletler");
            
            while (rs.next()) {
                Object[] satir = {
                    rs.getInt("id"),
                    rs.getString("guzergah"),
                    rs.getString("tarih"),
                    rs.getString("saat"),
                    rs.getString("koltuk"),
                    rs.getString("fiyat")
                };
                model.addRow(satir);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veriler çekilirken hata oluştu!");
        }
    }
}