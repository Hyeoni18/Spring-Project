package hello.hyeoni.springproject.recommendation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.AccountSerializer;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity @Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Location {

    @Id @GeneratedValue
    private Long  id;

    private String city;
    private String province;
    private String country;
    private String description;

    private LocalDateTime createdDateTime;

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account writer;

}
