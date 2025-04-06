package twistthrottle.services;

import twistthrottle.models.entities.Articles;

import java.util.List;

public interface ArticleService {
    List<Articles> findAllArticles();
}
