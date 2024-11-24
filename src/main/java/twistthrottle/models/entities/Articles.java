package twistthrottle.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Articles extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String content;
}
