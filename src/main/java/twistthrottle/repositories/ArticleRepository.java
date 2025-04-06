package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Articles;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Articles,Long> {
    Optional<Articles> findBySlug(String slug);
}
