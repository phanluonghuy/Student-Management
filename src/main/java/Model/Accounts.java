package Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Accounts {
    private String account_id;
    private String user_id;
    private String student_list_id;
    private String user_name;
    private String password;
    private String role_id;
}
