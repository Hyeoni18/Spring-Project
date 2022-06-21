package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.domain.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@SequenceGenerator(name = "BOARD_SEQ_GENERATOR", sequenceName = "BOARD_SEQ", initialValue = 1, allocationSize = 1)
@Getter@Setter
@Entity
public class Board {

    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="BOARD_SEQ_GENERATOR")
    private Integer id;

    private String content;

    private String title;

    @ManyToOne
    private Account author;

}
