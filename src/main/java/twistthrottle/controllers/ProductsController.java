package twistthrottle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.repositories.CategoryRepository;
import twistthrottle.repositories.ProductRepository;

import java.util.List;

@Controller
public class ProductsController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductsController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/products")
    public String getProducts(@RequestParam(required = false) Long categoryId, Model model) {
        List<Product> products;
        List<Category> categories = categoryRepository.findAll();

        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
            Category selectedCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            model.addAttribute("selectedCategory", selectedCategory);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "products"; // products.html
    }
}