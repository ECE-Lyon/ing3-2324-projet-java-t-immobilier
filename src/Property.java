import java.util.List;

public class Property {
    private int id_property;
    private float size;
    private String description;
    private Double price;
    private boolean statut_sell;
    private boolean is_sell;
    private boolean has_pool;
    private boolean has_garden;
    private String property_type;
    private int nb_room;
    private  boolean program_visit;
    private Client id_client;
    private Visit id_visit;
    private Employee id_employee;
    private List<Client> buyer_list;
}
