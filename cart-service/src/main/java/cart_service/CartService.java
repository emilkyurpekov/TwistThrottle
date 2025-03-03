package cart_service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private static final String CART_SESSION_KEY = "cart";

    public List<CartItem> getCart(HttpSession session) {
        Object cartObject = session.getAttribute(CART_SESSION_KEY);

        if (cartObject instanceof List<?>) {
            List<?> cartList = (List<?>) cartObject;

            List<CartItem> cart = cartList.stream()
                    .filter(obj -> obj instanceof CartItem)
                    .map(obj -> (CartItem) obj)
                    .collect(Collectors.toList());

            session.setAttribute(CART_SESSION_KEY, cart);
            return cart;
        }

        List<CartItem> newCart = new ArrayList<>();
        session.setAttribute(CART_SESSION_KEY, newCart);
        return newCart;
    }

    public void addToCart(HttpSession session, ProductDTO product, int quantity) {
        List<CartItem> cart = getCart(session);
        boolean productExists = false;

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            cart.add(new CartItem(product, product.getPrice(), quantity));
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public void updateItemQuantity(Long productId, int quantity, HttpSession session) {
        List<CartItem> cartItems = getCart(session);
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                session.setAttribute(CART_SESSION_KEY, cartItems);
                return;
            }
        }
    }
}
