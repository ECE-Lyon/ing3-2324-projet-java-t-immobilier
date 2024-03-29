import java.util.*;

abstract class User {
   private int id;
   private String name;
   private String email;
   private String password;
   private Date inscription_date;

    public User(int Identifiant, String nom, String mail,String mot_de_passe, Date date_inscription){
       this.id = Identifiant;
       this.name = nom;
       this.email = mail;
       this.password = mot_de_passe;
       this.inscription_date = date_inscription;
    }

}
