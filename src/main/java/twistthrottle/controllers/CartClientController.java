package twistthrottle.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import twistthrottle.dtos.CartItem;
import twistthrottle.dtos.ProductDTO;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/cart")
public class CartClientController {

    private final RestTemplate restTemplate;
    @Value("${cart.service.url}")
    private String cartServiceUrl;

    @Autowired
    public CartClientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam int quantity,
                            HttpServletRequest request) {

        ProductDTO product = new ProductDTO(productId, name, price, quantity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }

        HttpEntity<ProductDTO> requestEntity = new HttpEntity<>(product, headers);

        String url = cartServiceUrl + "/add?quantity=" + quantity;

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );


        } catch (Exception e) {
            System.err.println("Error calling cart service /add endpoint: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<CartItem[]> response = restTemplate.exchange(
                    cartServiceUrl,
                    HttpMethod.GET,
                    requestEntity,
                    CartItem[].class);

            CartItem[] cartItemsArray = response.getBody();
            List<CartItem> cart = (cartItemsArray != null) ? Arrays.asList(cartItemsArray) : List.of();
            model.addAttribute("cart", cart);
        } catch (Exception e) {
            System.err.println("Error fetching cart from cart service: " + e.getMessage());
            model.addAttribute("errorMessage", "Could not load cart contents.");
            model.addAttribute("cart", List.of());
        }
        return "cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long productId,
                                 @RequestParam int quantity,
                                 HttpServletRequest request) {

        if (quantity <= 0) {
            return "redirect:/cart?error=Quantity must be positive";
        }

        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }
        HttpEntity<?> updateRequestEntity = new HttpEntity<>(headers);

        String updateUrl = cartServiceUrl + "/update?productId=" + productId + "&quantity=" + quantity;

        try {
            restTemplate.exchange(
                    updateUrl,
                    HttpMethod.PUT,
                    updateRequestEntity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            System.err.println("Error updating cart item in cart service: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return "redirect:/cart?error=Update failed";
        } catch (Exception e) {
            System.err.println("Unexpected error during cart update: " + e.getMessage());
            return "redirect:/cart?error=Unexpected error";
        }
        return "redirect:/cart";
    }


    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }
        HttpEntity<?> deleteRequestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    cartServiceUrl + "/remove/" + productId,
                    HttpMethod.DELETE,
                    deleteRequestEntity,
                    String.class);
        } catch (Exception e) {
            System.err.println("Error calling cart service /remove endpoint: " + e.getMessage());
            return "redirect:/cart?error=Remove failed";
        }
        return "redirect:/cart";
    }

}