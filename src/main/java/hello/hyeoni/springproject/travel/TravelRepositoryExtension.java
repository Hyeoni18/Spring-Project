package hello.hyeoni.springproject.travel;

import hello.hyeoni.springproject.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface TravelRepositoryExtension {

    Page<Travel> findByKeyword(String keyword, Pageable pageable);

    Page<Travel> findByPublished(Pageable pageable);

    List<Travel> findByAccount(Account account);
}
