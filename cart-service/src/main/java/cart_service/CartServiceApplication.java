package cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cart_service")

public class CartServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(CartServiceApplication.class, args);
    }
}