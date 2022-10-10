package hello.hyeoni.springproject.travel;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TravelRepository extends JpaRepository<Travel, Long>, TravelRepositoryExtension {

    boolean existsByPath(String path);

    @EntityGraph(attributePaths = {"tags", "zones", "managers", "members"}, type = EntityGraph.EntityGraphType.LOAD) // LOAD는 EntityGraph에 명시한 데이터는 EAGER, 나머지는 기본 전략을 따름
    Travel findByPath(String path);

    @EntityGraph(attributePaths = {"tags", "managers"})
    Travel findTravelWithTagsByPath(String path);

    @EntityGraph(attributePaths = {"zones", "managers"})
    Travel findTravelWithZonesByPath(String path);

    @EntityGraph(attributePaths = "managers")
    Travel findTravelWithManagersByPath(String path);

    @EntityGraph(attributePaths = {"members","managers"})
    Travel findTravelWithMembersByPath(String path);

    @EntityGraph(attributePaths = {"tags", "zones"})
    Travel findTravelWithTagsAndZonesById(Long id);
}
