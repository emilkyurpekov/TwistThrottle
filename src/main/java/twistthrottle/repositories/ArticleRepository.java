package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Articles;

@Repository
public interface ArticleRepository extends JpaRepository<Articles,Long> {
}
