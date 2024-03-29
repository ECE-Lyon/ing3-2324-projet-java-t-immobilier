public class Address {
   private int number;
   private String name;
   private String city;
   private int id_address;
   private int postal_code;
   private Property id_property;

    public Address(int nombre, String nom, String ville, int ID_adresse, int code_postale, Property ID_property){
        this.number = nombre;
        this.name = nom;
        this.city = ville;
        this.id_address = ID_adresse;
        this.postal_code = code_postale;
        this.id_property = ID_property;
    }

}
