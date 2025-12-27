package Model;

public class User {
    private String Adsoyad;
    private String Sifre;

    public User(String Adsoyad,String Sifre){
        this.Adsoyad=Adsoyad;
        this.Sifre=Sifre;
    }
    public String getAdsoyad(){return Adsoyad;}
    public String getSifre(){return Sifre;}

}
