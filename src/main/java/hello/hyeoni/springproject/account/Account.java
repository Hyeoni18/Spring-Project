package hello.hyeoni.springproject.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@SequenceGenerator(name = "ACCOUNT_SEQ_GENERATOR", sequenceName = "ACCOUNT_SEQ", initialValue = 1, allocationSize = 1)
@Getter @Setter
@Entity
public class Account {

    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ACCOUNT_SEQ_GENERATOR")
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) //여러 개의 enum을 가질 수 있으니까. 그리고 기본 role은 LAZY인데 가져올 롤이 적고 매번 가져와야 하니까 EAGER로 변경.
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
