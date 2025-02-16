package twistthrottle.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private String shippingAddress;

    public OrderRequest() {}

    public OrderRequest(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}
