package twistthrottle.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import twistthrottle.repositories.ProductRepository;
import twistthrottle.repositories.UserRepository;
import twistthrottle.services.OrderService;
import twistthrottle.services.ProductService;

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
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final RestTemplate restTemplate;
    @Value("${cart.service.url}")
    private String cartServiceUrl;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderDetailsRepository orderDetailsRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            ProductService productService,
                            RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public Order createOrder(List<CartItem> cartItems, String shippingAddress, String cardNumber, String userEmail, String cookieHeader) {

        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User with email " + userEmail + " not found."));

        for (CartItem item : cartItems) {
            Long preCheckProductId = item.getProduct().getProductId();
            int preCheckQuantity = item.getQuantity();
            Product product = productRepository.findById(preCheckProductId)
                    .orElseThrow(() -> new RuntimeException("Product with ID " + preCheckProductId + " not found during pre-check."));
            if (product.getStock() < preCheckQuantity) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName() + " (ID: " + preCheckProductId + "). Requested: " + preCheckQuantity + ", Available: " + product.getStock());
            }
        }

        System.out.println("Received card number ending in: " + (cardNumber != null && cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : "****"));

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus(orderStatus.PENDING);
        order.setTotalPrice(calculateTotalPrice(cartItems));
        order.setPaymentMethod(paymentMethod.CREDIT_CARD);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(shippingAddress);
        order.setUser(user);

        order = orderRepository.save(order);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Long productId = cartItem.getProduct().getProductId();
            int quantityOrdered = cartItem.getQuantity();

            Product productToUpdate = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found during order processing."));

            int currentStock = productToUpdate.getStock();
            if (currentStock < quantityOrdered) {
                throw new IllegalStateException("Insufficient stock detected during final update for product: " + productToUpdate.getName() + " (ID: " + productId + ").");
            }

            int newStock = currentStock - quantityOrdered;
            productService.updateProductStock(productId, newStock);

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);
            Product productReference = productRepository.getReferenceById(productId);
            orderDetails.setProduct(productReference);
            orderDetails.setQuantity(quantityOrdered);
            orderDetails.setUnitPrice(BigDecimal.valueOf(cartItem.getPrice()));
            orderDetailsList.add(orderDetails);
        }

        orderDetailsRepository.saveAll(orderDetailsList);

        clearCartAfterOrder(cookieHeader);

        return order;
    }

    private void clearCartAfterOrder(String cookieHeader) {
        HttpHeaders headers = new HttpHeaders();
        if (cookieHeader != null) {
            headers.set("Cookie", cookieHeader);
        }
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    cartServiceUrl + "/clear",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println("Cart cleared successfully after order creation.");
        } catch (Exception e) {
            System.err.println("Warning: Failed to clear cart after order creation. Error: " + e.getMessage());
        }
    }

    private double calculateTotalPrice(List<CartItem> cartItems) {
        double total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
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
        return orderRepository.findByOrderDateBetween(startDate,endDate);
    }
    @Override
    public List<Order> findOrdersByBillingAddress(String billingAddress){
        return orderRepository.findOrderByBillingAddressContaining(billingAddress);
    }
    @Override
    public Order findOrderById(Long orderId){
        return orderRepository.findById(orderId).orElse(null);
    }
    @Override
    public List<Order> getOrdersByUser(Long userId){
        return orderRepository.findByUserId(userId);
    }
}