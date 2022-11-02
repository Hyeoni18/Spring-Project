package hello.hyeoni.springproject.recommendation;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity @Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Location {

    @Id @GeneratedValue
    private Integer  id;

    private String city;
    private String province;
    private String country;
    private String description;

    private LocalDateTime createdDateTime;
}
