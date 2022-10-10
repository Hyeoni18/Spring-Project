package hello.hyeoni.springproject.notification;

import hello.hyeoni.springproject.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String link;

    private String shortDescription;

    private boolean checked;

    @ManyToOne
    private Account account;

    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
