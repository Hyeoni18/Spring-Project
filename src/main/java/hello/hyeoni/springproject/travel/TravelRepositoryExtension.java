package hello.hyeoni.springproject.travel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TravelRepositoryExtension {

    Page<Travel> findByKeyword(String keyword, Pageable pageable);

    List<Travel> findByPublished();
}
