package twistthrottle.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import twistthrottle.models.entities.enums.motorcycleType;

@Entity
@Table(name = "motorcycles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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



    @Column(nullable = false)
    private double volume;
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
