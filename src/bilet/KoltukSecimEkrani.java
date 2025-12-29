package bilet;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KoltukSecimEkrani extends JFrame {

    //Veritabanı
    private final String DB_URL = "jdbc:mysql://localhost:3307/bilet_sistemi"; 
    private final String DB_USER = "root";
    private final String DB_PASS = "Wun1quew."; 

    private static Map<Integer, String> DOLU_KOLTUKLAR = new HashMap<>();
    private Map<Integer, String> anlikSepet = new HashMap<>(); 
    
    private int gelenSeferID;
    private String seferGuzergah = "Bilinmiyor";
    private String seferTarih = "-";
    private String seferSaat = "-";
    private double seferBirimFiyat = 0.0;

    private JRadioButton rdBay;
    private JRadioButton rdBayan;
    private JPanel koltukPanel;

    public KoltukSecimEkrani(int seferID) {
        this.gelenSeferID = seferID;
        
        seferBilgileriniGetir();
        doluKoltuklariCek();

        setTitle("Koltuk Seçimi - " + seferGuzergah);
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        getContentPane().setLayout(new BorderLayout());

        //ÜST 
        JPanel topPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        topPanel.setBackground(new Color(215, 206, 206));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setOpaque(false);
        JButton btnGeri = new JButton("<< Sefer Seçimine Dön");
        btnGeri.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnGeri.setBackground(new Color(180, 180, 180));
        btnGeri.addActionListener(e -> {
            new SeferSecimEkrani().setVisible(true);
            this.dispose(); 
        });
        navPanel.add(btnGeri);
        topPanel.add(navPanel); 
        
        JLabel lblBaslik = new JLabel("SEFER: " + seferGuzergah + " | " + seferSaat, SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(lblBaslik);

        JLabel lblBilgi = new JLabel("Lütfen Cinsiyet Seçip Koltuğa Tıklayınız", SwingConstants.CENTER);
        lblBilgi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBilgi.setForeground(new Color(200, 0, 0));
        topPanel.add(lblBilgi);
        
        //Cinsiyet Butonları
        JPanel cinsiyetPanel = new JPanel();
        cinsiyetPanel.setOpaque(false);
        ButtonGroup grup = new ButtonGroup();
        rdBay = new JRadioButton("ERKEK (Mavi)");
        rdBay.setBackground(new Color(163, 194, 179));
        rdBayan = new JRadioButton("KADIN (Pembe)");
        rdBayan.setBackground(new Color(163, 194, 179));
        rdBay.setSelected(true); 
        rdBay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rdBayan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        grup.add(rdBay); grup.add(rdBayan);
        cinsiyetPanel.add(rdBay); cinsiyetPanel.add(rdBayan);
        topPanel.add(cinsiyetPanel);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        //KOLTUKLAR
        koltukPanel = new JPanel(new GridLayout(13, 4, 10, 10));
        koltukPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        koltukPanel.setBackground(Color.WHITE);
        koltuklariCiz();
        getContentPane().add(new JScrollPane(koltukPanel), BorderLayout.CENTER);

        //DEVAM BUTONU 
        JButton btnDevam = new JButton("SEÇİMİ TAMAMLA VE DEVAM ET >>");
        btnDevam.setBackground(new Color(163, 194, 179));
        btnDevam.setForeground(new Color(0, 0, 0));
        btnDevam.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDevam.setPreferredSize(new Dimension(100, 65));
        
        btnDevam.addActionListener(e -> {
            if (anlikSepet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen en az bir koltuk seçiniz!");
            } else {
                verileriGonderVeIlerle();
            }
        });
        getContentPane().add(btnDevam, BorderLayout.SOUTH);
    }

    private void seferBilgileriniGetir() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM uygun_seferler WHERE SeferID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, gelenSeferID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                this.seferGuzergah = rs.getString("Güzergah");
                this.seferTarih = rs.getString("Tarih");
                this.seferSaat = rs.getString("Saat");
                this.seferBirimFiyat = rs.getDouble("Fiyat");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void doluKoltuklariCek() {
        DOLU_KOLTUKLAR.clear(); 
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT koltuk FROM onaylanmis_biletler WHERE guzergah = ? AND tarih = ? AND saat = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, this.seferGuzergah);
            pstmt.setString(2, this.seferTarih);
            pstmt.setString(3, this.seferSaat);
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String dbKoltuk = rs.getString("koltuk"); 
                if(dbKoltuk != null && !dbKoltuk.isEmpty()) {
                    String[] parcalar = dbKoltuk.split(",");
                    for(String p : parcalar) {
                        p = p.trim();
                        if(p.contains("(") && p.contains(")")) {
                            int ac = p.indexOf("(");
                            int kapat = p.indexOf(")");
                            int no = Integer.parseInt(p.substring(0, ac));
                            String cins = p.substring(ac + 1, kapat);
                            DOLU_KOLTUKLAR.put(no, cins.toUpperCase());
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void verileriGonderVeIlerle() {
        String koltukDetayli = anlikSepet.entrySet().stream()
                .map(entry -> entry.getKey() + "(" + entry.getValue() + ")") 
                .collect(Collectors.joining(", "));
        
        double toplamFiyat = anlikSepet.size() * seferBirimFiyat;

        BiletOnay.secilenGuzergah = this.seferGuzergah;
        BiletOnay.secilenTarih = this.seferTarih;
        BiletOnay.secilenSaat = this.seferSaat;
        BiletOnay.secilenKoltuk = koltukDetayli; 
        BiletOnay.secilenFiyat = String.valueOf(toplamFiyat);

        new BiletOnay().setVisible(true);
        this.dispose(); 
    }

    private void koltuklariCiz() {
        koltukPanel.removeAll();
        int sayac = 1;
        for (int i=0; i<13; i++) {
            koltukPanel.add(koltukOlustur(sayac++));
            koltukPanel.add(new JLabel("")); // Koridor
            koltukPanel.add(koltukOlustur(sayac++));
            koltukPanel.add(koltukOlustur(sayac++));
        }
    }

    private JButton koltukOlustur(int no) {
        JButton btn = new JButton(String.valueOf(no));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // RENKLENDİRME MANTIĞI
        if (DOLU_KOLTUKLAR.containsKey(no)) {
            String cinsiyet = DOLU_KOLTUKLAR.get(no);
            if (cinsiyet.equals("ERKEK")) {
                btn.setBackground(new Color(109, 147, 197)); // Koyu Mavi
            } else {
                btn.setBackground(new Color(213, 149, 213)); // Koyu Pembe
            }
            btn.setForeground(Color.WHITE);
            btn.setEnabled(false); 
        } else if (anlikSepet.containsKey(no)) {
            String seciliCinsiyet = anlikSepet.get(no);
            if (seciliCinsiyet.equals("ERKEK")) {
                btn.setBackground(Color.blue); // Seçilen Bay
            } else {
                btn.setBackground(Color.pink); // Seçilen Bayan
            }
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
        }
        
        btn.addActionListener(e -> {
            if (anlikSepet.containsKey(no)) {
                anlikSepet.remove(no);
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            } else {
                String secim = rdBay.isSelected() ? "ERKEK" : "KADIN";
                if (yanKoltukUygunMu(no, secim)) {
                    anlikSepet.put(no, secim);
                    if (secim.equals("ERKEK")) btn.setBackground(Color.blue);
                    else btn.setBackground(Color.pink);
                    btn.setForeground(Color.WHITE);
                } else {
                    JOptionPane.showMessageDialog(this, "HATA: Yan koltuk cinsiyet uyuşmazlığı!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        return btn;
    }

    private boolean yanKoltukUygunMu(int koltukNo, String benimCinsiyetim) {
        int yanKoltukNo = -1;
        if (koltukNo % 3 == 1) return true;
        if (koltukNo % 3 == 2) yanKoltukNo = koltukNo + 1;
        if (koltukNo % 3 == 0) yanKoltukNo = koltukNo - 1;

        if (anlikSepet.containsKey(yanKoltukNo)) return true; 
        
        if (DOLU_KOLTUKLAR.containsKey(yanKoltukNo)) {
            String yandakiCinsiyet = DOLU_KOLTUKLAR.get(yanKoltukNo);
            if (!yandakiCinsiyet.equals(benimCinsiyetim)) return false; 
        }
        return true; 
    }
}