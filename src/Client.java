import java.awt.*;
import java.util.Date;

public class Client extends User{

    private boolean fidelity_statut;
    private List visits_history;
    private List favorite_property;

    public Client(int Identifiant, String nom, String mail,String mot_de_passe, Date date_inscription, boolean statut_fidelite,List historique_visite, List propriete_fav){
        super(Identifiant,nom,mail,mot_de_passe,date_inscription);
        this.fidelity_statut = statut_fidelite;
        this.visits_history = historique_visite;
        this.favorite_property = propriete_fav;
    }
}
