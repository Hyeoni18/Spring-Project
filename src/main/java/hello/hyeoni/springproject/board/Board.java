package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.UserAccount;
import hello.hyeoni.springproject.travel.Travel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @EqualsAndHashCode(of="id")
public class Board {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Travel travel;

    @ManyToOne
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String fullDescription;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    public boolean isEditable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.createdBy.equals(account);
    }

    public String getNickname() {
        return this.createdBy.getNickname();
    }

    public boolean isManager() {
        return this.travel.getManagers().contains(this.createdBy);
    }
}
