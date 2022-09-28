package hello.hyeoni.springproject.travel;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TravelRepositoryExtension {

    List<Travel> findByKeyword(String keyword);
}
