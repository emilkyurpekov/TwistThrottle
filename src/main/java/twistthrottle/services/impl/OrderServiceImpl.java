// twistthrottle/services/impl/OrderServiceImpl.java
package twistthrottle.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twistthrottle.dtos.CartItem;
import twistthrottle.dtos.ProductDTO;
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

import java.lang.reflect.Field;
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

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository,
                            UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order createOrder(List<CartItem> cartItems, String shippingAddress, String billingAddress, String userEmail) {
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
        order.setPaymentMethod(paymentMethod.CREDIT_CARD);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress != null ? billingAddress : shippingAddress);
        order.setUser(user);
        order = orderRepository.save(order);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(order);

            Optional<Product> productOptional = productRepository.findById(cartItem.getProduct().getProductId());
            Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("Product with ID " + cartItem.getProduct().getProductId() + " not found."));

            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartItem.getQuantity());
            orderDetails.setUnitPrice(BigDecimal.valueOf(cartItem.getPrice()));
            orderDetailsList.add(orderDetails);
        }

        orderDetailsRepository.saveAll(orderDetailsList);

        order.setOrderDetails(orderDetailsList);
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
    private Product convertDtoToEntity(ProductDTO productDTO) {
        Product product = new Product();
        // This is essential!  Map the fields from the DTO to the entity.
        try {
            Field idField = Product.class.getDeclaredField("productId");
            idField.setAccessible(true);
            idField.set(product, productDTO.getProductId());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting ID on mock Product", e);
        }

        product.setName(productDTO.getName());
        product.setPrice(BigDecimal.valueOf(productDTO.getPrice()));
        product.setStock(productDTO.getQuantity());
        return product;
    }
    @Override
    public List<Order> findOrdersByBillingAddress(String billingAddress){
        return orderRepository.findOrderByBillingAddressContaining(billingAddress);
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }
}