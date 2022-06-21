package hello.hyeoni.springproject.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

//@SequenceGenerator(name = "ACCOUNT_SEQ_GENERATOR", sequenceName = "ACCOUNT_SEQ", initialValue = 1, allocationSize = 1)
@Getter @Setter @Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue //(strategy= GenerationType.SEQUENCE, generator="ACCOUNT_SEQ_GENERATOR")
    private Integer id;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String password;

    private boolean emailVerified; //이메일 인증 여부
    
    private String emailCheckToken; //메일 검증시 사용할 토큰 값
    
    private LocalDateTime joinedAt; //가입 완료 날짜

    //기본 정보
    private String bio;
    private String url;
    private String occupation;
    private String location;
//    @Lob //String은 varchar로 매핑, 보다 긴 경우 설정해주면 text로 매핑된다.
    @Basic(fetch = FetchType.EAGER) //기본은 LAZY, 프로필 사진은 바로 가져오는게 좋아 EAGER로 설정.
    private String profileImage;

    private LocalDateTime emailCheckTokenGeneratedAt; //이메일 토큰 생성 시간

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
