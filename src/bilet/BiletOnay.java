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
        setBounds(100, 100, 500, 400);
        setLocationRelativeTo(null); // Merkezi açılış

        // gridLayout 6 satır 2 sütun ekranı tamamen doldurur
        contentPane = new JPanel(new GridLayout(8, 2, 10, 10));
        contentPane.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPane.setBackground(new Color(248, 245, 242));
        setContentPane(contentPane);
        
        //başlik
        JLabel lblBaslik = new JLabel("BİLET BİLGİLERİ");
        lblBaslik.setForeground(Color.BLACK); 
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBaslik.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        contentPane.add(lblBaslik); 
        contentPane.add(new JLabel("")); //başlığın yanındaki hücre boş 
        

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

        // boşluk ve onay butonu
        contentPane.add(new JLabel("")); //sol boşluk
        
        btnOnayla = new JButton("Bileti Onayla");
        btnOnayla.setBackground(new Color(101, 0, 0)); //bordo
        btnOnayla.setForeground(Color.WHITE);
        btnOnayla.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnOnayla.setBorder(BorderFactory.createLineBorder(new Color(60, 30, 20), 1));//buton kenarı rengi
        btnOnayla.setFocusPainted(false);
        contentPane.add(btnOnayla);
        
        //buton üstüne gelince renk değiştirme
        btnOnayla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //mouse gelince kahverengi yap
                btnOnayla.setBackground(new Color(60,30,20));
                btnOnayla.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 0), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //mouse ayrılınca tekrar bordo yap
                btnOnayla.setBackground(new Color(101, 0, 0));
            }
        });

        //veritabanı kayıt işlemi
        btnOnayla.addActionListener(e -> biletKaydet());
    }

    //ekrandaki tüm yazıların boyutunu pencereye göre günceller.dinleyici olayı haber verir bu metot yapar.
    private void oranliFontAyari() {
        int yeniBoyut = getWidth() / 25; //genişliğin 25te biri font boyutu
        if (yeniBoyut < 14) yeniBoyut = 14; //min font sınırı

        Font dinamikFont = new Font("SansSerif", Font.BOLD, yeniBoyut);

        //panel içindeki her şeyin fontunu tek tek güncelle
        for (Component c : contentPane.getComponents()) {
            c.setFont(dinamikFont);
        }
    }

    // yardımcı metod
    private void bilesenEkle(String baslik, String veri) {
        JLabel lblBaslik = new JLabel(baslik);
        lblBaslik.setForeground(Color.DARK_GRAY);
        JLabel lblVeri = new JLabel(veri);
        lblVeri.setForeground(Color.BLUE);
        
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
            //listeyi aç
            BiletListesi liste = new BiletListesi();
            liste.setVisible(true);

            //onay sayfasini kapat
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
        }
    }
}