package Model;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Certificates {
    private String certificate_id;
    private String classification_id;
    private String student_id;
    private String account_id;
    private Date date_created;
    private String certificate_name;
}
