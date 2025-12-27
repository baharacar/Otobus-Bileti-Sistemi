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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sadece bu pencereyi kapatır
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248,245,242));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(230, 230, 230)); //hafif gri arka plan
        headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblListeBaslik = new JLabel("ONAYLANMIŞ BİLETLER");
        lblListeBaslik.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblListeBaslik.setForeground(Color.BLACK);
        headerPanel.add(lblListeBaslik);

        // Sayfanın en üstüne ekle (Tablonun üstünde durur)
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        //yablo Sütunları
        String[] kolonlar = {"ID", "Güzergah", "Tarih", "Saat", "Koltuk", "Fiyat"};
        model = new DefaultTableModel(kolonlar, 0) {
        	@Override
        	public boolean isCellEditable(int row, int column) {
                //tüm hücreler için düzenlemeyi kapat (false döndür)
                return false;
        	}
        };
        	
        table = new JTable(model);
        
        //ekranla orantılı büyümesi için JScrollPane içine koyuyoruz
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        verileriGetir();
        
        table.setRowHeight(30); //satır genişliği
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15)); // Başlıkları kalın yap
        table.getTableHeader().setBackground(new Color(70, 70, 70)); // Tablo başlığı gri
        table.getTableHeader().setForeground(Color.WHITE); // Başlık yazısı Beyaz
        table.setSelectionBackground(new Color(230, 240, 250)); // Seçilen satır hafif turuncu/kahve
        table.setGridColor(new Color(220,220,220));
        
        
        // alt panel ve kapat butonu
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(248, 245, 242));
        buttonPanel.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnKapat = new JButton("Kapat");
        btnKapat.setPreferredSize(new Dimension(120, 35));
        btnKapat.setBackground(new Color(101, 0, 0)); //bordo
        btnKapat.setForeground(Color.WHITE);
        btnKapat.setFocusPainted(false);
        btnKapat.setFont(new Font("Tahoma", Font.BOLD, 12));

        
        btnKapat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnKapat.setBackground(new Color(60, 30, 20)); //mouse üzerine gelince kahverengi
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnKapat.setBackground(new Color(101, 0, 0)); //mouse ayrılınca bordo
            }
        });

        // Kapat Butonu Tıklama Olayı
        btnKapat.addActionListener(e -> dispose());

        buttonPanel.add(btnKapat);
        
    }

    private void verileriGetir() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/bilet_sistemi", "root", "Betul.1137");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM onaylanmis_biletler");
            table.getColumnModel().getColumn(0).setPreferredWidth(30); // ID küçük olsun
            table.getColumnModel().getColumn(1).setPreferredWidth(150); // Güzergah geniş olsun

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
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veriler çekilirken hata oluştu!");
        }
    }
}
	


