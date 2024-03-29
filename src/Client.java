import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class Client extends Utilisateur {

    private int id_client;
    private boolean statut_fidelity;
    private List<visits_history> visits_history;
    private List<favorite_property> favorite_property;
    private List<action_history> action_history;
    private List<own_property> own_property;
    private boolean buyer;
    private boolean seller;

    public Client(int Identifiant_user, String nom, String mail, String mot_de_passe, Date date_inscription, int ID_client,boolean statut_utilisateur,boolean acheteur, boolean vendeur,boolean statut_fidelite){
        super(Identifiant_user,nom,mail,mot_de_passe,date_inscription,statut_utilisateur);
        this.id_client = ID_client;
        this.statut_fidelity = statut_fidelite;
        this.buyer =acheteur;
        this.seller=vendeur;
        this.visits_history = new ArrayList<>();
        this.favorite_property = new ArrayList<>();
        this.action_history= new ArrayList<>();
        this.own_property =new ArrayList<>();
    }
}
