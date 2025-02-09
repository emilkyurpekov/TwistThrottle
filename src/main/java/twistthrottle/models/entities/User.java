package twistthrottle.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name",nullable = false)
    private String lastName;
    @Column(name = "username",nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Motorcycle> motorcycles = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;


    public void addMotorcycle(Motorcycle motorcycle) {
        motorcycles.add(motorcycle);
        motorcycle.setUser(this);
    }
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}
