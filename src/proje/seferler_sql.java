package proje;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class seferler_sql {

    private String kullaniciAdi = "root"; 
    private String sifre = "Acar0110."; 
    private String dbIsmi = "proje"; 
    private String host = "localhost"; 
    private int port = 3306;
    
    public Connection baglan() {
        Connection conn = null;
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
           
            String url = "jdbc:mysql://localhost:" + port + "/" + dbIsmi + "?useUnicode=true&characterEncoding=utf8";
            
            conn = DriverManager.getConnection(url, kullaniciAdi, sifre);
           
        } catch (ClassNotFoundException e) {
            System.out.println("Sürücü bulunamadı: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantı hatası: " + e.getMessage());
        }
        
        return conn;
    }
}