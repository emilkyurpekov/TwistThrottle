package twistthrottle.services;

import org.springframework.stereotype.Service;
import twistthrottle.models.entities.Motorcycle;
import twistthrottle.models.entities.User;
import twistthrottle.models.entities.enums.motorcycleType;
import twistthrottle.repositories.MotorcycleRepository;

import java.util.List;

@Service
public class MotorcycleServiceImpl implements MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;

    public MotorcycleServiceImpl(MotorcycleRepository motorcycleRepository) {
        this.motorcycleRepository = motorcycleRepository;
    }

    @Override
    public List<Motorcycle> findByMake(String make) {
        return null;
    }

    @Override
    public List<Motorcycle> findByModel(String model) {
        return null;
    }

    @Override
    public List<Motorcycle> findByYear(int year) {
        return null;
    }

    @Override
    public Motorcycle findById(int id) {
        return null;
    }

    @Override
    public List<Motorcycle> findByUser(User user) {
        List<Motorcycle> motorcycles = motorcycleRepository.findByUser(user);
        System.out.println("Fetched " + motorcycles.size() + " motorcycles for user " + user.getUsername());
        return motorcycles;
    }

    @Override
    public List<Motorcycle> findAllByHorsepowerBetween(int minHP, int maxHP) {
        List<Motorcycle> motorcycles = motorcycleRepository.findAllByHorsepowerBetween(minHP,maxHP);

        return motorcycles;
    }

    @Override
    public List<Motorcycle> findByHorsepowerGreaterThan(int horsepower) {
        return null;
    }

    @Override
    public List<Motorcycle> findByHorsepowerLessThan(int horsepower) {
        return null;
    }

    @Override
    public List<Motorcycle> findAllByMotorcycleType(motorcycleType motorcycleType) {
        return null;
    }


    public void save(Motorcycle motorcycle) {
        motorcycleRepository.save(motorcycle);
    }
}
