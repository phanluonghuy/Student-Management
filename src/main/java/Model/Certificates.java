package Model;

import java.util.Date;

public class Certificates {
    private String certificate_id;
    private String classification_id;
    private String student_id;
    private String account_id;
    private Date date_created;
    private String certificate_name;
    private String classification_name;

    public Certificates(String certificate_id, String classification_id, String student_id, String account_id, Date date_created, String certificate_name, String classification_name) {
        this.certificate_id = certificate_id;
        this.classification_id = classification_id;
        this.student_id = student_id;
        this.account_id = account_id;
        this.date_created = date_created;
        this.certificate_name = certificate_name;
        this.classification_name = classification_name;
    }

    public String getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(String certificate_id) {
        this.certificate_id = certificate_id;
    }

    public String getClassification_id() {
        return classification_id;
    }

    public void setClassification_id(String classification_id) {
        this.classification_id = classification_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }

    public String getClassification_name() {
        return classification_name;
    }

    public void setClassification_name(String classification_name) {
        this.classification_name = classification_name;
    }

    @Override
    public String toString() {
        return "Certificates{" +
                "certificate_id='" + certificate_id + '\'' +
                ", classification_id='" + classification_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", account_id='" + account_id + '\'' +
                ", date_created=" + date_created +
                ", certificate_name='" + certificate_name + '\'' +
                ", classification_name='" + classification_name + '\'' +
                '}';
    }
}
