package twistthrottle.models.entities;

import jakarta.persistence.*;
import twistthrottle.models.entities.enums.productType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity{
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private productType productType;
    private BigDecimal price;
    @Column(nullable = false)
    private int stock;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    public Product() {
    }

    public Product(String name, twistthrottle.models.entities.enums.productType productType, BigDecimal price, List<OrderDetails> orderDetails) {
        this.name = name;
        this.productType = productType;
        this.price = price;
        this.orderDetails = orderDetails;
    }
}
