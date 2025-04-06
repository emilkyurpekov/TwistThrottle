package twistthrottle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Import PathVariable
import org.springframework.web.server.ResponseStatusException; // For handling not found
import twistthrottle.models.entities.Articles;
import twistthrottle.services.ArticleService;

import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String showArticlesListPage(Model model) {
        List<Articles> articles = articleService.findAllArticles();
        model.addAttribute("articles", articles);
        return "articles/articles-list";
    }

    @GetMapping("/articles/{slug}")
    public String showArticleDetailPage(@PathVariable String slug, Model model) {
        Articles article = articleService.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found")); // Handle not found case

        model.addAttribute("article", article);
        return "articles/article-detail";
    }
}