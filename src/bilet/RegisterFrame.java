package bilet;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

public class RegisterFrame extends JFrame {
    private JTextField txtAdSoyad;
    private JPasswordField txtSifre;

    public RegisterFrame() {
        getContentPane().setBackground(new Color(215, 206, 206));
        setTitle("Üye Ol");
        setSize(500, 400);
        getContentPane().setLayout(null);

        JLabel lblAd = new JLabel("Ad Soyad:");
        lblAd.setFont(new Font("Serif", Font.BOLD, 20));
        lblAd.setBounds(50, 92, 120, 30);
        getContentPane().add(lblAd);

        txtAdSoyad = new JTextField();
        txtAdSoyad.setBounds(180, 91, 250, 40);
        getContentPane().add(txtAdSoyad);

        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setFont(new Font("Serif", Font.BOLD, 20));
        lblSifre.setBounds(50, 168, 120, 30);
        getContentPane().add(lblSifre);

        txtSifre = new JPasswordField();
        txtSifre.setBounds(180, 167, 250, 40);
        getContentPane().add(txtSifre);

        JButton btnKayit = new JButton("Kaydol");
        btnKayit.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnKayit.setBackground(new Color(163, 194, 179));
        btnKayit.setBounds(166, 261, 150, 50);
        getContentPane().add(btnKayit);

        btnKayit.addActionListener(e -> {
            String adSoyad = txtAdSoyad.getText();
            String sifre = new String(txtSifre.getPassword());

            UserService.register(adSoyad, sifre);
            JOptionPane.showMessageDialog(this, "Üyelik başarılı");
            dispose(); // pencereyi kapat
        });
        this.setLocationRelativeTo(null); // Pencereyi ekranın merkezine konumlandırır
        setVisible(true);
    }
}
