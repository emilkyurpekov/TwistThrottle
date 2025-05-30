package twistthrottle.services;

import twistthrottle.dtos.CartItem;
import twistthrottle.models.entities.Order;

import java.util.List;

public interface OrderService {
    List<Order> findOrdersByUserEmail(String email);
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findOrdersByDateRange(java.util.Date startDate, java.util.Date endDate);
    List<Order> findOrdersByBillingAddress(String billingAddress);
    Order findOrderById(Long orderId);
    public List<Order> getOrdersByUser(Long userId);
    Order createOrder(List<CartItem> cartItems, String shippingAddress, String cardNumber, String userEmail, String cookieHeader);

}
