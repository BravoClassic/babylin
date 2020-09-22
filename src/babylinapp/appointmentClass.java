package babylinapp;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

public class appointmentClass {
    protected int appointmentId;
    protected int userId;
    protected Date date;
    protected LocalTime t;

    public appointmentClass(){
        this.appointmentId = 0;
        this.userId=0;
        this.date=Date.from(Instant.now());
        this.t= LocalTime.now();
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public appointmentClass(int appointmentId, int userId, Date date, LocalTime t) {
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

    public LocalTime getT() {
        return t;
    }

    public void setT(LocalTime t) {
        this.t = t;
    }
}
