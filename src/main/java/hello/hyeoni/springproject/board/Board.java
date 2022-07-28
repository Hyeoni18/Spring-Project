package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@SequenceGenerator(name = "BOARD_SEQ_GENERATOR", sequenceName = "BOARD_SEQ", initialValue = 1, allocationSize = 1)
@Getter@Setter
@Entity
public class Board {

    @Id @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="BOARD_SEQ_GENERATOR")
    private Integer id;

    private String title;

    private String content;

    @ManyToOne
    private Account author;

}
