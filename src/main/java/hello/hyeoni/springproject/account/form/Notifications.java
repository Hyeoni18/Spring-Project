package hello.hyeoni.springproject.account.form;

import lombok.Data;

@Data
public class Notifications {
    private boolean travelCreatedByEmail;
    private boolean travelCreatedByWeb;
    private boolean travelUpdatedByEmail;
    private boolean travelUpdatedByWeb;
}
