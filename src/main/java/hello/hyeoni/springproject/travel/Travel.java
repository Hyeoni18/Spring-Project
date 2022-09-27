package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter @Getter @EqualsAndHashCode
@Builder @NoArgsConstructor @AllArgsConstructor
public class Travel {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany // 관리자는 여러 명일 수 있음
    private Set<Account> managers = new HashSet<>(); // Travel 관리자

    @ManyToMany
    private Set<Account> members = new HashSet<>(); // Travel 회원

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    private String fullDescription;

    @Lob
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime; // Travel 공개한 시간
    private LocalDateTime closedDateTime; // Travel 종료한 시간
    private LocalDateTime recruitingUpdateDateTime; // Travel 인원 모집 가능 시간

    private boolean recruiting; // Travel 회원 모집 여부
    private boolean published; // Travel 공개 여부
    private boolean closed; // Travel 종료 여부
    private boolean useBanner; // 배너 사용 여부

}
