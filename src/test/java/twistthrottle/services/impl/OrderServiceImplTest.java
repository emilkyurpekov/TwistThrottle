package twistthrottle.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import twistthrottle.dtos.CartItem;
import twistthrottle.dtos.ProductDTO;
import twistthrottle.models.entities.Order;
import twistthrottle.models.entities.Product;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.orderStatus;
import twistthrottle.repositories.OrderDetailsRepository;
import twistthrottle.repositories.OrderRepository;
import twistthrottle.repositories.ProductRepository;
import twistthrottle.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private User mockUser;
    private Product mockProduct;
    private List<CartItem> cartItems;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setEmail("test@example.com");

        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setPrice(BigDecimal.valueOf(100.0));

        CartItem mockCartItem = new CartItem();
        mockCartItem.setProduct(new ProductDTO(1L, "Test Product", 100.0, 10));
        mockCartItem.setQuantity(2);
        mockCartItem.setPrice(100.0);

        cartItems = new ArrayList<>();
        cartItems.add(mockCartItem);

        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setUser(mockUser);
        mockOrder.setTotalPrice(200.0);
        mockOrder.setOrderStatus(orderStatus.PENDING);
        mockOrder.setOrderDate(new Date());
    }

    @Test
    void testCreateOrder_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        Order createdOrder = orderService.createOrder(cartItems, "123 Street", null, "test@example.com");

        assertNotNull(createdOrder);
        assertEquals(200.0, createdOrder.getTotalPrice());
        assertEquals(orderStatus.PENDING, createdOrder.getOrderStatus());
        assertEquals("123 Street", createdOrder.getShippingAddress());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderDetailsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateOrder_WithEmptyCart_ShouldThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(new ArrayList<>(), "123 Street", null, "test@example.com")
        );
        assertEquals("Cart items cannot be null or empty.", thrown.getMessage());
    }

    @Test
    void testCreateOrder_WithNullShippingAddress_ShouldThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(cartItems, null, null, "test@example.com")
        );
        assertEquals("Shipping address cannot be null or empty.", thrown.getMessage());
    }

    @Test
    void testCreateOrder_UserNotFound_ShouldThrowException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(cartItems, "123 Street", null, "test@example.com")
        );
        assertEquals("User with email test@example.com not found.", thrown.getMessage());
    }

    @Test
    void testCreateOrder_ProductNotFound_ShouldThrowException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                orderService.createOrder(cartItems, "123 Street", null, "test@example.com")
        );
        assertEquals("Product with ID 1 not found.", thrown.getMessage());
    }

    @Test
    void testFindOrdersByUserEmail() {
        List<Order> orders = List.of(mockOrder);
        when(orderRepository.findOrderByUserEmail("test@example.com")).thenReturn(orders);

        List<Order> foundOrders = orderService.findOrdersByUserEmail("test@example.com");

        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
    }

    @Test
    void testFindOrdersByUserId() {
        List<Order> orders = List.of(mockOrder);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        List<Order> foundOrders = orderService.findOrdersByUserId(1L);

        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
    }

    @Test
    void testFindOrdersByDateRange() {
        List<Order> orders = List.of(mockOrder);
        when(orderRepository.findByOrderDateBetween(any(Date.class), any(Date.class))).thenReturn(orders);

        List<Order> foundOrders = orderService.findOrdersByDateRange(new Date(), new Date());

        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
    }

    @Test
    void testFindOrdersByBillingAddress() {
        List<Order> orders = List.of(mockOrder);
        when(orderRepository.findOrderByBillingAddressContaining("123 Street")).thenReturn(orders);

        List<Order> foundOrders = orderService.findOrdersByBillingAddress("123 Street");

        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
    }

    @Test
    void testFindOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        Order foundOrder = orderService.findOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getId());
    }

    @Test
    void testFindOrderById_NotFound_ShouldThrowException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.findOrderById(1L));
    }
}