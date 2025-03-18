package twistthrottle.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String name;
    private double price;
    private int quantity;

    public ProductDTO() {
    }
}
