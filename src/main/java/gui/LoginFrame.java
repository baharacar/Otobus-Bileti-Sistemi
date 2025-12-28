import Service.UserService;

import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField txtAdSoyad;
    private JPasswordField txtSifre;

    public LoginFrame() {
        setTitle("Giriş");
        setSize(300, 220);
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

        JButton btnGiris = new JButton("Giriş");
        btnGiris.setBounds(30, 120, 100, 30);
        add(btnGiris);

        JButton btnUyeOl = new JButton("Üye Ol");
        btnUyeOl.setBounds(150, 120, 100, 30);
        add(btnUyeOl);

        btnGiris.addActionListener(e -> {
            String adSoyad = txtAdSoyad.getText();
            String sifre = new String(txtSifre.getPassword());

            if (UserService.login(adSoyad, sifre)) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı");
                // burada arkadaşının ekranına geçilecek
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı giriş");
            }
        });

        btnUyeOl.addActionListener(e -> new RegisterFrame());

        setVisible(true);
    }
}
