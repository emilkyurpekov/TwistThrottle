package cart_service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRequest {
    private String shippingAddress;

    public OrderRequest() {}

    public OrderRequest(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}
