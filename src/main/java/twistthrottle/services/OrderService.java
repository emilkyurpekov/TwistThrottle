package twistthrottle.services;

import twistthrottle.dtos.CartItem;
import twistthrottle.models.entities.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(List<CartItem> cartItems, String shippingAddress, String billingAddress, String userEmail); // Added userEmail
    List<Order> findOrdersByUserEmail(String email);
    List<Order> findOrdersByUserId(Long userId);
    List<Order> findOrdersByDateRange(java.util.Date startDate, java.util.Date endDate);
    List<Order> findOrdersByBillingAddress(String billingAddress);
    Order findOrderById(Long orderId);
}
