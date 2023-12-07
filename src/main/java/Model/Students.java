package Model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Students {
    private String student_id;
    private String student_list_id;
    private String full_name;
    private Date birthday;
    private String gender;
    private String home_address;
    private String phone;
    private float GPA;
}