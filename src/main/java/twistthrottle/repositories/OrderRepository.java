package twistthrottle.repositories;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findOrderById(int id);
    Order findOrderByUserEmail(String email);
}
