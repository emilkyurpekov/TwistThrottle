package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findOrderByUserEmail(String email);
    List<Order> findByUserId(Long userId);
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
    List<Order> findOrderByBillingAddressContaining(String billingAdress);

}
