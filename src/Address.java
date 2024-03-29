public class Address {
   private int number_street;
   private String name_street;
   private String city;
   private int id_address;
   private int postal_code;
   private Property id_property;

    public Address(int nombre, String nom, String ville, int ID_adresse, int code_postale, Property ID_property){
        this.number_street = nombre;
        this.name_street = nom;
        this.city = ville;
        this.id_address = ID_adresse;
        this.postal_code = code_postale;
        this.id_property = ID_property;
    }

}
