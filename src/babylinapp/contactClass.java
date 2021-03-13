package babylinapp;

import java.util.Date;

public class contactClass {
    public contactClass(Integer contactId, String contactName, Date dateBirth, String contactEmail, String contactAddress, String contactPhone) {
        super();
        this.contactId = contactId;
        this.contactName = contactName;
        this.dateBirth = dateBirth;
        this.contactEmail = contactEmail;
        this.contactAddress = contactAddress;
        this.contactPhone = contactPhone;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public contactClass(){
        this.contactId=0;
        this.contactName="";
        this.dateBirth=null;
        this.contactEmail="";
        this.contactAddress="";
        this.contactPhone="";
    }

    protected Integer contactId;
    protected String contactName;
    protected Date dateBirth;
    protected String contactEmail;
    protected String contactAddress;
    protected String contactPhone;

    public Integer getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }
}
