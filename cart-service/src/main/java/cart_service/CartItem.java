package cart_service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItem {
    private ProductDTO product;
    private double price;
    private int quantity;
}
