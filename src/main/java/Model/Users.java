package Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Users {
    private String user_id;
    private String fullname;
    private int age;
    private String phone;
    private String img_profile;
    private String isActive;
}
