package twistthrottle.models.entities;

import jakarta.persistence.*;
import twistthrottle.models.entities.enums.orderStatus;
import twistthrottle.models.entities.enums.paymentMethod;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    //fields:
    @Temporal(TemporalType.DATE)
    @Column(name = "order_date", nullable = false)
    private Date date;
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


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public orderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(orderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public paymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(paymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order() {
    }

    public Order(Date date, orderStatus orderStatus, double totalPrice, paymentMethod paymentMethod, String shippingAddress, String billingAddress, List<OrderDetails> orderDetails, User user) {
        this.date = date;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderDetails = orderDetails;
        this.user = user;
    }
}
