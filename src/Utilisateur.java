import java.util.*;

abstract class Utilisateur {
   private int id_user;
   private String name;
   private String email;
   private String password;
   private Date inscription_date;

   private Boolean statut_user;

    public Utilisateur(int Identifiant_user, String nom, String mail, String mot_de_passe, Date date_inscription, boolean statut_Utilisateur){
       this.id_user = Identifiant_user;
       this.name = nom;
       this.email = mail;
       this.password = mot_de_passe;
       this.inscription_date = date_inscription;
       this.statut_user =statut_Utilisateur;
    }

}
