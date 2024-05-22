package twistthrottle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.enums.motorcycleType;

import java.util.List;

public interface MotorcycleRepository  extends JpaRepository<Motorcycle,Long> {
    List<Motorcycle> findByMake(String make);
    List<Motorcycle> findByModel(String model);
    List<Motorcycle> findByYear(int year);
    Motorcycle findById(int id);
    List<Motorcycle> findAllByHorsepowerBetween(int minHP, int maxHP);
    List<Motorcycle> findByHorsepowerGreaterThan(int horsepower);
    List<Motorcycle> findByHorsepowerLessThan(int horsepower);
    List<Motorcycle> findAllByMotorcycleType(motorcycleType motorcycleType);
}
