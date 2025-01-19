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
            @RequestParam(required = false) Long categoryId, // Optional parameter for filtering
            Model model,
            HttpServletRequest request) {

        List<Product> products;

        if (categoryId != null) {
            // If categoryId is provided, fetch products for that category
            products = productService.findByCategoryId(categoryId);
        } else {
            // Otherwise, fetch all products
            products = productService.findAll();
        }

        // Add products to the model
        model.addAttribute("products", products);

        // Add categories to the model (to display category filters on the page)
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Check if the user is logged in
        Boolean isLoggedIn = (request.getSession().getAttribute("loggedInUser") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "products";
    }

}
