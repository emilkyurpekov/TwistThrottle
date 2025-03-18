package twistthrottle.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import twistthrottle.dtos.CartItem;
import twistthrottle.models.entities.Order;
import twistthrottle.models.entities.OrderDetails;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.orderStatus;
import twistthrottle.models.entities.enums.paymentMethod;
import twistthrottle.repositories.OrderDetailsRepository;
import twistthrottle.repositories.OrderRepository;
import twistthrottle.repositories.UserRepository;
import twistthrottle.services.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String CART_SERVICE_URL = "http://localhost:8081/api/cart";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Order createOrder(List<CartItem> cartItems, String shippingAddress, String userEmail) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart items cannot be null or empty.");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be null or empty.");
        }

        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User with email " + userEmail + " not found."));

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus(orderStatus.PENDING);
        order.setTotalPrice(calculateTotalPrice(cartItems));
        order.setPaymentMethod(paymentMethod.CREDIT_CARD); // You should get this from the request
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(shippingAddress); // Set billing address
        order.setUser(user);
        order = orderRepository.save(order);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            //Fetch product from db.
            Product product = new Product();
            product.setName(cartItem.getProduct().getName());
            product.setId(cartItem.getProduct().getProductId());
            product.setPrice(BigDecimal.valueOf(cartItem.getPrice()));
            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartItem.getQuantity());
            orderDetails.setUnitPrice(BigDecimal.valueOf(cartItem.getPrice()));
            orderDetailsList.add(orderDetails);
        }

        orderDetailsRepository.saveAll(orderDetailsList);

        order.setOrderDetails(orderDetailsList);

        // Clear the cart *after* successful order creation
        clearCart();
        return order;
    }
    private double calculateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
    @Override
    public List<Order> findOrdersByUserEmail(String email) {
        return orderRepository.findOrderByUserEmail(email);
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findOrdersByDateRange(Date startDate, Date endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    @Override
    public List<Order> findOrdersByBillingAddress(String billingAddress){
        return orderRepository.findOrderByBillingAddressContaining(billingAddress);
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }

    private void clearCart() {
        try {
            restTemplate.postForEntity(CART_SERVICE_URL + "/clear", null, String.class);
        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
        }
    }
}