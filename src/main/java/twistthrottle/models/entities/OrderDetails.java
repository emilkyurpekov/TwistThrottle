package twistthrottle.models.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderDetails extends BaseEntity {



    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private BigDecimal unitPrice;

    public OrderDetails() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
