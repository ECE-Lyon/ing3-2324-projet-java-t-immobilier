package models;

import java.sql.Date;
import java.sql.Time;

public class Visit {
    private int idVisit;
    private Date dateVisit;
    private Time timeVisit;
    private String feedback;
    private int idProperty;
    private int idUser;

    public Visit(int idVisit, Date dateVisit, Time timeVisit, String feedback, int idProperty, int idUser) {
        this.idVisit = idVisit;
        this.dateVisit = dateVisit;
        this.timeVisit = timeVisit;
        this.feedback = feedback;
        this.idProperty = idProperty;
        this.idUser = idUser;
    }

    public int getIdVisit() {
        return idVisit;
    }

    public Date getDateVisit() {
        return dateVisit;
    }

    public Time getTimeVisit() {
        return timeVisit;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getIdProperty() {
        return idProperty;
    }

    public int getIdUser() {
        return idUser;
    }
}
