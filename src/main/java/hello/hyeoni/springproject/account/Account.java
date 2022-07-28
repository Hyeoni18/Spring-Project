package hello.hyeoni.springproject.account;

import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter @Setter @Builder
@Entity @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean emailVerified;
    private String emailCheckToken;
    private LocalDateTime joinedAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;
    private LocalDateTime emailCheckTokenGeneratedAt;

    private boolean studyCreatedByEmail;
    private boolean studyCreatedByWeb = true;
    private boolean studyEnrollmentResultByEmail;
    private boolean studyEnrollmentResultByWeb = true;
    private boolean studyUpdatedByEmail;
    private boolean studyUpdatedByWeb = true;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

//    @ElementCollection(fetch = FetchType.EAGER) //여러 개의 enum을 가질 수 있으니까. 그리고 기본 role은 LAZY인데 가져올 롤이 적고 매번 가져와야 하니까 EAGER로 변경.
//    @Enumerated(EnumType.STRING)
//    private Set<AccountRole> roles;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }

}
