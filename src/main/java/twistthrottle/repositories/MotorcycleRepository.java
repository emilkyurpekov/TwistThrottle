package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import twistthrottle.models.entities.Motorcycle;

import java.util.List;

public interface MotorcycleRepository  extends JpaRepository<Motorcycle,Long> {
    List<Motorcycle> findByMake(String make);
    List<Motorcycle> findByModel(String model);
    List<Motorcycle> findByYear(int year);
    List<Motorcycle> findByCategory(String category);
}
