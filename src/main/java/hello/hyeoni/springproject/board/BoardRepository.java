package hello.hyeoni.springproject.board;

import hello.hyeoni.springproject.travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTravelOrderByCreatedDateTimeDesc(Travel travel);
}
