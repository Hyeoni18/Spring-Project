package hello.hyeoni.springproject.board;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "lecture")
public class Lecture {

    @Id
    private Integer id;

    private String subject;

    private Integer price;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime opening;

    private String instructor;
}