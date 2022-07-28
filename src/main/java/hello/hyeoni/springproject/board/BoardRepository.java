package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Integer> {

   // @Query("SELECT board FROM Board board WHERE board.title LIKE %:title% ")
//    Page<Board> findByTitle(String title, Pageable pageable);
    Board findByTitle(String title);

    Board findById(int boardId);

}
