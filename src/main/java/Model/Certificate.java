package Model;

import java.util.Date;

public class Certificate {
    private String certificate_id;
    private String student_id;
    private Date date_created;
    private String certificate_name;
    private String certificate_level;
    private Date expired_date;

    public Certificate(String certificate_id, String student_id, Date date_created, String certificate_name, String certificate_level, Date expired_date) {
        this.certificate_id = certificate_id;
        this.student_id = student_id;
        this.date_created = date_created;
        this.certificate_name = certificate_name;
        this.certificate_level = certificate_level;
        this.expired_date = expired_date;
    }

    public Certificate(String student_id, String certificate_name, String certificate_level, Date expired_date) {
        this.student_id = student_id;
        this.certificate_name = certificate_name;
        this.certificate_level = certificate_level;
        this.expired_date = expired_date;
    }

    public Certificate(String certificate_id, String student_id, String certificate_name, String certificate_level, Date expired_date) {
        this.certificate_id = certificate_id;
        this.student_id = student_id;
        this.certificate_name = certificate_name;
        this.certificate_level = certificate_level;
        this.expired_date = expired_date;
    }

    public String getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(String certificate_id) {
        this.certificate_id = certificate_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
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

    public String getCertificate_level() {
        return certificate_level;
    }

    public void setCertificate_level(String certificate_level) {
        this.certificate_level = certificate_level;
    }

    public Date getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(Date expired_date) {
        this.expired_date = expired_date;
    }

    @Override
    public String toString() {
        return "Certificates{" +
                "certificate_id='" + certificate_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", date_created=" + date_created +
                ", certificate_name='" + certificate_name + '\'' +
                ", certificate_level='" + certificate_level + '\'' +
                ", expired_date=" + expired_date +
                '}';
    }
}
