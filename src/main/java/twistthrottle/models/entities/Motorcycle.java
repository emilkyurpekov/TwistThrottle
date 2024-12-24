package twistthrottle.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import twistthrottle.models.entities.enums.motorcycleType;

@Entity
@Table(name = "motorcycles")
public class Motorcycle extends BaseEntity{
    @Column(nullable = false)
    private String make;
    @Column(nullable = false)
    private String model;
    private int year;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private motorcycleType motorcycleType;
    @Column(nullable = false)
    private int horsepower;
    @Column(nullable = false)
    private double weight;
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    public String getMake() {
        return make;
    }

    public int getYear() {
        return year;
    }
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public motorcycleType getMotorcycleType() {
        return motorcycleType;
    }

    public void setMotorcycleType(motorcycleType motorcycleType) {
        this.motorcycleType = motorcycleType;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
