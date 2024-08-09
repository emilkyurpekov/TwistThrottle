package twistthrottle.services;

import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.motorcycleType;

import java.util.List;

public interface MotorcycleService {
    List<Motorcycle> findByMake(String make);
    List<Motorcycle> findByModel(String model);
    List<Motorcycle> findByYear(int year);
    Motorcycle findById(int id);
    List<Motorcycle> getMotorcyclesByUser(User user);
    List<Motorcycle> findAllByHorsepowerBetween(int minHP, int maxHP);
    List<Motorcycle> findByHorsepowerGreaterThan(int horsepower);
    List<Motorcycle> findByHorsepowerLessThan(int horsepower);
    List<Motorcycle> findAllByMotorcycleType(motorcycleType motorcycleType);
}
