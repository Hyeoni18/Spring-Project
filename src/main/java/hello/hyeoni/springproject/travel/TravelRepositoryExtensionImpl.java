package hello.hyeoni.springproject.travel;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import hello.hyeoni.springproject.account.QAccount;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TravelRepositoryExtensionImpl extends QuerydslRepositorySupport implements TravelRepositoryExtension{ // QuerydslRepositorySupport은 QueryDsl 작성에 도움을 줌

    public TravelRepositoryExtensionImpl() {
        super(Travel.class);
    }

    @Override
    public List<Travel> findByKeyword(String keyword) {
        QTravel travel = QTravel.travel;
        JPQLQuery<Travel> query = from(travel).where(travel.published.isTrue()
                .and(travel.title.containsIgnoreCase(keyword)));
        return query.fetch();
    }

}
