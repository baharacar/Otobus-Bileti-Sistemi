package Service;
import Model.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static List<User> users=new ArrayList<>();

    //üye ekleme
    public static void register(String Adsoyad,String Sifre){
        users.add(new User(Adsoyad,Sifre));
    }

    public static boolean login(String Adsoyad,String Sifre){
       for(User user: users){
           if(user.getAdsoyad().equals(Adsoyad)&& user.getSifre().equals(Sifre)){return true;}
       }
        return false;
    }
    public static void register(User user){
        // sql bağlandığında burası dolacak
        System.out.println("Kayıt alındı: "+user.getAdsoyad());
    }
}
