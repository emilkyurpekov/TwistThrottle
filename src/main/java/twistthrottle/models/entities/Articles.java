package twistthrottle.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "articles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Articles extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false, unique = true)
    private String slug;
}
