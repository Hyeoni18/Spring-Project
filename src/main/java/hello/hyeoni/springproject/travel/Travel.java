package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.UserAccount;
import hello.hyeoni.springproject.board.Board;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "travel")
    private List<Board> boards;

    private LocalDateTime publishedDateTime; // Travel 공개한 시간
    private LocalDateTime closedDateTime; // Travel 종료한 시간
    private LocalDateTime recruitingUpdateDateTime; // Travel 인원 모집 가능 시간

    private boolean recruiting; // Travel 회원 모집 여부
    private boolean published; // Travel 공개 여부
    private boolean closed; // Travel 종료 여부

    public void addManager(Account account) {
        this.managers.add(account);
    }

    // travel/view.html 에서 사용, 가입 가능 여부 조회
    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount(); // 현재 principal 정보
        return this.isPublished() && this.isRecruiting() // 공개된 동행자 모집, 동행자 모집중
                && !this.members.contains(account) && !this.managers.contains(account); // 동행자 아님, 매니저 아님
    }
    // 동행자 여부 조회
    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }
    // 매니저 여부 조회
    public boolean isManager(UserAccount userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }

    public boolean isManagedBy(Account account) {
        return this.getManagers().contains(account);
    }

    public void publish() {
        if(!this.closed && !this.published) {
            this.published = true;
            this.publishedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("동행자 모집을 공개할 수 없습니다. 이미 공개했거나 종료된 모집입니다.");
        }
    }

    public void close() {
        if(!this.closed && this.published) {
            this.closed = true;
            this.closedDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("동행자 모집을 종료할 수 없습니다. 공개되지 않았거나 이미 종료된 모집입니다.");
        }
    }

    public boolean isRemovable() {
        return !this.published;
    }

    public String getEncodedPath() {
        return URLEncoder.encode(this.path, StandardCharsets.UTF_8);
    }

    public boolean canUpdateRecruiting() {
        return this.published && this.recruitingUpdateDateTime == null || this.recruitingUpdateDateTime.isBefore(LocalDateTime.now().minusHours(1));
    }

    public void startRecruit() {
        if(canUpdateRecruiting()) {
            this.recruiting = true;
            this.recruitingUpdateDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 할 수 없습니다. 동행자 모집을 공개하거나 1시간 뒤 다시 시도해 주세요.");
        }
    }

    public void stopRecruit() {
        if(canUpdateRecruiting()) {
            this.recruiting = false;
            this.recruitingUpdateDateTime = LocalDateTime.now();
        } else {
            throw new RuntimeException("인원 모집을 중단할 수 없습니다. 1시간 뒤 다시 시도해 주세요.");
        }
    }

    public void addMember(Account account) {
        this.members.add(account);
    }

    public void removeMember(Account account) {
        this.members.remove(account);
    }
}
