package twistthrottle.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private ProductDTO product;
    private int quantity;

    public CartItem() {}

    public CartItem(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

}
