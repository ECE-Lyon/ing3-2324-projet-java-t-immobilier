import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class Client extends Utilisateur {

    private int id_client;
    private boolean statut_fidelity;
    private int[] visits_history;
    private int[] favorite_property;
    private int[] action_history;
    private List<Property> own_property;
    private boolean buyer;
    private boolean seller;
    private Utilisateur id_user;

    public Client(int Identifiant_user, String nom, String mail, String mot_de_passe, Date date_inscription, int ID_client,boolean statut_utilisateur,boolean acheteur, boolean vendeur,boolean statut_fidelite, int[] historique_visite,int[] propriete_favorite,int[] historique_action, Utilisateur ID_user){
        super(Identifiant_user,nom,mail,mot_de_passe,date_inscription,statut_utilisateur);
        this.id_client = ID_client;
        this.statut_fidelity = statut_fidelite;
        this.buyer =acheteur;
        this.seller=vendeur;
        this.visits_history = historique_visite;
        this.favorite_property = propriete_favorite;
        this.action_history= historique_action;
        this.own_property =new ArrayList<>();
        this.id_user =ID_user;
    }
}
