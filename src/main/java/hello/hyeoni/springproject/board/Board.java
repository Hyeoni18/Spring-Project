package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter @Setter
@Entity
public class Board {

    @Id @GeneratedValue
    private Integer id;

    private String title;

    private String content;

    @ManyToOne
    private Account author;

}
