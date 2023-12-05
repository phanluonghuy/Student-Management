package Model;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class History {
    private String history_id;
    private String account_id;
    private Date date_perform;

}
