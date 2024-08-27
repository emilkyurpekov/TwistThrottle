package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.motorcycleType;

import java.util.List;
@Repository
public interface MotorcycleRepository  extends JpaRepository<Motorcycle,Long> {
    List<Motorcycle> findByMake(String make);
    List<Motorcycle> findByModel(String model);
    List<Motorcycle> findByYear(int year);
    Motorcycle findById(int id);
    List<Motorcycle> findByUser(User user);
    List<Motorcycle> findAllByHorsepowerBetween(int minHP, int maxHP);
    List<Motorcycle> findByHorsepowerGreaterThan(int horsepower);
    List<Motorcycle> findByHorsepowerLessThan(int horsepower);
    List<Motorcycle> findAllByMotorcycleType(motorcycleType motorcycleType);
}
