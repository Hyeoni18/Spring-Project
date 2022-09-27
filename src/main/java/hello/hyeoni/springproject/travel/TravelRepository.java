package hello.hyeoni.springproject.travel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TravelRepository extends JpaRepository<Travel, Long> {

    boolean existsByPath(String path);
}
