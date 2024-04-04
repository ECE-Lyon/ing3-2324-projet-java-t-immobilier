package Class;

import java.util.Date;
import java.time.LocalTime;

public class Visit {
    private int id_visit;
    private Date date_visit;
    private LocalTime time_visit;
    private String feedback;

    public Visit(int ID_visit, Date visite_date, LocalTime heure_visite, String retour){
        this.id_visit=ID_visit;
        this.date_visit=visite_date;
        this.time_visit=heure_visite;
        this.feedback=retour;
    }
}
