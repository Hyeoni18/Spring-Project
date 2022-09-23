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
    private boolean emailVerified; // 이메일 인증 여부
    private String emailCheckToken; // 이메일 검증에 사용될 토큰
    private LocalDateTime joinedAt; // 이메일 인증 완료 시간을 가입 날짜로 저장

    // 기본 정보
    private String bio;
    private String url;
    private String occupation;
    private String location;
    @Lob // String은 varchar(255)로 저장, Lob을 통해 text로 매핑되도록 함
    @Basic(fetch = FetchType.EAGER) // 기본 fetch는 LAZE, 사진을 바로 가져 오려고 EAGER 설정
    private String profileImage;
    private LocalDateTime emailCheckTokenGeneratedAt; // 이메일 토큰 생성 시간

    private boolean studyCreatedByEmail; // 스터디 생성 알림 메일 수신 여부
    private boolean studyCreatedByWeb = true; // 스터디 생성 알림 웹 수신 여부
    private boolean studyEnrollmentResultByEmail; // 스터디 가입 결과 메일 수신 여부
    private boolean studyEnrollmentResultByWeb = true; // 스터디 가입 결과 웹 수신 여부
    private boolean studyUpdatedByEmail; // 스터디 변경 사항 메일 수신 여부
    private boolean studyUpdatedByWeb = true; // 스터디 변경 사항 웹 수신 여부

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
