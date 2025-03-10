package twistthrottle.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ValidInput_OrderCreatedSuccessfully() {
        ProductDTO mockProductDTO = new ProductDTO();
        mockProductDTO.setProductId(1L);
        mockProductDTO.setName("Test Product DTO");
        mockProductDTO.setPrice(10.0); // Price in the *DTO*
        mockProductDTO.setQuantity(100);

        // 2. Create the CartItem (using the DTO)
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(mockProductDTO, mockProductDTO.getPrice(), 2)); // Correct: DTO, price, quantity

        // 3. Create the User
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        Product mockProduct = new Product();
        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(mockProduct, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting ID on mock Product", e);
        }
        mockProduct.setName("Test Product Entity");
        mockProduct.setPrice(BigDecimal.valueOf(10.0));
        mockProduct.setStock(50);




        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });
        when(orderDetailsRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.createOrder(cartItems, "123 Main St", "456 Oak Ave", "test@example.com");


        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getId());
        assertEquals(orderStatus.PENDING, createdOrder.getOrderStatus());
        assertEquals(20.0, createdOrder.getTotalPrice());
        assertEquals(paymentMethod.CREDIT_CARD, createdOrder.getPaymentMethod());
        assertEquals("123 Main St", createdOrder.getShippingAddress());
        assertEquals("456 Oak Ave", createdOrder.getBillingAddress());
        assertEquals(mockUser, createdOrder.getUser());

        assertNotNull(createdOrder.getOrderDetails());
        assertEquals(1, createdOrder.getOrderDetails().size());
        OrderDetails orderDetails = createdOrder.getOrderDetails().get(0);
        assertEquals(2, orderDetails.getQuantity());
        assertEquals(BigDecimal.valueOf(10.0), orderDetails.getUnitPrice());

        assertEquals(mockProduct.getId(), orderDetails.getProduct().getId());
        assertEquals(mockProduct.getName(), orderDetails.getProduct().getName());
        // ... assert other Product entity properties ...

        // Verify mock interactions
        verify(userRepository).findByEmail("test@example.com");
        verify(productRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(orderDetailsRepository).saveAll(anyList());
    }
    @Test
    void createOrder_NullCartItems_ThrowsIllegalArgumentException() {
        String shippingAddress = "123 Main St";
        String billingAddress = "456 Oak Ave";
        String userEmail = "test@example.com";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(null, shippingAddress, billingAddress, userEmail));
        assertEquals("Cart items cannot be null or empty.", exception.getMessage());
    }
    @Test
    void createOrder_EmptyCart_ThrowsIllegalArgumentException() {
        List<CartItem> cartItems = new ArrayList<>();
        String shippingAddress = "123 Main St";
        String userEmail = "test@example.com";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(cartItems, shippingAddress, null, userEmail);  //billing address can be null
        });
        assertEquals("Cart items cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createOrder_NullShippingAddress_ThrowsIllegalArgumentException() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new ProductDTO(), 2, 10));
        String userEmail = "test@example.com";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(cartItems, null, "billing", userEmail));
        assertEquals("Shipping address cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createOrder_EmptyShippingAddress_ThrowsIllegalArgumentException() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new ProductDTO(), 2, 10));
        String userEmail = "test@example.com";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(cartItems, "", "billing", userEmail));
        assertEquals("Shipping address cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createOrder_UserNotFound_ThrowsIllegalArgumentException() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new ProductDTO(), 2, 10));
        String shippingAddress = "123 Main St";
        String userEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(cartItems, shippingAddress, null, userEmail));
        assertEquals("User with email " + userEmail + " not found.", exception.getMessage());
    }

    @Test
    void createOrder_ProductNotFound_ThrowsIllegalArgumentException() {
        List<CartItem> cartItems = new ArrayList<>();
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setProductId(999L);
        cartItems.add(new CartItem(mockProduct, 2, 10));

        String shippingAddress = "123 Main St";
        String userEmail = "test@example.com";
        User mockUser = new User();
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(cartItems, shippingAddress, null, userEmail));
        assertEquals("Product with ID " + 999L + " not found.", exception.getMessage());

        verify(userRepository).findByEmail(userEmail);
        verify(productRepository).findById(999L);
        verify(orderRepository, never()).save(any());
        verify(orderDetailsRepository, never()).saveAll(any());

    }

    @Test
    void createOrder_BillingAddressNull_UsesShippingAddress() {
        List<CartItem> cartItems = new ArrayList<>();
        ProductDTO mockProductDTO = new ProductDTO();
        mockProductDTO.setProductId(1L);
        mockProductDTO.setName("Test Product DTO");
        mockProductDTO.setPrice(20.0);
        mockProductDTO.setQuantity(50);

        cartItems.add(new CartItem(mockProductDTO, mockProductDTO.getPrice(), 1));

        String shippingAddress = "123 Main St";
        String userEmail = "test@example.com";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(productRepository.findProductById(1L)).thenReturn((List<Product>) mockProductDTO);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order savedOrder = i.getArgument(0);
            savedOrder.setId(1L);
            return savedOrder;
        });
        when(orderDetailsRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        Order createdOrder = orderService.createOrder(cartItems, shippingAddress, null, userEmail);

        assertNotNull(createdOrder);
        assertEquals(shippingAddress, createdOrder.getBillingAddress());
        assertEquals(1, createdOrder.getOrderDetails().size());
        OrderDetails orderDetails = createdOrder.getOrderDetails().get(0);
        assertEquals(1, orderDetails.getQuantity());
        assertEquals(BigDecimal.valueOf(20.0), orderDetails.getUnitPrice());
        assertEquals(mockProductDTO.getProductId(), orderDetails.getProduct().getId());
        assertEquals(mockProductDTO.getName(), orderDetails.getProduct().getName());

        verify(userRepository).findByEmail(userEmail);
        verify(productRepository).findProductById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(orderDetailsRepository).saveAll(anyList());
    }

    @Test
    void findOrdersByUserEmail_OrdersFound_ReturnsListOfOrders() {
        String userEmail = "test@example.com";
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findOrderByUserEmail(userEmail)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findOrdersByUserEmail(userEmail);

        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository).findOrderByUserEmail(userEmail);
    }

    @Test
    void findOrdersByUserEmail_NoOrdersFound_ReturnsEmptyList() {
        String userEmail = "test@example.com";
        when(orderRepository.findOrderByUserEmail(userEmail)).thenReturn(Collections.emptyList());

        List<Order> orders = orderService.findOrdersByUserEmail(userEmail);

        assertTrue(orders.isEmpty());
        verify(orderRepository).findOrderByUserEmail(userEmail);
    }
    @Test
    void findOrdersByUserId_OrdersFound_ReturnsListOfOrders() {
        // Arrange
        Long userId = 1L;
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.findOrdersByUserId(userId);

        // Assert
        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void findOrdersByUserId_NoOrdersFound_ReturnsEmptyList() {
        // Arrange
        Long userId = 1L;
        when(orderRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        List<Order> orders = orderService.findOrdersByUserId(userId);

        // Assert
        assertTrue(orders.isEmpty());
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void findOrdersByDateRange_OrdersFound_ReturnsListOfOrders() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findByOrderDateBetween(startDate, endDate)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findOrdersByDateRange(startDate, endDate);

        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository).findByOrderDateBetween(startDate, endDate);
    }
    @Test
    void findOrdersByDateRange_NoOrdersFound_ReturnsEmptyList() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(orderRepository.findByOrderDateBetween(startDate, endDate)).thenReturn(Collections.emptyList());

        List<Order> orders = orderService.findOrdersByDateRange(startDate, endDate);

        assertTrue(orders.isEmpty());
        verify(orderRepository).findByOrderDateBetween(startDate, endDate);
    }

    @Test
    void findOrdersByBillingAddress_OrdersFound_ReturnsListOfOrders() {
        String billingAddress = "123";
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findOrderByBillingAddressContaining(billingAddress)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findOrdersByBillingAddress(billingAddress);

        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository).findOrderByBillingAddressContaining(billingAddress);
    }
    @Test
    void findOrdersByBillingAddress_NoOrdersFound_ReturnsEmptyList() {
        String billingAddress = "123";
        when(orderRepository.findOrderByBillingAddressContaining(billingAddress)).thenReturn(Collections.emptyList());

        List<Order> orders = orderService.findOrdersByBillingAddress(billingAddress);

        assertTrue(orders.isEmpty());
        verify(orderRepository).findOrderByBillingAddressContaining(billingAddress);
    }

    @Test
    void findOrderById_OrderExists_ReturnsOrder() {
        Long orderId = 1L;
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Order actualOrder = orderService.findOrderById(orderId);

        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository).findById(orderId);
    }
    @Test
    void findOrderById_OrderDoesntExist_ReturnsGetOfNull() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> orderService.findOrderById(orderId));

        verify(orderRepository).findById(orderId);
    }
}