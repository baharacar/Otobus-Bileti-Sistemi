package bilet;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

public class LoginFrame extends JFrame {
    private JTextField txtAdSoyad;
    private JPasswordField txtSifre;

    public LoginFrame() {
        getContentPane().setBackground(new Color(215, 206, 206));
        getContentPane().setFont(new Font("Book Antiqua", Font.BOLD | Font.ITALIC, 26));
        setTitle("Giriş");
        setSize(500, 400);
        getContentPane().setLayout(null);
        this.setLocationRelativeTo(null);

        Font sunumFontu = new Font("Tahoma", Font.BOLD, 18);

        JLabel lblAd = new JLabel("Ad Soyad:");
        lblAd.setFont(new Font("Serif", Font.BOLD, 18));
        lblAd.setBounds(50, 80, 120, 30);
        getContentPane().add(lblAd);

        txtAdSoyad = new JTextField();
        txtAdSoyad.setFont(sunumFontu);
        txtAdSoyad.setBounds(180, 80, 250, 40);
        getContentPane().add(txtAdSoyad);

        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setFont(new Font("Serif", Font.BOLD, 18));
        lblSifre.setBounds(50, 150, 120, 30);
        getContentPane().add(lblSifre);

        txtSifre = new JPasswordField();
        txtSifre.setFont(sunumFontu);
        txtSifre.setBounds(180, 150, 250, 40);
        getContentPane().add(txtSifre);

        JButton btnGiris = new JButton("Giriş");
        btnGiris.setBackground(new Color(163, 194, 179));
        btnGiris.setFont(sunumFontu);
        btnGiris.setBounds(80, 250, 150, 50);
        getContentPane().add(btnGiris);

        JButton btnUyeOl = new JButton("Üye Ol");
        btnUyeOl.setBackground(new Color(163, 194, 179));
        btnUyeOl.setFont(sunumFontu);
        btnUyeOl.setBounds(250, 250, 150, 50);
        getContentPane().add(btnUyeOl);

        JLabel lblNewLabel = new JLabel("MVBB BİLET SİSTEMİNE HOŞGELDİNİZ");
        lblNewLabel.setBackground(new Color(0, 0, 0));
        lblNewLabel.setFont(new Font("Book Antiqua", Font.BOLD | Font.ITALIC, 22));
        lblNewLabel.setBounds(32, 11, 446, 35);
        getContentPane().add(lblNewLabel);

        btnGiris.addActionListener(e -> {
            String adSoyad = txtAdSoyad.getText();
            String sifre = new String(txtSifre.getPassword());

            if (UserService.login(adSoyad, sifre)) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı");
                new SeferSecimEkrani().setVisible(true); // Sefer seçim ekranına yönlendir
                this.setLocationRelativeTo(null); // Pencereyi ekranın merkezine konumlandırır
                this.dispose(); // Giriş ekranını kapat
                // burada arkadaşının ekranına geçilecek
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı giriş");
            }
        });

        btnUyeOl.addActionListener(e -> new RegisterFrame());

        setVisible(true);
    }
    public static void main(String[] args) {
        // Önemli: UserService listesi boş olduğu için giriş yapamazsın.
        // Bu yüzden test amaçlı bir kullanıcı ekliyoruz:
        UserService.register("admin", "1234");

        // Pencereyi oluştur ve görünür yap
        EventQueue.invokeLater(() -> {
            try {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
