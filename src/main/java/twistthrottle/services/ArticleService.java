package twistthrottle.services;

import twistthrottle.models.entities.Articles;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Articles> findAllArticles();
    Optional<Articles> findBySlug(String slug);
}
