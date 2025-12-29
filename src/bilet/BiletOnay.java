package bilet;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BiletOnay extends JFrame {
    
    public static String secilenTarih ;
    public static String secilenSaat;
    public static String secilenGuzergah ;
    public static String secilenFiyat ;
    public static String secilenKoltuk ;

    private JPanel contentPane;
    private JButton btnOnayla;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BiletOnay frame = new BiletOnay();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BiletOnay() {
        setTitle("Bilet Satın Alma Onayı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 538, 421);
        setLocationRelativeTo(null); 

        contentPane = new JPanel(new GridLayout(8, 2, 10, 10));
        contentPane.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPane.setBackground(new Color(215, 206, 206));
        setContentPane(contentPane);
        
        JLabel lblBaslik = new JLabel("BİLET BİLGİLERİ");
        lblBaslik.setForeground(Color.BLACK); 
        lblBaslik.setFont(new Font("Bookman Old Style", Font.BOLD | Font.ITALIC, 26));
        lblBaslik.setHorizontalAlignment(SwingConstants.CENTER);
        
        contentPane.add(lblBaslik); 
        contentPane.add(new JLabel(""));
        
        // pencere büyütülüp küçültüldğünde ekrandakilerin de aynı oranda hareket etmesi için olay dinleyici
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                oranliFontAyari();
            }
        });

        bilesenEkle("Güzergah:", secilenGuzergah);
        bilesenEkle("Tarih:", secilenTarih);
        bilesenEkle("Saat:", secilenSaat);
        bilesenEkle("Koltuk No:", secilenKoltuk);
        bilesenEkle("Fiyat:", secilenFiyat + " TL");

        contentPane.add(new JLabel("")); 
        btnOnayla = new JButton("Bileti Onayla");
        btnOnayla.setBackground(new Color(163, 194, 179)); 
        btnOnayla.setForeground(Color.BLACK);
        btnOnayla.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnOnayla.setBorder(BorderFactory.createLineBorder(new Color(60, 30, 20), 1));
        btnOnayla.setFocusPainted(false);
        contentPane.add(btnOnayla);
        
        btnOnayla.addActionListener(e -> biletKaydet());
    }

    private void oranliFontAyari() {
        int yeniBoyut = getWidth() / 25; 
        if (yeniBoyut < 14) yeniBoyut = 14; 

        Font dinamikFont = new Font("SansSerif", Font.BOLD, yeniBoyut);

        for (Component c : contentPane.getComponents()) {
            c.setFont(dinamikFont);
        }
    }

    private void bilesenEkle(String baslik, String veri) {
        JLabel lblBaslik = new JLabel(baslik);
        lblBaslik.setForeground(Color.DARK_GRAY);
        JLabel lblVeri = new JLabel(veri);
        lblVeri.setForeground(Color.black);
        
        contentPane.add(lblBaslik);
        contentPane.add(lblVeri);
    }

    private void biletKaydet() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/bilet_sistemi", "root", "Betul.1137");
            String query = "INSERT INTO onaylanmis_biletler (guzergah,tarih,saat,koltuk,fiyat) VALUES(?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, secilenGuzergah);
            pst.setString(2, secilenTarih);
            pst.setString(3, secilenSaat);
            pst.setString(4, secilenKoltuk);
            pst.setString(5, secilenFiyat);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "İşlem Başarılı! İyi Yolculuklar.");
            conn.close();
            
            BiletListesi liste = new BiletListesi();
            liste.setVisible(true);

            
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
        }
    }
}