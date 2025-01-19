package twistthrottle.models.entities;
import twistthrottle.models.entities.enums.categoryType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private categoryType categoryType;
    @Column(length = 512)
    private String description;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public twistthrottle.models.entities.enums.categoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(twistthrottle.models.entities.enums.categoryType categoryType) {
        this.categoryType = categoryType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category() {
    }

    public Category(String name, twistthrottle.models.entities.enums.categoryType categoryType, String description) {
        this.name = name;
        this.categoryType = categoryType;
        this.description = description;
    }
}