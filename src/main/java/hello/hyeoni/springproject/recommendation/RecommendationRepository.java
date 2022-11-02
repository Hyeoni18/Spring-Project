package hello.hyeoni.springproject.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Location, Integer> {
}
