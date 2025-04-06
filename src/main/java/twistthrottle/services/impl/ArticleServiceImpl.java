package twistthrottle.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Articles;
import twistthrottle.repositories.ArticleRepository;
import twistthrottle.services.ArticleService;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Articles> findAllArticles() {
        return articleRepository.findAll();
    }
    @Override
    public Optional<Articles> findBySlug(String slug) {
        return articleRepository.findBySlug(slug);
    }
}

