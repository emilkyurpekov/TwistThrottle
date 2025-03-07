package twistthrottle.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import twistthrottle.models.entities.enums.orderStatus;
import twistthrottle.models.entities.enums.paymentMethod;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Temporal(TemporalType.DATE)
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private orderStatus orderStatus;
    @Column(nullable = false)
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private paymentMethod paymentMethod;
    @Column(nullable = false)
    private String shippingAddress;
    private String billingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
