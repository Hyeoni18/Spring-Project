package hello.hyeoni.springproject.travel;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.QAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TravelRepositoryExtensionImpl extends QuerydslRepositorySupport implements TravelRepositoryExtension{ // QuerydslRepositorySupport은 QueryDsl 작성에 도움을 줌

    public TravelRepositoryExtensionImpl() {
        super(Travel.class);
    }

    @Override
    public Page<Travel> findByKeyword(String keyword, Pageable pageable) {
        QTravel travel = QTravel.travel;
        JPQLQuery<Travel> query = from(travel).where(travel.published.isTrue()
                .and(travel.title.containsIgnoreCase(keyword)))
                .leftJoin(travel.managers, QAccount.account).fetchJoin()
                .leftJoin(travel.members, QAccount.account).fetchJoin();
        JPQLQuery<Travel> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Travel> travelQueryResults = pageableQuery.fetchResults();
        return new PageImpl<>(travelQueryResults.getResults(), pageable, travelQueryResults.getTotal());
    }

    @Override
    public Page<Travel> findByPublished(Pageable pageable) {
        QTravel travel = QTravel.travel;
        JPQLQuery<Travel> query = from(travel).where(travel.published.isTrue())
                .leftJoin(travel.members, QAccount.account).fetchJoin()
                .leftJoin(travel.managers, QAccount.account).fetchJoin();
        JPQLQuery<Travel> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Travel> travelQueryResults = pageableQuery.fetchResults();
        return new PageImpl<>(travelQueryResults.getResults(), pageable, travelQueryResults.getTotal());
    }

    @Override
    public List<Travel> findByAccount(Account account) {
        QTravel travel = QTravel.travel;
        JPQLQuery<Travel> query = from(travel).where(travel.managers.contains(account).or(travel.members.contains(account)));
        return query.fetch();
    }

}
