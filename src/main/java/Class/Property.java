package Class;

import java.util.ArrayList;
import java.util.List;

public class Property {
    private int id_property;
    private float size;
    private String description;
    private Double price;
    private boolean statut_sold;
    private boolean has_pool;
    private boolean has_garden;
    private String property_type;
    private int nb_room;
    private  boolean program_visit;
    private Client id_client;
    private Visit id_visit;
    private Employee id_employee;
    private List<Client> buyer_list;

    public Property(int ID_Propriete, float taille, String descrip, Double prix, boolean statut_vente, boolean piscine, boolean jardin, String propriete_type, int nb_chambre, boolean visite_programme, Client ID_client, Visit ID_visit, Employee ID_employee){
        this.id_property=ID_Propriete;
        this.size = taille;
        this.description = descrip;
        this.price=prix;
        this.statut_sold=statut_vente;
        this.has_pool =piscine;
        this.has_garden =jardin;
        this.property_type=propriete_type;
        this.nb_room=nb_chambre;
        this.program_visit = visite_programme;
        this.id_client =ID_client;
        this.id_visit=ID_visit;
        this.id_employee=ID_employee;
        this.buyer_list=new ArrayList<>();
    }
}
