package babylinapp;

import java.time.Instant;
import java.util.Date;

public class appointmentClass {
    protected int appointmentId;
    protected int userId;
    protected Date date;
    protected String t;

    public appointmentClass(){
        this.appointmentId = 0;
        this.userId=0;
        this.date=Date.from(Instant.now());
        this.t= "";
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public appointmentClass(int appointmentId, int userId, Date date, String  t) {
        this.appointmentId=appointmentId;
        this.userId = userId;
        this.date = date;
        this.t = t;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
