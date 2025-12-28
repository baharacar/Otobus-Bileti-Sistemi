import Service.UserService;

import javax.swing.*;

public class RegisterFrame extends JFrame {

    private JTextField txtAdSoyad;
    private JPasswordField txtSifre;

    public RegisterFrame() {
        setTitle("Üye Ol");
        setSize(300, 200);
        setLayout(null);

        JLabel lblAd = new JLabel("Ad Soyad:");
        lblAd.setBounds(20, 20, 80, 25);
        add(lblAd);

        txtAdSoyad = new JTextField();
        txtAdSoyad.setBounds(110, 20, 150, 25);
        add(txtAdSoyad);

        JLabel lblSifre = new JLabel("Şifre:");
        lblSifre.setBounds(20, 60, 80, 25);
        add(lblSifre);

        txtSifre = new JPasswordField();
        txtSifre.setBounds(110, 60, 150, 25);
        add(txtSifre);

        JButton btnKayit = new JButton("Kaydol");
        btnKayit.setBounds(90, 110, 100, 30);
        add(btnKayit);

        btnKayit.addActionListener(e -> {
            String adSoyad = txtAdSoyad.getText();
            String sifre = new String(txtSifre.getPassword());

            UserService.register(adSoyad, sifre);
            JOptionPane.showMessageDialog(this, "Üyelik başarılı");
            dispose(); // pencereyi kapat
        });

        setVisible(true);
    }
}
