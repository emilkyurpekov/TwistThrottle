package twistthrottle.controllers;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import twistthrottle.models.entities.Category;
import twistthrottle.models.entities.Product;
import twistthrottle.services.impl.CategoryServiceImpl;
import twistthrottle.services.impl.ProductServiceImpl;

import java.util.List;

@Controller
public class ProductPageController {
    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;
    public ProductPageController(ProductServiceImpl productService, CategoryServiceImpl categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String getProducts(
            @RequestParam(required = false) Long categoryId,
            Model model,
            HttpServletRequest request) {

        List<Product> products;

        if (categoryId != null) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.findAll();
        }

        model.addAttribute("products", products);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Boolean isLoggedIn = (request.getSession().getAttribute("loggedInUser") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "products";
    }

}
