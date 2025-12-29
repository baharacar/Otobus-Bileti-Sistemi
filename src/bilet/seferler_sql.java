package bilet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class seferler_sql {

	private static final String KULLANICI_ADI = "root"; 
    private static final String SIFRE = "Betul.1137"; 
    private static final String DB_ISMI = "bilet_sistemi"; 
    private static final String HOST = "localhost"; 
    private static final int PORT = 3307; 
    
    public static Connection baglan() {
        Connection conn = null;
        try {
 
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Bağlantı URL'si (Türkçe karakter desteği ile)
            String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_ISMI + 
                         "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
            
            conn = DriverManager.getConnection(url, KULLANICI_ADI, SIFRE);
            System.out.println("Bağlantı Başarılı: " + DB_ISMI);
            
        } catch (ClassNotFoundException e) {
            System.err.println("HATA: MySQL Driver bulunamadı! (Connector JAR dosyasını kontrol et)");
        } catch (SQLException e) {
            System.err.println("HATA: Veritabanına bağlanılamadı! Mesaj: " + e.getMessage());
        }
        
        return conn;
    }

}


