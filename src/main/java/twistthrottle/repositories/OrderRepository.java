package twistthrottle.repositories;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Order;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findOrderByUserEmail(String email);
    List<Order> findByUserId(Long userId);
    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);
}
